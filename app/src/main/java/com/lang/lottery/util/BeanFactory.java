package com.lang.lottery.util;

import android.content.Context;

import com.lang.lottery.engine.UserEngine;

import java.io.IOException;
import java.util.Properties;

/**
 * 工厂类
 * Created by Lang on 2015/5/14.
 */
public class BeanFactory {
    //依据配置文件加载实例

    private static Properties properties;
    static {
        properties = new Properties();
        //bean.properties必须在src的根目录下
        try {

            properties.load(BeanFactory.class.getResourceAsStream("/assets/bean.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载需要的实现类
     * @param clazz
     * @return
     */
    public static UserEngine getImpl(Class clazz){
        String key = clazz.getSimpleName();
        String className = properties.getProperty(key);
        try {
            return (UserEngine) Class.forName(className).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
