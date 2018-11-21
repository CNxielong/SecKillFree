package org.seckill.dto;

/**
 * @Description: 封装秒杀VO信息 封装JSON信息
 * @Auther: X-Dragon
 * @Date: 2018/11/21 18:10
 * @Version: 1.0
 */
public class SeckillResult<T> {

    // 确定是否成功
    private  boolean success;

    //封装返回的结果 T
    private T data;

    //封装返回的错误信息
    private String msg;

    //成功时的构造方法
    public SeckillResult(boolean success, T data) {
        this.success = success;
        this.data = data;
    }
    //失败时的构造方法
    public SeckillResult(boolean success, String msg) {
        this.success = success;
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
