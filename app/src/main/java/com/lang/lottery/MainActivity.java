package com.lang.lottery;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.lang.lottery.util.FadeUtil;
import com.lang.lottery.util.PromptManager;
import com.lang.lottery.view.FirstUI;
import com.lang.lottery.view.Hall;
import com.lang.lottery.view.SecondUI;
import com.lang.lottery.view.BaseUI;
import com.lang.lottery.view.manager.BottomManager;
import com.lang.lottery.view.manager.MiddleManager;
import com.lang.lottery.view.manager.TitleManager;


public class MainActivity extends Activity {

    private RelativeLayout middle;  //中间占位置的容器

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            changeUI(new SecondUI(MainActivity.this));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        TitleManager manager = TitleManager.getInstance();
        manager.init(this);
        manager.showUnLoginTitle();

        BottomManager.getInstance().init(this);
        BottomManager.getInstance().changeBottomVisibility(View.VISIBLE);
        middle = (RelativeLayout) findViewById(R.id.ll_middle);

        MiddleManager.getInstance().setMiddle(middle);


        //建立观察者和被观察者之间的关系（标题和底部导航添加到观察者的容器里面）
        MiddleManager.getInstance().addObserver(TitleManager.getInstance());
        MiddleManager.getInstance().addObserver(BottomManager.getInstance());

        //loadFirstUI();
//        MiddleManager.getInstance().changeUI(FirstUI.class);
        MiddleManager.getInstance().changeUI(Hall.class);

        //handler.sendEmptyMessageDelayed(10, 2000);
    }

    private void loadFirstUI() {
        FirstUI firstUI = new FirstUI(this);

        View child = firstUI.getChild();
        middle.addView(child);
        //FadeUtil.fadeOut(child, 2000);
    }

    protected void loadSecondUI() {
        SecondUI secondUI = new SecondUI(this);

        View child = secondUI.getChild();
        middle.addView(child);

        //执行切换动画
        //middle.startAnimation(AnimationUtils.loadAnimation(this, R.anim.la_view_change));
        FadeUtil.fadeIn(child, 2000, 1000);
    }

    /**
     * 切换界面
     *
     * @param ui
     */
    protected void changeUI(BaseUI ui) {
        middle.removeAllViews();
        View child = ui.getChild();
        middle.addView(child);
        child.startAnimation(AnimationUtils.loadAnimation(this, R.anim.la_view_change));
    }

    /**
     * 切换界面
     */
    protected void changeUI() {
        //middle.removeAllViews();
        loadSecondUI();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                boolean result = MiddleManager.getInstance().goBack();
                if(!result){
                    PromptManager.showExitSystem(this);
                }
                return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
