package com.lang.lottery.net.protocal;

import org.xmlpull.v1.XmlSerializer;

/**
 * xml文件的简单叶子
 * Created by Lang on 2015/5/11.
 */
public class Leaf {
    private String tagName;
    private String tagValue;

    /**
     * 每个叶子需要指定标签名称
     * @param tagName
     */
    public Leaf(String tagName) {
        this.tagName = tagName;
    }

    //处理常量
    public Leaf(String tagName, String tagValue) {
        this.tagName = tagName;
        this.tagValue = tagValue;
    }

    public String getTagValue() {
        return tagValue;
    }

    public void setTagValue(String tagValue) {
        this.tagValue = tagValue;
    }

    /**
     * 序列化叶子
     * @param serializer
     */
    public void serializerLeaf(XmlSerializer serializer){

        try{
            serializer.startTag(null, tagName);
            if(tagValue == null){
                tagValue = "";
            }
            serializer.text(tagValue);
            serializer.endTag(null, tagName);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
