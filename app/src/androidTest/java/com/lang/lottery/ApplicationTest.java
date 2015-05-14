package com.lang.lottery;

import android.app.Application;

import android.test.ApplicationTestCase;
import android.util.Log;


import com.lang.lottery.bean.User;
import com.lang.lottery.engine.UserEngine;
import com.lang.lottery.engine.UserEngineImpl;
import com.lang.lottery.net.NetUtil;
import com.lang.lottery.net.protocal.Element;
import com.lang.lottery.net.protocal.Message;
import com.lang.lottery.net.protocal.element.CurrentIssueElement;
import com.lang.lottery.util.BeanFactory;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {

    private static final String TAG = "ApplicationTest";

    public ApplicationTest() {


        super(Application.class);
    }


    public void test2() {
        // System.out.println("sdfsfsfsfsf");
        Message message = new Message();
        CurrentIssueElement element = new CurrentIssueElement();
        element.getLotteryid().setTagValue("118");
        String xml = message.getXml(element);
        Log.i(TAG, xml);
        System.out.println(xml);
    }

    public void testNetType() {
        NetUtil.checkNet(getContext());
    }

    public void testUserLogin() {
//        UserEngineImpl userEngine = new UserEngineImpl();
//        User user;
//        user = new User();
//        user.setUsername("1320000000");
//        user.setPassword("000000");
//        Message login = userEngine.login(user);
//
//        Log.i(TAG, login.getBody().getOelement().getErrormsg());

        UserEngine userEngine = BeanFactory.getImpl(UserEngine.class);
        User user;
        user = new User();
        user.setUsername("1320000000");
        user.setPassword("000000");
        Message login = userEngine.login(user);

        Log.i(TAG, login.getBody().getOelement().getErrormsg());
    }
}