package com.lang.lottery.view;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lang.lottery.ConstantValue;
import com.lang.lottery.R;
import com.lang.lottery.engine.CommonInfoEngine;
import com.lang.lottery.net.NetUtil;
import com.lang.lottery.net.protocal.Element;
import com.lang.lottery.net.protocal.Message;
import com.lang.lottery.net.protocal.Oelement;
import com.lang.lottery.net.protocal.element.CurrentIssueElement;
import com.lang.lottery.util.BeanFactory;
import com.lang.lottery.util.LogUtil;
import com.lang.lottery.util.PromptManager;

import org.apache.commons.lang3.StringUtils;

/**
 * 购彩大厅
 * Created by Lang on 2015/5/16.
 */
public class Hall extends BaseUI{

    // 第一步：加载layout（布局参数设置）
    // 第二步：初始化layout中控件
    // 第三步：设置监听




    private TextView ssqIssue;
    private ImageView ssqBet;


    public Hall(Context context) {
        super(context);

    }

    public void setListener() {
        ssqBet.setOnClickListener(this);
    }

    public void init() {
        showInMiddle = (LinearLayout) View.inflate(context, R.layout.ll_hall, null);

        ssqIssue = (TextView) findViewById(R.id.ll_hall_ssq_summary);
        ssqBet = (ImageView) findViewById(R.id.ll_hall_ssq_bet);
        LogUtil.info(Hall.class, "wocao" );
        Log.i("Hall", "lsfdjslfjslkfj");

        getCurrentIssueInfo();
    }


    @Override
    public int getID() {
        return ConstantValue.VIEW_HALL;
    }

    /**
     * 获取当前销售期信息
     */
    private void getCurrentIssueInfo(){

        new MyHttpTask<Integer>(){

            @Override
            protected Message doInBackground(Integer... params) {
                //获得数据--业务的调用
                CommonInfoEngine engine = BeanFactory.getImpl(CommonInfoEngine.class);


                assert engine != null;
                return engine.getCurrentIssueInfo(params[0]);

            }

            @Override
            protected void onPostExecute(Message message) {
                Log.i("Hall", "dddsf");
                //更新界面
                if(message != null){
                    Oelement oelement = message.getBody().getOelement();
                    if(ConstantValue.SUCCESS.equals(oelement.getErrorcode())){

                        changeNotice(message.getBody().getElements().get(0));
                    } else {
                        PromptManager.showToast(context, oelement.getErrormsg());
                    }
                }else{
                    //可能：网络不通、权限、服务器出错、非法数据
                    //如何提示用户
                    PromptManager.showToast(context, "服务器繁忙，请稍后再试。。。");
                }
                super.onPostExecute(message);
            }
        }.executeProxy(ConstantValue.SSQ);
    }

    /**
     * 修改界面提示信息
     * @param element
     */
    private void changeNotice(Element element) {
        CurrentIssueElement currentIssueElement = (CurrentIssueElement) element;
        //第ISSUE期 还有TIME停售
        String text = context.getResources().getString(R.string.is_hall_common_summary);
        String issue = currentIssueElement.getIssue();
        String time = getLasttime(currentIssueElement.getIssue());
        text = StringUtils.replaceEach(text, new String[]{"ISSUE", "TIME"}, new String[]{issue, time});

        LogUtil.info(Hall.class, "wori" + text);
        ssqIssue.setText(text);
    }

    /**
     * 将时间转换成日时分格式
     * @param lasttime
     * @return
     */
    public String getLasttime(String lasttime){
        StringBuffer result = new StringBuffer();
        if(StringUtils.isNumericSpace(lasttime)){
            int time = Integer.parseInt(lasttime);
            int day = time / (24 * 60 * 60);
            result.append(day).append("天");
            if(day > 0){
                time = time - day * 24 * 60 *60;
            }
            int hour = time / 3600;
            result.append(hour).append("时");
            if(hour > 0){
                time = time - hour * 3600;
            }
            int minute = time / 60;
            result.append(minute).append("分");
        }
        return result.toString();
    }

//    /**
//     * 异步访问网络的工具
//     * @param <Params>  传输的参数
//     * Void：下载相关的进度提示
//     * Message:服务器回复数据的封装
//     */
//    private class MyAsyncTask<Params> extends AsyncTask<Params, Void, Message> {
//
//        /**
//         * 同run方法，在子线程中运行
//         * @param params
//         * @return
//         */
//        @Override
//        protected Message doInBackground(Params... params) {
//            return null;
//        }
//
//        /**
//         * 显示滚动条
//         */
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        /**
//         * 修改界面提示信息
//         * @param message
//         */
//        @Override
//        protected void onPostExecute(Message message) {
//            super.onPostExecute(message);
//        }
//
//        /**
//         * 类似与Thread.start方法
//         * 由于final修饰，无法Override
//         * 省略掉网络判断
//         * @param params
//         * @return
//         */
//        public final AsyncTask<Params, Void, Message> executeProxy(Params... params) {
//            if(NetUtil.checkNet(context)) {
//                return super.execute(params);
//            } else{
//                PromptManager.showNoNetWork(context);
//            }
//            return null;
//        }
//    }
}
