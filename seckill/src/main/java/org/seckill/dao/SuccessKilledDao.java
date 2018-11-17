package org.seckill.dao;

import org.seckill.entity.SuccessKilled;

/**
 * @Auther: XDragon
 * @Date: 2018/11/17 22:11
 * @Description: TODO 购买成功DAO
 * @Version: 1.0
 */
public interface SuccessKilledDao {

    /*
     * @description: TODO 插入购买明细
     * @date 2018/11/17 22:17
     * @param [seckillId, userPhone]
     * @return int 插入行数
     */
    int insertSuccessKilled(long seckillId, long userPhone);

    /*
     * @description: TODO 根据ID查询SuccessKilled并携带秒杀产品对象实体
     * @date 2018/11/17 22:20
     * @param [seckillId]
     * @return org.seckill.entity.SuccessKilled
     */
    SuccessKilled queryByIdSeckill(long seckillId);
}
