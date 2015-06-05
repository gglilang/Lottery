package com.lang.lottery.net.protocal.element;

import com.lang.lottery.net.protocal.Element;
import com.lang.lottery.net.protocal.Leaf;

import org.xmlpull.v1.XmlSerializer;

/**
 * 投注请求
 * Created by Lang on 2015/6/4.
 */
public class BetElement extends Element{

    // lotteryid string * 玩法编号
    private Leaf lotteryid = new Leaf("lotteryid");
    // issue string * 期号（当前销售期）
    // lotterycode string * 投注号码，注与注之间^分割
    private Leaf lotterycode = new Leaf("lotterycode");

    // issue string * 期号（当前销售期）
    private Leaf issue = new Leaf("issue");

    // lotterynumber string 注数
    private Leaf lotterynumber = new Leaf("lotterynumber");
    // lotteryvalue string 方案金额，以分为单位
    private Leaf lotteryvalue = new Leaf("lotteryvalue");
    // appnumbers string 倍数
    private Leaf appnumbers = new Leaf("appnumbers");
    // issuesnumbers string 追期
    private Leaf issuesnumbers = new Leaf("issuesnumbers");
    // issueflag int * 是否多期追号 0否，1多期
    private Leaf issueflag = new Leaf("issueflag");

    private Leaf bonusstop = new Leaf("bonusstop", "1");

    /********************************************/

    // actvalue int * 用户账户余额
    private String actvalue;

    public String getActvalue() {
        return actvalue;
    }

    public void setActvalue(String actvalue) {
        this.actvalue = actvalue;
    }

    /********************************************/

    public Leaf getLotteryid() {
        return lotteryid;
    }

    public Leaf getLotterycode() {
        return lotterycode;
    }

    public Leaf getIssue() {
        return issue;
    }

    public Leaf getLotterynumber() {
        return lotterynumber;
    }

    public Leaf getLotteryvalue() {
        return lotteryvalue;
    }

    public Leaf getAppnumbers() {
        return appnumbers;
    }

    public Leaf getIssuesnumbers() {
        return issuesnumbers;
    }

    public Leaf getIssueflag() {
        return issueflag;
    }

    public Leaf getBonusstop() {
        return bonusstop;
    }

    @Override
    public void serializerElement(XmlSerializer serializer) {

    }

    @Override
    public String getTransactionType() {
        return "12006";
    }
}
