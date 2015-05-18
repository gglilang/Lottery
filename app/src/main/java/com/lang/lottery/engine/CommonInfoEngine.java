package com.lang.lottery.engine;

import com.lang.lottery.net.protocal.Message;

/**
 * 公共数据处理
 * Created by Lang on 2015/5/17.
 */
public interface CommonInfoEngine {
    /**
     * 获取当前销售期信息
     * @param param 彩种的标示
     * @return
     */
    Message getCurrentIssueInfo(Integer param);
}
