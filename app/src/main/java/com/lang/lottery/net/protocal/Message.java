package com.lang.lottery.net.protocal;

import android.util.Xml;

import com.lang.lottery.ConstantValue;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.StringWriter;

/**
 * 协议封装
 * Created by Lang on 2015/5/11.
 */
public class Message {

    public Header getHeader() {
        return header;
    }

    public Body getBody() {
        return body;
    }

    private Header header = new Header();
    private Body body = new Body();

    /**
     * 序列化协议
     */
    public void serializerMessage(XmlSerializer serializer){
        try {
            //<message version="1.0">
            serializer.startTag(null, "message");
            serializer.attribute(null, "version", "1.0");

            header.serializerHeader(serializer, body.getWholeBody());    //获取完整的body
//            body.serializerBody(serializer);
            serializer.startTag(null, "body");
            serializer.text(body.getBodyInsideDESInfo());
            serializer.endTag(null, "body");

            serializer.endTag(null, "message");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取请求的xml文件
     * @return
     */
    public String getXml(Element element){
        if(element == null){
            throw new IllegalArgumentException("element is null");
        }

        header.getTransactiontype().setTagValue(element.getTransactionType());
        body.getElements().add(element);
        //序列化
        XmlSerializer serializer = Xml.newSerializer();
        StringWriter write = new StringWriter();
        try {
            serializer.setOutput(write);
            serializer.startDocument(ConstantValue.ENCONDING, null);
            this.serializerMessage(serializer);
            serializer.endDocument();
            return write.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
