package org.seckill.dao.cache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dao.SeckillDao;
import org.seckill.entity.Seckill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
//告诉junitSpring的配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class RedisDaoTest {

    @Autowired
    RedisDao redisDao;

    @Autowired
    SeckillDao seckillDao;

    private long seckillId = 1001L;

    @Test
    public void testRedis(){
        Seckill seckill = redisDao.getSeckill(seckillId);
        if( null == seckill ){// 如果缓存中没有
            Seckill seckillMySql = seckillDao.queryById(seckillId);// 取数据库查询
            if (null != seckillMySql){ // 如果数据库中找到了
                String result = redisDao.setSeckill(seckillMySql);
                System.out.println("放入的redis结果:"+result);
                Seckill seckillRedis = redisDao.getSeckill(seckillId);
                System.out.println("取到的Redis是:"+seckillRedis);
            }
         }else{
            System.out.println("已经缓存到了Redis:"+seckill);
        }

    }
}