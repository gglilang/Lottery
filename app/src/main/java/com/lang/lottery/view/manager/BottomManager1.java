package com.lang.lottery.view.manager;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lang.lottery.R;


/**
 * Created by Lang on 2015/5/15.
 */
public class BottomManager1 {

    private static final String TAG = "BottomManager";
    /******************第一步：管理对象的创建（单例模式）*****************/
    //创建一个静态实例
    private static BottomManager1 instrance;

    //构造私有
    public BottomManager1() {
    }

    //提供统一的对外获取实例的入口
    public static BottomManager1 getInstrance() {
        if(instrance == null){
            instrance = new BottomManager1();
        }
        return instrance;
    }

    /****************第二步：初始化各个导航容器及相关控件设置监听****************/

    //底部菜单容器
    private RelativeLayout bottomMenuContainer;

    //底部导航
    private LinearLayout commonBottom;  //购彩通用导航
    private LinearLayout playBottom;    //购彩

    /***************导航按钮*************/

    //购彩导航底部按钮及提示信息
    private ImageButton cleanButton;
    private ImageButton addButton;

    private TextView playBottomNotice;

    /*****************通用导航底部按钮*****************/
    private ImageButton homeButton;
    private ImageButton hallButton;
    private ImageButton rechargeButton;
    private ImageButton myselfButton;

    public void init(Activity activity){
        bottomMenuContainer = (RelativeLayout) activity.findViewById(R.id.ll_bottom);
        commonBottom = (LinearLayout) activity.findViewById(R.id.ll_bottom_common);
        playBottom = (LinearLayout) activity.findViewById(R.id.ll_bottom_game);

        playBottomNotice = (TextView) activity.findViewById(R.id.ll_bottom_game_choose_notice);
        cleanButton = (ImageButton) activity.findViewById(R.id.ll_bottom_game_choose_clean);
        addButton = (ImageButton) activity.findViewById(R.id.ll_bottom_game_choose_ok);

        //设置监听
        setListener();
    }

    /**
     * 设置监听
     */
    private void setListener() {
        //清空按钮
        cleanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "点击清空按钮");
            }
        });


        //选好按钮
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "点击选好按钮");
            }
        });
    }


    /*************************第三步：控制各个导航容器的显示和隐藏***************/

    /**
     * 转换到通用导航
     */
    public void showCommonBottom(){
        if(bottomMenuContainer.getVisibility() == View.GONE || bottomMenuContainer.getVisibility() == View.INVISIBLE){
            bottomMenuContainer.setVisibility(View.VISIBLE);
        }
        commonBottom.setVisibility(View.VISIBLE);
        playBottom.setVisibility(View.INVISIBLE);
    }

    /**
     * 转换到购彩
     */
    public void showGameBottom(){
        if(bottomMenuContainer.getVisibility() == View.GONE || bottomMenuContainer.getVisibility() == View.INVISIBLE){
            bottomMenuContainer.setVisibility(View.VISIBLE);
        }
        commonBottom.setVisibility(View.INVISIBLE);
        playBottom.setVisibility(View.VISIBLE);
    }

    /**
     * 改变底部导航容器显示情况
     * @param type
     */
    public void changeBottomVisibility(int type){
        if(bottomMenuContainer.getVisibility() != type){
            bottomMenuContainer.setVisibility(type);
        }
    }

    /***********************第四步：控制玩法导航内容显示******************/

    public void changeGameBottomNotice(String notice){
        playBottomNotice.setText(notice);
    }


}
