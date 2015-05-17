package com.lang.lottery.view;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.lang.lottery.R;

/**
 * 所有界面的基类
 * Created by Lang on 2015/5/15.
 */
public abstract class BaseUI implements View.OnClickListener {
    protected Context context;

    protected ViewGroup showInMiddle;

    public BaseUI(Context context) {
        this.context = context;
        init();

        setListener();
    }

    /**
     * 设置监听
     */
    protected abstract void setListener();

    /**
     * 界面的初始化
     */
    protected abstract void init();

    /**
     * 获取需要在中间容器加载的内容
     *
     * @return
     */
    public View getChild() {
        // 设置layout参数
        // if(root==null) showInMiddle.getLayoutParams() == null
        // if(root != null) return root;

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        if(showInMiddle.getLayoutParams() == null){
            showInMiddle.setLayoutParams(params);
        }
        return showInMiddle;
    }

    /**
     * 获取每个界面的标示--容器联动时的对比依据
     *
     * @return
     */
    public abstract int getID();

    public View findViewById(int id) {
        return showInMiddle.findViewById(id);
    }

    @Override
    public void onClick(View v) {

        Log.i("test", "test");
    }
}
