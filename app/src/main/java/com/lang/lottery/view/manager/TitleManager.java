package com.lang.lottery.view.manager;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lang.lottery.ConstantValue;
import com.lang.lottery.R;
import com.lang.lottery.view.SecondUI;

import org.apache.commons.lang3.StringUtils;

import java.util.Observable;
import java.util.Observer;

/**
 * 管理标题容器的工具
 * Created by Lang on 2015/5/15.
 */
public class TitleManager implements Observer {

    //显示和隐藏

    private static TitleManager instance = new TitleManager();

    public static TitleManager getInstance() {
        return instance;
    }

    private RelativeLayout commonContainer;
    private RelativeLayout loginContainer;
    private RelativeLayout unLoginContainer;

    private ImageView goback;   //返回
    private ImageView help;     //帮助
    private ImageView login;    //登陆

    private TextView titleContent;  //标题内容
    private TextView userInfo;      //用户信息

    public void init(Activity activity) {
        commonContainer = (RelativeLayout) activity.findViewById(R.id.ll_common_container);
        loginContainer = (RelativeLayout) activity.findViewById(R.id.ll_login_container);
        unLoginContainer = (RelativeLayout) activity.findViewById(R.id.ll_unlogin_container);

        goback = (ImageView) activity.findViewById(R.id.ll_title_goback);
        help = (ImageView) activity.findViewById(R.id.ll_title_help);
        login = (ImageView) activity.findViewById(R.id.ll_title_login);

        titleContent = (TextView) activity.findViewById(R.id.ll_title_content);
        userInfo = (TextView) activity.findViewById(R.id.ll_top_user_info);

        setListener();
    }

    private void setListener() {
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("点击返回键");
            }
        });
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("点击帮助键");

            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("点击登陆键");
                //MiddleManager.getInstance().changeUI(new SecondUI(MiddleManager.getInstance().getContext()));
                //changeUI需要修改，不能传递对象，但是明确目标
                MiddleManager.getInstance().changeUI(SecondUI.class);
            }
        });
    }

    private void initTitle() {
        commonContainer.setVisibility(View.GONE);
        loginContainer.setVisibility(View.GONE);
        unLoginContainer.setVisibility(View.GONE);
    }

    /**
     * 显示通用标题
     */
    public void showCommonTitle() {

        initTitle();
        commonContainer.setVisibility(View.VISIBLE);
    }

    /**
     * 显示未登录的标题
     */
    public void showUnLoginTitle() {
        initTitle();
        unLoginContainer.setVisibility(View.VISIBLE);
    }

    /**
     * 显示登陆的标题
     */
    public void showLoginTitle() {
        initTitle();
        loginContainer.setVisibility(View.VISIBLE);
    }

    public void changeTitle(String title) {
        titleContent.setText(title);
    }

    @Override
    public void update(Observable observable, Object data) {
        if (data != null && StringUtils.isNumeric(data.toString())) {
            int id = Integer.parseInt(data.toString());
            switch (id) {
                case ConstantValue.VIEW_FIRST:
                case ConstantValue.VIEW_HALL:
                    showUnLoginTitle();
                    break;
                case ConstantValue.VIEW_SECOND:
                case ConstantValue.VIEW_SSQ:
                    showCommonTitle();
                    break;

            }
        }
    }
}
