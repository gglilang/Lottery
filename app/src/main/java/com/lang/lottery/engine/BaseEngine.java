package com.lang.lottery.engine;

import android.util.Xml;

import com.lang.lottery.ConstantValue;
import com.lang.lottery.net.HttpClientUtil;
import com.lang.lottery.net.protocal.Message;
import com.lang.lottery.util.DES;

import org.apache.commons.codec.digest.DigestUtils;
import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;

/**
 * Created by Lang on 2015/5/14.
 */
public class BaseEngine {

    public Message getResult(String xml){
        //第一步和第三步

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
                                result.getBody().setServiceBodyInsideDESINFO(parser.nextText());
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
            String body = "<body>" + des.authcode(result.getBody().getServiceBodyInsideDESINFO(), "ENCODE", ConstantValue.DES_PASSWORD) + "</body>";
            String orgInfo = result.getHeader().getTimestamp().getTagValue()
                    + ConstantValue.AGENTER_PASSWORD
                    + body;

            //利用工具生成手机端的MD5
            String md5Hex = DigestUtils.md5Hex(orgInfo);
            //将手机端与服务器的进行比对
            if(md5Hex.endsWith(result.getHeader().getDigest().getTagValue())){
                return result;
            }
        }
        return null;
    }
}
