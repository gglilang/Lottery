package com.lang.lottery.view;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lang.lottery.ConstantValue;

/**
 * Created by Lang on 2015/5/15.
 */
public class FirstUI extends BaseUI {



    public FirstUI(Context context) {
        super(context);

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void init() {

    }

    public View getChild(){
        TextView textView = new TextView(context);
        ViewGroup.LayoutParams layoutParams = textView.getLayoutParams();
        layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        textView.setLayoutParams(layoutParams);
        textView.setBackgroundColor(Color.BLUE);
        textView.setText("这是第一个界面");
        return textView;
    }

    @Override
    public int getID() {
        return ConstantValue.VIEW_FIRST;
    }
}
