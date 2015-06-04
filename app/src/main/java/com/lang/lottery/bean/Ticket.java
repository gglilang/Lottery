package com.lang.lottery.bean;

/**
 * 用户投注信息封装
 * Created by Lang on 2015/6/1.
 */
public class Ticket {
    private String redNum;
    private String blueNum;

    private int num;    // 注数


    public String getRedNum() {
        return redNum;
    }

    public void setRedNum(String redNum) {
        this.redNum = redNum;
    }

    public String getBlueNum() {
        return blueNum;
    }

    public void setBlueNum(String blueNum) {
        this.blueNum = blueNum;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
