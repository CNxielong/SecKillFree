package org.seckill.dao.cache;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.seckill.entity.Seckill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @Description: 用Redis存储缓存
 * @Auther: X-Dragon
 * @Date: 2018/11/26 19:52
 * @Version: 1.0
 */
public class RedisDao {

    // Logger日志
    private static final Logger LOG = LoggerFactory.getLogger(RedisDao.class);

    // jedis连接池
    private final JedisPool jedisPool;

    // 序列化工具
    private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);

    // 构造方法
    public RedisDao(String ip, int port) {
        jedisPool = new JedisPool(ip, port);
    }

    public Seckill getSeckill(long seckillId) {
        // redis 操作逻辑
        try {
                Jedis jedis = jedisPool.getResource();
            try {
                String key = "seckill:"+seckillId;
                // 实现内部序列化
                // get ->byte[] ->反序列化 ->Object(Seckill)
                byte[] bytes = jedis.get(key.getBytes());
                if(bytes != null){
                    // 空对象
                    Seckill seckill  = schema.newMessage();
                    ProtostuffIOUtil.mergeFrom(bytes, seckill, schema);
                    // seckill 被反序列化
                    return seckill;
                }
            } finally {
                jedis.close(); // 不是jedisPool的close()方法 否则取不到连接
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }

        return null;
    }

    public String setSeckill(Seckill seckill) {
        //set:Object(Seckill)->序列化->byte[] ->发送给redis
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                String key = "seckill:" + seckill.getSeckillID();
                byte[] bytes = ProtostuffIOUtil.toByteArray(seckill, schema, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
                //超时缓存
                int timeOut = 60 * 60;
                String result = jedis.setex(key.getBytes(), timeOut, bytes);
                return result;
            } finally {
                jedis.close();
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return null;
    }

}
