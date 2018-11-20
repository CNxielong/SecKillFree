package org.seckill.exception;

/**
 * @Description: 重复秒杀异常
 * @Auther: X-Dragon
 * @Date: 2018/11/19 14:23
 */
public class RepeatKillException extends  SeckillException{
    public RepeatKillException(String message) {
        super(message);
    }

    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }
}
