package com.lang.lottery.view;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lang.lottery.ConstantValue;
import com.lang.lottery.R;

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
    }


    @Override
    public int getID() {
        return ConstantValue.VIEW_HALL;
    }
}
