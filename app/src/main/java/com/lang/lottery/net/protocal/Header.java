package com.lang.lottery.net.protocal;

import com.lang.lottery.ConstantValue;

import org.apache.commons.codec.digest.DigestUtils;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 头结点的封装
 * Created by Lang on 2015/5/11.
 */
public class Header {

//    <agenterid>889931</agenterid>
    private Leaf agenterid = new Leaf("agenterid", ConstantValue.AGENTERID);
//    <source>ivr</source>
    private Leaf source = new Leaf("source", ConstantValue.SOURCE);
//    <compress>DES</compress>
    private Leaf compress = new Leaf("COMPRESS", ConstantValue.COMPRESS);


//    <messengerid>20091113101533000001</messengerid>
    private Leaf messengerid = new Leaf("messengerid");
//    <timestamp>20091113101533</timestamp>
    private Leaf timestamp = new Leaf("timestamp");
//    <digest>7ec8582632678032d25866bd4bce114f</digest>
    private Leaf digest = new Leaf("digest");


//    <transactiontype>12002</transactiontype>
    private Leaf transactiontype = new Leaf("transactiontype");
//    <username>13200000000</username>
    private Leaf username = new Leaf("username");

    public void serializerHeader(XmlSerializer serializer, String body){

        //为timestamp、messagerid、digest设置数据
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String time = format.format(new Date());
        timestamp.setTagValue(time);

        //messagerid:时间戳+六位的随机数
        Random random = new Random();
        int number = random.nextInt(999999) + 1;    //[1,999999]
        DecimalFormat decimalFormat = new DecimalFormat("000000");
        messengerid.setTagValue(time + decimalFormat.format(number));

        //digest:时间戳+代理商的密码+完整body（明文）
        String orgInfo = time + ConstantValue.AGENTER_PASSWORD + body;
        String md5Hex = DigestUtils.md5Hex(orgInfo);
        digest.setTagValue(md5Hex);

        try {
            serializer.startTag(null, "header");

            agenterid.serializerLeaf(serializer);
            source.serializerLeaf(serializer);
            compress.serializerLeaf(serializer);
            messengerid.serializerLeaf(serializer);
            timestamp.serializerLeaf(serializer);
            digest.serializerLeaf(serializer);
            transactiontype.serializerLeaf(serializer);
            username.serializerLeaf(serializer);

            serializer.endTag(null, "header");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Leaf getTransactiontype() {
        return transactiontype;
    }

    public Leaf getUsername() {
        return username;
    }


    /*******************处理服务器回复*****************************/
    public Leaf getDigest() {
        return digest;
    }

    public Leaf getTimestamp() {
        return timestamp;
    }
}
