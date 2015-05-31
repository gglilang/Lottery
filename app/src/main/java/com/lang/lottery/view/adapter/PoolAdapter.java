package com.lang.lottery.view.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lang.lottery.R;

import java.text.DecimalFormat;
import java.util.List;

/**
 * 选号容器yongAdapter
 * Created by Lang on 2015/5/19.
 */
public class PoolAdapter extends BaseAdapter {

    Context context;

    private int endNum;

    private List<Integer> selectedNums;

    private int selectedBgResId;

    public PoolAdapter(Context context, int endNum, List<Integer> selectedNums, int selectedBgResId) {
        this.context = context;
        this.endNum = endNum;
        this.selectedNums = selectedNums;
        this.selectedBgResId = selectedBgResId;
    }

    @Override
    public int getCount() {
        return endNum;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView ball = new TextView(context);
        DecimalFormat decimalFormat = new DecimalFormat("00");
        ball.setText(decimalFormat.format(position + 1));

        // 获取到用户已选号码的集合，判断集合中中有的话，背景图片修改为已选中
        if(selectedNums.contains(position + 1)){
            ball.setBackgroundResource(selectedBgResId);
        } else {
            ball.setBackgroundResource(R.drawable.id_defalut_ball);
        }

        ball.setTextSize(16);
        ball.setGravity(Gravity.CENTER);
        return ball;
    }
}
