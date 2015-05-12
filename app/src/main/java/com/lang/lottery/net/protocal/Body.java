package com.lang.lottery.net.protocal;

import android.util.Xml;

import com.lang.lottery.ConstantValue;
import com.lang.lottery.util.DES;

import org.apache.commons.lang3.StringUtils;
import org.xmlpull.v1.XmlSerializer;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * 消息体节点封装
 * Created by Lang on 2015/5/11.
 */
public class Body {
    private List<Element> elements = new ArrayList<>();

    public List<Element> getElements() {
        return elements;
    }

    /**
     * 序列化请求
     */
    public void serializerBody(XmlSerializer serializer){

//        <body>
//          <elements>
//              <element>
//                  <lotteryid>118</lotteryid>
//                  <issues>1</issues>
//              </element>
//          </elements>
//        </body>

        try {
            serializer.startTag(null, "body");
            serializer.startTag(null, "elements");

            for(Element element : elements){
                element.serializerElement(serializer);
            }

            serializer.endTag(null, "elements");
            serializer.endTag(null, "body");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取到完整的body
     * @return
     */
    public String getWholeBody(){

        StringWriter writer = new StringWriter();

        XmlSerializer temp = Xml.newSerializer();

        try{
            temp.setOutput(writer);
            this.serializerBody(temp);
            //output will be flushed
            temp.flush();
            return writer.toString();

        } catch (Exception e){
            e.printStackTrace();
        }


        return null;
    }

    public String getBodyInsideDESInfo(){

        //加密数据
        String wholeBody = getWholeBody();
        String orgDesInfo = StringUtils.substringBetween(wholeBody, "<body>", "</body>");

        DES des = new DES();
        return des.authcode(wholeBody, "DECODE", ConstantValue.DES_PASSWORD);

    }
}
