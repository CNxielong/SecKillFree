package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.SuccessKilled;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * @Description: SuccessKilledDaoTest的测试类
 * @Auther: X-Dragon
 * @Date: 2018/11/18 18:04
 */

@RunWith(SpringJUnit4ClassRunner.class)
//告诉junitSpring的配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessKilledDaoTest {

    @Resource // @Autowired
    private SuccessKilledDao successKilledDao;

    @Test
    /**
     * 第一次执行:成功秒杀了1件商品
     * 第二次执行:成功秒杀了0件商品
     * 常见错误:### Cause: com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException: Duplicate entry '1000-17744494563' for key 'PRIMARY'
     * 解决:sql加上ignore忽略主键冲突  INSERT ignore INTO
     */
    public void insertSuccessKilled() {
        //    int insertSuccessKilled(long seckillId, long userPhone);
//        Long seckillId = 1000L;
//        Long userPhone = 17744494563l;
//        int result = successKilledDao.insertSuccessKilled(seckillId, userPhone);
//        System.out.println("成功秒杀了"+result+"件商品");
        long seckillId = 1002L;
        long userPhone = 17744494563L;
        int result = successKilledDao.insertSuccessKilled(seckillId, userPhone);
        System.out.println("成功秒杀了"+result+"件商品");
    }

    @Test
    public void queryByIdSeckill() {
        Long seckillId = 1002L;
        long userPhone = 17744494563L;
        SuccessKilled successKilled = successKilledDao.queryByIdSeckill(seckillId,userPhone);
        System.out.println("查询到的是:"+successKilled);
        System.out.println("查询到的商品是:"+successKilled.getSeckill());
        /**
         * 查询到的是:SuccessKilled
         * {seckillId=1000,
         * userPhone=17744494563,
         * state=1,
         * createTime=Sun Nov 18 18:24:46 CST 2018,
         * seckill=Seckill{seckillID=1000, name='1000元秒杀小米MIX2S', number=0, startTime=Sun Nov 11 00:00:00 CST 2018, endTime=Thu Nov 22 23:59:59 CST 2018, createTime=Tue Nov 20 00:40:12 CST 2018}}
         */
        /**
         * 查询到的商品是:Seckill
         * {seckillID=1000,
         * name='1000元秒杀小米MIX2S',
         * number=0,
         * startTime=Sun Nov 11 00:00:00 CST 2018,
         * endTime=Thu Nov 22 23:59:59 CST 2018,
         * createTime=Tue Nov 20 00:40:12 CST 2018}
         */
    }
}