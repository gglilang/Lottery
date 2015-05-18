package com.lang.lottery.engine.impl;

import android.util.Xml;

import com.lang.lottery.ConstantValue;
import com.lang.lottery.bean.User;
import com.lang.lottery.engine.BaseEngine;
import com.lang.lottery.engine.UserEngine;
import com.lang.lottery.net.HttpClientUtil;
import com.lang.lottery.net.protocal.Message;
import com.lang.lottery.net.protocal.element.UserLoginElement;
import com.lang.lottery.util.DES;

import org.apache.commons.codec.digest.DigestUtils;
import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.io.StringReader;


/**
 * Created by Lang on 2015/5/13.
 */
public class UserEngineImpl extends BaseEngine implements UserEngine {


    public Message login(User user){

        //第一步：获取登陆用的xml
        //创建登陆用Element
        UserLoginElement element = new UserLoginElement();
        //设置用户数据
        element.getActpassword().setTagValue(user.getPassword());
        //Message.getXml(element)
        Message message = new Message();
        message.getHeader().getUsername().setTagValue(user.getUsername());
        String xml = message.getXml(element);

        //如果第三步对比通过result，否则返回空
        Message result = getResult(xml);

        //第四步：请求结果的数据处理
        //body部分的第二次解析，解析的是明文内容
        XmlPullParser parser = Xml.newPullParser();
        try {
            //body明文（解析+解密DES）
            DES des = new DES();
            String body = "<body>" + des.authcode(result.getBody().getServiceBodyInsideDESInfo(), "ENCODE", ConstantValue.DES_PASSWORD) + "</body>";

            parser.setInput(new StringReader(body));
            int eventType = parser.getEventType();
            String name;

            while(eventType != XmlPullParser.END_DOCUMENT){

                switch (eventType){
                    case XmlPullParser.START_TAG:
                        name = parser.getName();
                        if("errorcode".equals(name)) {
                            result.getBody().getOelement().setErrorcode(parser.nextText());
                        }
                        if("errormsg".equals(name)) {
                            result.getBody().getOelement().setErrorcode(parser.nextText());
                        }
                        break;
                }
                eventType = parser.getEventType();
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }





    public Message login1(User user){

        //第一步：获取登陆用的xml
        //创建登陆用Element
        UserLoginElement element = new UserLoginElement();
        //设置用户数据
        element.getActpassword().setTagValue(user.getPassword());
        //Message.getXml(element)
        Message message = new Message();
        message.getHeader().getUsername().setTagValue(user.getUsername());
        String xml = message.getXml(element);

        //第二步：发送xml到服务器端，等待回复
        //HttpClientUtil.sendXml
        //在这行代码前，没有判断网络类型
        HttpClientUtil util = new HttpClientUtil();
        InputStream is = util.sendXml(ConstantValue.LOTTERY_URI, xml);
        //判断输入流非空
        if(is != null) {

            Message result = new Message();

            //第三步：数据的检验（MD5数据检验）
            //timestamp+digest+body
            XmlPullParser parser = Xml.newPullParser();
            try {
                parser.setInput(is, ConstantValue.ENCONDING);
                int eventType = parser.getEventType();
                String name;

                while(eventType != XmlPullParser.END_DOCUMENT){

                    switch (eventType){
                        case XmlPullParser.START_TAG:
                            name = parser.getName();
                            if("timestamp".equals(name)) {
                                result.getHeader().getTimestamp().setTagValue(parser.nextText());
                            }
                            if("digest".equals(name)){
                                result.getHeader().getDigest().setTagValue(parser.nextText());
                            }
                            if("body".equals(name)){
                                result.getBody().setServiceBodyInsideDESInfo(parser.nextText());
                            }
                            break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            //原始数据还原：时间戳（解析）+密码（常量）+body明文（解析DES)
            //body明文（解析+解密DES）
            DES des = new DES();
            String body = "<body>" + des.authcode(result.getBody().getServiceBodyInsideDESInfo(), "ENCODE", ConstantValue.DES_PASSWORD) + "</body>";
            String orgInfo = result.getHeader().getTimestamp().getTagValue()
                    + ConstantValue.AGENTER_PASSWORD
                    + body;

            //利用工具生成手机端的MD5
            String md5Hex = DigestUtils.md5Hex(orgInfo);
            //将手机端与服务器的进行比对
            if(md5Hex.endsWith(result.getHeader().getDigest().getTagValue())){
                //第四步：请求结果的数据处理
                //body部分的第二次解析，解析的是明文内容
                parser = Xml.newPullParser();
                try {
                    parser.setInput(new StringReader(body));
                    int eventType = parser.getEventType();
                    String name;

                    while(eventType != XmlPullParser.END_DOCUMENT){

                        switch (eventType){
                            case XmlPullParser.START_TAG:
                                name = parser.getName();
                                if("errorcode".equals(name)) {
                                    result.getBody().getOelement().setErrorcode(parser.nextText());
                                }
                                if("errormsg".equals(name)) {
                                result.getBody().getOelement().setErrorcode(parser.nextText());
                            }
                                break;
                        }
                        eventType = parser.getEventType();
                    }
                    return result;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }
}
