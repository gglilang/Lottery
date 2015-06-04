package com.lang.lottery.net.protocal.element;

import com.lang.lottery.net.protocal.Element;

import org.xmlpull.v1.XmlSerializer;

/**
 * Created by Lang on 2015/6/4.
 */
public class BalanceElement extends Element{

    /******************回复信息**********************/
    //investvalues 可投注金额
    private String investvalues;

    public String getInvestvalues() {
        return investvalues;
    }

    public void setInvestvalues(String investvalues) {
        this.investvalues = investvalues;
    }

    @Override
    public void serializerElement(XmlSerializer serializer) {

    }

    @Override
    public String getTransactionType() {
        return "11007";
    }
}
