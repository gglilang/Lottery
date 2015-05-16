package com.lang.lottery.view;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lang.lottery.ConstantValue;
import com.lang.lottery.view.manager.BaseUI;

/**
 * Created by Lang on 2015/5/15.
 */
public class SecondUI extends BaseUI{

    TextView textView;

    public SecondUI(Context context) {
        super(context);
        init();
    }

    public View getChild(){
        return textView;
    }

    /**
     * 初始化：调用一次
     */
    private void init() {
        textView = new TextView(context);
        ViewGroup.LayoutParams layoutParams = textView.getLayoutParams();
        layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        textView.setLayoutParams(layoutParams);
        textView.setBackgroundColor(Color.RED);
        textView.setText("这是第二个界面");
    }

    @Override
    public int getID() {
        return ConstantValue.VIEW_SECOND;
    }
}
