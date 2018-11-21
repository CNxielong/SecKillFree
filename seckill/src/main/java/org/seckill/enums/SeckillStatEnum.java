package org.seckill.enums;

/**
 * @Description: 封装返回信息的枚举类
 * @Auther: X-Dragon
 * @Date: 2018/11/19 17:01
 * @Version: 1.0
 */
public enum SeckillStatEnum {
    SUCCESS(1,"秒杀成功"),
    END(0,"秒杀结束"),
    REPEAT_KILL(-1,"重复秒杀"),
    INSERT_ERROR(-2,"系统异常"),
    DATA_REWRITE(-3,"数据篡改"),
    TIME_ERROR(-4,"错过秒杀时间");

    //状态值
    private  int state;
    //状态信息
    private  String stateInfo;

    SeckillStatEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }
    // 获取方法
    public static SeckillStatEnum stateOf(int index) {
        for (SeckillStatEnum state : values()) {
            if (state.getState() == index) {
                return state;
            }
        }
        return null;
    }

}
