package com.lang.lottery.bean;

import com.lang.lottery.GlobalParams;

import java.util.ArrayList;
import java.util.List;

/**
 * 购物车
 * Created by Lang on 2015/6/1.
 */
public class ShoppingCart {
    public ShoppingCart() {
    }

    private static ShoppingCart instance = new ShoppingCart();

    public static ShoppingCart getInstance() {
        return instance;
    }
    // 投注
    // lotteryid string * 玩法编号
    // issue string * 期号（当前销售期）
    // lotterycode string * 投注号码，注与注之间^分割
    // lotterynumber string 注数
    // lotteryvalue string 方案金额，以分为单位

    // appnumbers string 倍数
    // issuesnumbers string 追期
    // issueflag int * 是否多期追号 0否，1多期
    // bonusstop int * 中奖后是否停止：0不停，1停

    private Integer lotteryid;
    private String issue;

    private List<Ticket> tickets = new ArrayList<>();

    private Integer lotterynumber;  // 计算
    private Integer lotteryvalue;

    private Integer appnumbers = 1;
    private Integer issuenumbers = 1;

    public Integer getAppnumbers() {
        return appnumbers;
    }

    public Integer getIssuenumbers() {
        return issuenumbers;
    }

    public Integer getLotteryid() {
        return lotteryid;
    }

    public void setLotteryid(Integer lotteryid) {
        this.lotteryid = lotteryid;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public Integer getLotterynumber() {
        lotterynumber = 0;
        for(Ticket item : tickets){
            lotterynumber += item.getNum();
        }
        return lotterynumber;
    }

    public Integer getLotteryvalue() {
        lotteryvalue = getLotterynumber() * 2 * appnumbers * issuenumbers;
        return lotteryvalue;
    }

    /**
     * 操作倍数
     * @return
     */
    public boolean addAppnumbers(boolean isAdd) {
        if (isAdd) {
            appnumbers++;
            if (appnumbers > 99) {
                appnumbers--;
                return false;
            }
            if (getLotteryvalue() > GlobalParams.MONEY) {
                appnumbers--;
                return false;
            }
        } else {
            appnumbers--;
            if(appnumbers == 0){
                appnumbers++;
                return false;
            }
        }
        return true;
    }

    /**
     * 操作追期
     * @return
     */
    public boolean addIssuenumbers(boolean isAdd) {
        if (isAdd) {
            issuenumbers++;
            if (issuenumbers > 99) {
                issuenumbers--;
                return false;
            }
            if (getLotteryvalue() > GlobalParams.MONEY) {
                issuenumbers--;
                return false;
            }
        } else {
            issuenumbers--;
            if(issuenumbers == 0){
                issuenumbers++;
                return false;
            }
        }
        return true;
    }

    public void clear() {
        instance = null;
        instance = new ShoppingCart();
    }
}
