package com.lang.lottery.net.protocal;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

/**
 * 请求数据的封装
 * 不会将所有的请求用到的叶子放到Element中
 * Element将作为所有请求的代表，Element请求的公共部分
 * Created by Lang on 2015/5/11.
 */
public abstract class Element {
    //包含内容
    //序列化
    //特有：请求标示


    /**
     * 每个请求都需要序列化自己
     * @param serializer
     */
    public abstract void serializerElement(XmlSerializer serializer);

    /**
     * 每个请求都有自己的表示
     * @return
     */
    public abstract String getTransactionType();


    //<lotteryid>118</lotteryid>
//    private Leaf lotteryid = new Leaf("lotteryid");
//
//    //<issues>1</issues>
//    private Leaf issues = new Leaf("issues", "1");
//
//    public Leaf getLotteryid() {
//        return lotteryid;
//    }

    /**
     * 序列化请求
     */
//    public void serializerElement(XmlSerializer serializer){
//        try {
//            serializer.startTag(null, "element");
//
//            lotteryid.serializerLeaf(serializer);
//            issues.serializerLeaf(serializer);
//
//            serializer.endTag(null, "element");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    public String getTransactionType(){
//        return "12002";
//    }
}
