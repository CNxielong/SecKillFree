package org.seckill.exception;
/**
 * @Description: 秒杀相关业务异常
 * @Auther: X-Dragon
 * @Date: 2018/11/19 14:35
 */

public class SeckillException extends  RuntimeException {
    public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }
}
