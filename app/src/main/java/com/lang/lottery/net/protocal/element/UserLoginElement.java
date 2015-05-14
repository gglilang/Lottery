package com.lang.lottery.net.protocal.element;

import com.lang.lottery.net.protocal.Element;
import com.lang.lottery.net.protocal.Leaf;

import org.xmlpull.v1.XmlSerializer;

/**
 * 用户登陆用请求
 * Created by Lang on 2015/5/13.
 */
public class UserLoginElement extends Element {
    private Leaf actpassword = new Leaf("actpassword");
    @Override
    public void serializerElement(XmlSerializer serializer) {
        try {
            serializer.startTag(null, "element");

           actpassword.serializerLeaf(serializer);

            serializer.endTag(null, "element");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getTransactionType() {
        return "14001";
    }

    public Leaf getActpassword() {
        return actpassword;
    }
}
