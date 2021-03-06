package org.seckill.service.impl;

import org.apache.commons.collections.MapUtils;
import org.seckill.dao.SeckillDao;
import org.seckill.dao.SuccessKilledDao;
import org.seckill.dao.cache.RedisDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStatEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @Description:
 * @Auther: X-Dragon
 * @Date: 2018/11/19 15:41
 * @Version: 1.0
 */
@Service
// @Component // 也行
public class SeckillServiceImpl implements SeckillService {

    // Logger日志
    Logger logger = LoggerFactory.getLogger(this.getClass());

    // 两个dao
    @Autowired
    private SeckillDao seckillDao;
    @Autowired
    private SuccessKilledDao successKilledDao;

    @Autowired
    private RedisDao redisDao;

    // md5盐值字符串，用于混淆MD5
    private static final String salt = "xielong";

    @Override
    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0, 4);
    }

    @Override
    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    /**
     * @Auther: XDragon
     * @Date: 2018/11/20 14:15
     * @Description: TODO 暴露秒杀接口
     * @Version: 1.0
     */
    @Override
    public Exposer exportSeckillUrl(long seckillId) {
        // 优化点：缓存优化

        // 1 从Redis 获取秒杀实体
        Seckill seckill = redisDao.getSeckill(seckillId);
        if (null == seckill) { // 如果Redis中没有缓存
            // 2 取数据库查询秒杀实体
            seckill = seckillDao.queryById(seckillId);
            if (null == seckill) { // 如果数据库中也不存在
                return new Exposer(false, seckillId);
            }else {
                // 3 放入Redis
                redisDao.setSeckill(seckill);
            }
        }

        // 当前系统时间、开始时间、结束时间
        Long now = new Date().getTime();//getTime()
        Long start = seckill.getStartTime().getTime();
        Long end = seckill.getEndTime().getTime();
        // 如果当前系统时间<开始时间或者 当前系统时间>结束时间 抛出异常
        if (now < start || now > end) {
            return new Exposer(false, seckillId, now, start, end);
        }

        // 获得MD5
        String md5 = getMD5(seckillId);
        // 执行秒杀
        return new Exposer(true, md5, seckillId);
    }

    private String getMD5(long seckillId) {
        String base = seckillId + "/" + salt;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    /**
     * @Auther: XDragon
     * @Date: 2018/11/20 14:16
     * @Description: TODO 执行秒杀过程
     * @Version: 1.0
     */
    @Override
    @Transactional
    /**
     * 使用注解控制事务的优点:
     * 1.开发团队达成一致约定,明确标注事务方法的编程风格.
     * 2.保证事务方法的执行时间尽可能短,不要穿插其他网络操作RPC/HTTP请求或者剥离到事务方法外部.
     * 3.不是所有的方法都需要事务.如一些查询的service.只有一条修改操作的service.
     */
    public SeckillExecution excuteSeckill(long seckillId, long userPhone, String md5)
            throws SeckillException, RepeatKillException, SeckillCloseException {

        // 判断MD5是否为空
        if (null == md5 || !md5.equals(getMD5(seckillId))) {
            throw new SeckillException(SeckillStatEnum.INNER_ERROR.getStateInfo());
        }
        Date now = new Date();

        try {
            // 记录秒杀行为 插入
            int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
            if (insertCount <= 0) {// 插入失败
                // 重复秒杀
                throw new RepeatKillException(SeckillStatEnum.REPEAT_KILL.getStateInfo());
            } else {// 插入成功

                // 执行秒杀过程 1、减库存热点商品竞争 2、记录购买行为
                int updateCount = seckillDao.reduceNumber(seckillId, now);
                // 减库存 失败
                if (updateCount <= 0) {
                    // 没有更新到记录，秒杀结束，rollback
                    throw new SeckillCloseException(SeckillStatEnum.TIME_ERROR.getStateInfo());
                } else {
                    // 秒杀成功 commit
                    SuccessKilled successKilled = successKilledDao.queryByIdSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, 1, SeckillStatEnum.SUCCESS.getStateInfo(), successKilled);
                }

            }

        } catch (SeckillCloseException e1) {
            throw e1;
        } catch (RepeatKillException e2) {
            throw e2;
        } catch (Exception e) {
            logger.error(e.getMessage());
            // 所有的编译期异常转化为运行期异常,spring的声明式事务做rollback
            throw new SeckillException("seckill inner error: " + e.getMessage());
        }

    }

    @Override
    public SeckillExecution executeSeckillProcedure(long seckillId, long userPhone, String md5) {
        // 判断MD5是否为空或者有改动
        if(md5 == null || !md5.equals(getMD5(seckillId)) ){
            return new SeckillExecution(seckillId, SeckillStatEnum.DATA_REWRITE);
        }
        // 当前系统时间
        Date killTime = new Date();
        // 封装参数
        HashMap<String,Object> map = new HashMap<String, Object>();
        map.put("seckillId", seckillId);
        map.put("phone", userPhone);
        map.put("killTime", killTime);
        map.put("result", null);// 输出参数 用out

            // 调用存储过程秒杀 result被赋值
        try {
            seckillDao.killByProcedure(map);
            // 获取result
            int result = MapUtils.getInteger(map, "result", -2);
            if( result == 1 ){
                SuccessKilled successKilled = successKilledDao.queryByIdSeckill(seckillId, userPhone);
                return new SeckillExecution(seckillId,SeckillStatEnum.SUCCESS.getState(),SeckillStatEnum.SUCCESS.getStateInfo(),successKilled);
            }else {
                return new SeckillExecution(seckillId, SeckillStatEnum.stateOf(result));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new SeckillExecution(seckillId, SeckillStatEnum.INNER_ERROR);
        }

    }


}
