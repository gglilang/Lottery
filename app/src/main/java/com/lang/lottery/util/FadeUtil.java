package com.lang.lottery.util;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

/**
 * 淡入淡出的切换
 * Created by Lang on 2015/5/15.
 */
public class FadeUtil {
    //当前正在展示的淡出，动画的执行时间
    //在这个执行过程中，第二界面处于等待状态
    //第二个界面淡入，动画的执行时间

    /**
     * 淡出
     * @param view 执行动画的界面
     * @param duration 执行的时间
     */
    public static void fadeOut(final View view, long duration){
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        alphaAnimation.setDuration(duration);

        //动画执行完成之后，做删除view的操作
        //增加动画执行完成之后的监听
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ViewGroup parent = (ViewGroup) view.getParent();
                parent.removeView(view);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        view.startAnimation(alphaAnimation);
    }

    /**
     * 淡入
     * @param view 执行动画的界面
     * @param delay 等待时间（淡出的界面执行动画的时间相同）
     * @param duration 执行的时间
     */
    public static void fadeIn(View view, long delay, long duration){
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(duration);
        alphaAnimation.setStartOffset(delay);
        view.startAnimation(alphaAnimation);
    }
}
