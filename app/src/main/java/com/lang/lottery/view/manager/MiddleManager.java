package com.lang.lottery.view.manager;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.lang.lottery.R;
import com.lang.lottery.view.BaseUI;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Observable;

/**
 * Created by Lang on 2015/5/15.
 */
public class MiddleManager extends Observable {

    private static final String TAG = "MiddleManager";
    private static MiddleManager instance = new MiddleManager();

    public static MiddleManager getInstance() {
        return instance;
    }

    private RelativeLayout middle;

    public void setMiddle(RelativeLayout middle) {
        this.middle = middle;
    }

    //利用手机内存控件，换应用程序的运行速度
    private Map<String, BaseUI> VIEWCACHE = new HashMap<>();    //K：唯一的标示BaseUI的子类

    private BaseUI currentUI;   //当前正在展示界面

    /**
     * 模拟栈记录用户操作界面的历史
     */
    private LinkedList<String> HISTORY = new LinkedList<>();

    public BaseUI getCurrentUI() {
        return currentUI;
    }

    //构造函数
    public void changeUI(Class<? extends BaseUI> targetClazz) {
        changeUI(targetClazz, null);
    }

    /**
     * 切换界面：解决问题“三个容器的联动”
     *
     * @param targetClazz 目标对象的字节码
     * @param bundle
     */
    public void changeUI(Class<? extends BaseUI> targetClazz, Bundle bundle) {

        //判断：当前正在展示的界面和切换目标界面是否相同
        if (currentUI != null && currentUI.getClass() == targetClazz) {
            return;
        }

        BaseUI targetUI = null;
        //一旦建过，重用
        //判断是否创建了--曾经创建过的界面需要存储
        String key = targetClazz.getSimpleName();
        if (VIEWCACHE.containsKey(key)) {
            //创建了，重用
            targetUI = VIEWCACHE.get(key);
        } else {
            //否则，创建
            try {
                Constructor<? extends BaseUI> constructor = targetClazz.getConstructor(Context.class);
                targetUI = constructor.newInstance(getContext());
                VIEWCACHE.put(key, targetUI);
            } catch (Exception e) {
                throw new RuntimeException("constructor new instance error");
            }
        }

        if(targetUI != null && bundle != null){
            targetUI.setBundle(bundle);
        }

        Log.i(TAG, targetUI.toString());

        // 在清理当前正在展示的界面之前--onPause方法
        if(currentUI != null)
        currentUI.onPause();

        middle.removeAllViews();
        View child = targetUI.getChild();
        middle.addView(child);
        child.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.la_view_change));

        // 在加载完界面之后-onResume
        targetUI.onResume();
        currentUI = targetUI;

        //将当前显示的界面放到栈顶
        HISTORY.addFirst(key);

        // 当中间容器切换成功时，处理另外的两个容器的变化
        changeTitleAndBottom();
    }

    private void changeTitleAndBottom() {
        // 1、界面一对应未登录标题和通用导航
        // 2、界面二对应通用标题和玩法导航

        // 当前正在展示的如果是第一个界面
        // 方案一：存在问题，对比的依据：名称 或者 字节码
        // 在界面处理初期，将所有的界面名称确定
        // 如果是字节码，将所有的界面都创建完成
//        if(currentUI.getClass() == FirstUI.class){
//            TitleManager.getInstance().showUnLoginTitle();
//            BottomManager.getInstance().showCommonBottom();
//        }
//        if(currentUI.getClass().getSimpleName().equals("SecondUI")){
//            TitleManager.getInstance().showLoginTitle();
//            BottomManager.getInstance().showGameBottom();
//        }

//        //方案二：更换对比依据
//        switch (currentUI.getID()){
//            case ConstantValue.VIEW_FIRST:
//                TitleManager.getInstance().showUnLoginTitle();
//            BottomManager.getInstance().showCommonBottom();
//                break;
//            case ConstantValue.VIEW_SECOND:
//                TitleManager.getInstance().showLoginTitle();
//                BottomManager.getInstance().showGameBottom();
//                break;
//        }

        // 降低三个容器的耦合度
        // 当中间容器变动的时候，中间容器“通知”其他的容器，你们该变动了，位移的标示传递，其他容器依据唯一标示进行容器内容的切换
        // 通知：
        // 广播：多个应用
        // 为中间容器的变动增加了监听--观察者设计模式

        // 1、将中间容器变成被观察者的对象
        // 2、标题和底部导航变成观察者
        // 3、建立观察者和被观察者之间的关系（标题和底部导航添加到观察者的容器里面）
        // 4、一旦中间容器变动，修改boolean，然后通知所有的观察者.update();
        setChanged();
        notifyObservers(currentUI.getID());
    }

    /**
     * 切换界面：解决问题“中间容器中，每次切换没有判断当前正在展示和需要切换的目标是不是同一个”
     *
     * @param targetClazz 目标对象的字节码
     */
    public void changeUI3(Class<? extends BaseUI> targetClazz) {

        //判断：当前正在展示的界面和切换目标界面是否相同
        if (currentUI != null && currentUI.getClass() == targetClazz) {
            return;
        }

        BaseUI targetUI = null;
        //一旦建过，重用
        //判断是否创建了--曾经创建过的界面需要存储
        String key = targetClazz.getSimpleName();
        if (VIEWCACHE.containsKey(key)) {
            //创建了，重用
            targetUI = VIEWCACHE.get(key);
        } else {
            //否则，创建
            try {
                Constructor<? extends BaseUI> constructor = targetClazz.getConstructor(Context.class);
                targetUI = constructor.newInstance(getContext());
                VIEWCACHE.put(key, targetUI);
            } catch (Exception e) {
                throw new RuntimeException("constructor new instance error");
            }
        }
        Log.i(TAG, targetUI.toString());

        middle.removeAllViews();
        View child = targetUI.getChild();
        middle.addView(child);
        child.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.la_view_change));
        currentUI = targetUI;

        //将当前显示的界面放到栈顶
        HISTORY.addFirst(key);
    }

    /**
     * 切换界面：解决问题“在标题容器中每次点击都创建一个目标界面”
     *
     * @param targetClazz 目标对象的字节码
     */
    public void changeUI2(Class<? extends BaseUI> targetClazz) {
        BaseUI targetUI = null;
        //一旦建过，重用
        //判断是否创建了--曾经创建过的界面需要存储
        String key = targetClazz.getSimpleName();
        if (VIEWCACHE.containsKey(key)) {
            //创建了，重用
            targetUI = VIEWCACHE.get(key);
        } else {
            //否则，创建
            try {
                Constructor<? extends BaseUI> constructor = targetClazz.getConstructor(Context.class);
                targetUI = constructor.newInstance(getContext());
                VIEWCACHE.put(key, targetUI);
            } catch (Exception e) {
                throw new RuntimeException("constructor new instance error");
            }
        }
        Log.i(TAG, targetUI.toString());

        middle.removeAllViews();
        View child = targetUI.getChild();
        middle.addView(child);
        child.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.la_view_change));
    }

    /**
     * 切换界面
     *
     * @param ui
     */
    public void changeUI1(BaseUI ui) {
        middle.removeAllViews();
        View child = ui.getChild();
        middle.addView(child);
        child.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.la_view_change));
    }

    public Context getContext() {
        return middle.getContext();
    }

    public boolean goBack() {
        // 记录一下用户操作历史
        // 频繁操作栈顶（添加）--在界面切换成功
        // 获取栈顶
        // 删除了栈顶
        // 有序集合

        if (HISTORY.size() > 1) {
            HISTORY.removeFirst();
            String key = HISTORY.getFirst();
            BaseUI currentTarget = VIEWCACHE.get(key);

            currentUI.onPause();

            middle.removeAllViews();
            middle.addView(currentTarget.getChild());

            currentTarget.onResume();

            currentUI = currentTarget;
            changeTitleAndBottom();
            return true;
        }
        return false;
    }
}
