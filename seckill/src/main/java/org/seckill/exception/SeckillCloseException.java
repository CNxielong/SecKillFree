package org.seckill.exception;
/**
 * @Description: 秒杀关闭异常
 * @Auther: X-Dragon
 * @Date: 2018/11/19 14:31
 */

public class SeckillCloseException  extends SeckillException{

    public SeckillCloseException(String message) {
        super(message);
    }

    public SeckillCloseException(String message, Throwable cause) {
        super(message, cause);
    }
}
