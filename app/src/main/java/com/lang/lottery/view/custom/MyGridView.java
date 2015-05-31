package com.lang.lottery.view.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lang.lottery.R;
import com.lang.lottery.util.DensityUtil;

/**
 * Created by Lang on 2015/5/22.
 */
public class MyGridView extends GridView {

    private PopupWindow pop;
    private TextView ball;

    private OnActionUpListener onActionUpListener;

    public OnActionUpListener getOnActionUpListener() {
        return onActionUpListener;
    }

    public void setOnActionUpListener(OnActionUpListener onActionUpListener) {
        this.onActionUpListener = onActionUpListener;
    }

    public MyGridView(Context context) {
        super(context);
    }

    public MyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);

        View view = View.inflate(context, R.layout.ll_gridview_item_pop, null);
        ball = (TextView) view.findViewById(R.id.ll_preTextView);

        pop = new PopupWindow(context);
        pop.setContentView(view);

        //设置pop的大小
        pop.setWidth(DensityUtil.dip2px(context, 55));
        pop.setHeight(DensityUtil.dip2px(context, 53));
        //设置背景透明
        pop.setBackgroundDrawable(null);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // 当手指按下的时候，获取到点击那个球

        int x = (int) ev.getX();
        int y = (int) ev.getY();

        int position = pointToPosition(x, y);
        System.out.println(position + "---------");
        if(position == INVALID_POSITION){
            hiddenPop();
            return false;
        }

        TextView child = (TextView) this.getChildAt(position);

        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                // 当手指按下的时候，接管ScrollView滑动
                this.getParent().getParent().requestDisallowInterceptTouchEvent(true);
                showPop(child);
                break;
            case MotionEvent.ACTION_MOVE:
                updatePop(child);
                break;
            case MotionEvent.ACTION_UP:
                // 当手指离开的时候，放行ScrollView滑动
                hiddenPop();
                // 增加一个监听
                this.getParent().getParent().requestDisallowInterceptTouchEvent(false);

                onActionUpListener.OnActionUp(child, position);
                break;
            default:
                hiddenPop();
                break;
        }
        return super.onTouchEvent(ev);
    }

    private void showPop(TextView child){
        int xOffset = -(pop.getWidth() - child.getWidth()) / 2;
        int yOffset = -(pop.getHeight() + child.getHeight());
        ball.setText(child.getText());
        pop.showAsDropDown(child, xOffset, yOffset);
    }

    private void hiddenPop(){
        pop.dismiss();
    }

    private void updatePop(TextView child){

        int xOffset = -(pop.getWidth() - child.getWidth()) / 2;
        int yOffset = -(pop.getHeight() + child.getHeight());
        ball.setText(child.getText());
        pop.update(child, xOffset, yOffset, -1, -1);
    }

    public interface OnActionUpListener{
        /**
         * 手指抬起
         * @param view 当前手底下的球
         * @param position 位置
         */
        void OnActionUp(View view, int position);
    }
}
