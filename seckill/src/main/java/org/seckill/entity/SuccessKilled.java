package org.seckill.entity;

import java.util.Date;

/**
 * @Description: 秒杀成功明细表
 * @Auther: X-Dragon
 * @Date: 2018/11/11 19:32
 * @Version: 1.0
 */
public class SuccessKilled {

    private long seckillId;

    private long userPhone;

    // 1:成功, 0:秒杀结束, -1:重复秒杀, -2:系统异常, -3:数据篡改
    private short state;

    private Date createTime;

    //变通
    //多对一
    private Seckill seckill;

    @Override
    public String toString() {
        return "SuccessKilled{" +
                "seckillId=" + seckillId +
                ", userPhone=" + userPhone +
                ", state=" + state +
                ", createTime=" + createTime +
                ", seckill=" + seckill +
                '}';
    }

    public Seckill getSeckill() {
        return seckill;
    }

    public void setSeckill(Seckill seckill) {
        this.seckill = seckill;
    }

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public long getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(long userPhone) {
        this.userPhone = userPhone;
    }

    // 1:成功, 0:秒杀结束, -1:重复秒杀, -2:系统异常, -3:数据篡改
    public short getState() {
        return state;
    }

    public void setState(short state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
