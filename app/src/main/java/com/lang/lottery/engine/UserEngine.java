package com.lang.lottery.engine;

import com.lang.lottery.bean.User;
import com.lang.lottery.net.protocal.Message;

/**
 * 业务操作的接口
 * Created by Lang on 2015/5/14.
 */
public interface UserEngine {

    /**
     * 用户登录
     * @param user
     * @return
     */
    Message login(User user);

    /**
     * 获取用户余额
     * @param user
     * @return
     */
    Message getBalance(User user);

    Message bet(User user);
}
