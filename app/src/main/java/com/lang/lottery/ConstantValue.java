package com.lang.lottery;

/**
 * Created by Lang on 2015/5/11.
 */
public interface ConstantValue {
    //class : public static final
    String ENCONDING = "UTF-8";

    /**
     * 子代理商的密钥
     */
    String AGENTER_PASSWORD = "5353535353";

    /**
     * <agenterid>889931</agenterid>
     * 代理的id
     */
    String AGENTERID = "889931";

    /**
     * <source>ivr</source>
     * 消息来源
     */
    String SOURCE = "ivr";

    /**
     * <compress>DES</compress>
     * body里面的数据加密算法
     */
    String COMPRESS = "DES";

    /**
     * des加密用密钥
     */
    String DES_PASSWORD = "9b2648fcdfbad80f";

    /**
     * 服务器地址
     */
    String LOTTERY_URI = "http://192.168.1.9:8080/ZCWService/Entrance";// 10.0.2.2模拟器如果需要跟PC机通信127.0.0.1
    // String LOTTERY_URI = "http://192.168.1.100:8080/ZCWService/Entrance";// 10.0.2.2模拟器如果需要跟PC机通信127.0.0.1
}
