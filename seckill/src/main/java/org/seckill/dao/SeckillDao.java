package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.Seckill;

import java.util.Date;
import java.util.List;

/**
 * @Auther: XDragon
 * @Date: 2018/11/17 21:26
 * @Description: TODO 秒杀接口
 * @Version: 1.0
 */
public interface SeckillDao {

    /*
     * @author X-Dragon
     * @description: 减库存
     * @date 2018/11/17 22:05
     * @param [seckillId, killTime]
     * @return int 如果行数>1,表示更新的记录行数
     */
    int reduceNumber(@Param("seckillId") long seckillId, @Param("killTime") Date killTime);

    /*
     * @author X-Dragon
     * @description: TODO 根据秒杀ID查询对象
     * @date 2018/11/17 22:07
     * @param [seckillId]
     * @return org.seckill.entity.Seckill
     */
    Seckill queryById(long seckillId);

    /*
     * @author X-Dragon
     * @description: TODO 根据偏移量查询秒杀商品列表
     * @date 2018/11/17 22:08
     * @param [offet, limit]
     * @return java.util.List<org.seckill.entity.Seckill>
     */
    List<Seckill> queryAll(@Param("offset") int offset, @Param("limit") int limit);
}
