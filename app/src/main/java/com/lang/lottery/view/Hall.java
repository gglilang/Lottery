package com.lang.lottery.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lang.lottery.ConstantValue;
import com.lang.lottery.GlobalParams;
import com.lang.lottery.R;
import com.lang.lottery.engine.CommonInfoEngine;
import com.lang.lottery.net.protocal.Element;
import com.lang.lottery.net.protocal.Message;
import com.lang.lottery.net.protocal.Oelement;
import com.lang.lottery.net.protocal.element.CurrentIssueElement;
import com.lang.lottery.util.BeanFactory;
import com.lang.lottery.util.PromptManager;
import com.lang.lottery.view.manager.MiddleManager;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 购彩大厅
 * Created by Lang on 2015/5/16.
 */
public class Hall extends BaseUI {

    // 第一步：加载layout（布局参数设置）
    // 第二步：初始化layout中控件
    // 第三步：设置监听

    private ListView categoryList;  // 彩种的入口

    private CategoryAdapter adapter;    //ListView的适配器

    //ViewPager配置
    private ViewPager pager;
    private PagerAdapter pagerAdapter;

    private List<View> pagers;

    private ImageView underLine;

    private TextView fcTitle;// 福彩
    private TextView tcTitle;// 体彩
    private TextView gpcTitle;// 高频彩


//    private TextView ssqIssue;
//    private ImageView ssqBet;


    public Hall(Context context) {
        super(context);

    }

    /**
     * 记录上一个界面的位置
     */
    private int lastPosition = 0;

    public void setListener() {

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                TranslateAnimation animation = new TranslateAnimation(
                        lastPosition * GlobalParams.WIN_WIDTH / 3,
                        position * GlobalParams.WIN_WIDTH / 3, 0, 0);
                animation.setDuration(300);
                animation.setFillAfter(true);
                underLine.startAnimation(animation);
                lastPosition = position;

                fcTitle.setTextColor(Color.BLACK);
                tcTitle.setTextColor(Color.BLACK);
                gpcTitle.setTextColor(Color.BLACK);

                switch (position) {
                    case 0:
                        fcTitle.setTextColor(Color.RED);
                        break;
                    case 1:
                        tcTitle.setTextColor(Color.RED);
                        break;
                    case 2:
                        gpcTitle.setTextColor(Color.RED);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void init() {
        showInMiddle = (LinearLayout) View.inflate(context, R.layout.ll_hall, null);
        categoryList = new ListView(context);

        pager = (ViewPager) findViewById(R.id.ll_viewPager);
        pagerAdapter = new MyPagerAdapter();


        //needUpdate = new ArrayList<>();

        adapter = new CategoryAdapter();
        categoryList.setAdapter(adapter);
        initPager();
        pager.setAdapter(pagerAdapter);

        //初始化选项卡的下划线
        initTabStrip();


//
//        ssqIssue = (TextView) findViewById(R.id.ll_hall_ssq_summary);
//        ssqBet = (ImageView) findViewById(R.id.ll_hall_ssq_bet);


        // 只会调用一次进
        // 每次入购彩大厅界面，获取最新的数据--已进入到某个界面，想去修改界面信息（存储）-》当进入到某个界面的时候，开启耗费资源的操作，当要离开界面，清理耗费资源
        // 进入做些工作，出去的时候还需要完成一些工作
        // onResume（当界面被加载了：add(View)  onPause(当界面被删除：removeAllView)==模仿Activity的两个方法
        // 倒计时
        //getCurrentIssueInfo();
    }

    private void initTabStrip() {
        fcTitle = (TextView) findViewById(R.id.ii_category_fc);
        tcTitle = (TextView) findViewById(R.id.ii_category_tc);
        gpcTitle = (TextView) findViewById(R.id.ii_category_gpc);

        underLine = (ImageView) findViewById(R.id.ll_category_selector);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.id_category_selector);

        int offset = (GlobalParams.WIN_WIDTH / 3 - bitmap.getWidth()) / 2;

        //设置图片初始位置
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        underLine.setImageMatrix(matrix);

        fcTitle.setTextColor(Color.RED);
    }

    private void initPager() {
        pagers = new ArrayList<>();
        pagers.add(categoryList);

        TextView item = new TextView(context);
        item.setText("体彩");
        pagers.add(item);

        item = new TextView(context);
        item.setText("高频彩");
        pagers.add(item);
    }

    @Override
    public void onResume() {
        getCurrentIssueInfo();
    }

    @Override
    public int getID() {
        return ConstantValue.VIEW_HALL;
    }

    /**
     * 获取当前销售期信息
     */
    private void getCurrentIssueInfo() {

        new MyHttpTask<Integer>() {

            @Override
            protected Message doInBackground(Integer... params) {
                //获得数据--业务的调用
                CommonInfoEngine engine = BeanFactory.getImpl(CommonInfoEngine.class);


                assert engine != null;
                return engine.getCurrentIssueInfo(params[0]);

            }

            @Override
            protected void onPostExecute(Message message) {
                Log.i("Hall", "dddsf");
                //更新界面
                if (message != null) {
                    Oelement oelement = message.getBody().getOelement();
                    if (ConstantValue.SUCCESS.equals(oelement.getErrorcode())) {

                        changeNotice(message.getBody().getElements().get(0));
                    } else {
                        PromptManager.showToast(context, oelement.getErrormsg());
                    }
                } else {
                    //可能：网络不通、权限、服务器出错、非法数据
                    //如何提示用户
                    PromptManager.showToast(context, "服务器繁忙，请稍后再试。。。");
                }
                super.onPostExecute(message);
            }
        }.executeProxy(ConstantValue.SSQ);
    }

    private String text;
    // private List<View> needUpdate;

    private Bundle ssqBundle;
    /**
     * 修改界面提示信息
     *
     * @param element
     */
    private void changeNotice(Element element) {
        CurrentIssueElement currentIssueElement = (CurrentIssueElement) element;
        //第ISSUE期 还有TIME停售
        text = context.getResources().getString(R.string.is_hall_common_summary);
        String issue = currentIssueElement.getIssue();
        String time = getLasttime(currentIssueElement.getIssue());
        text = StringUtils.replaceEach(text, new String[]{"ISSUE", "TIME"}, new String[]{issue, time});

        // 更新界面
        // 方式一：
        //adapter.notifyDataSetChanged(); //所有的item更新

        // 方式二：更新需要更新的内容（没必要刷新所有的信息）
        // 获取到需要更新控件的应用
//        TextView view = (TextView) needUpdate.get(0);
//        view.setText(text);

        // 方式三：不想维护needUpdate，如何获取需要更新的控件的引用
        // 将所有的item添加到ListView，是不是有方式可以获取ListView的啊孩子
        TextView view = (TextView) categoryList.findViewWithTag(0);
        if (view != null) {
            view.setText(text);
        }

        //设置需要传输的数据
        ssqBundle = new Bundle();
        ssqBundle.putString("issue", issue);
    }

    /**
     * 将时间转换成日时分格式
     *
     * @param lasttime
     * @return
     */
    public String getLasttime(String lasttime) {
        StringBuffer result = new StringBuffer();
        if (StringUtils.isNumericSpace(lasttime)) {
            int time = Integer.parseInt(lasttime);
            int day = time / (24 * 60 * 60);
            result.append(day).append("天");
            if (day > 0) {
                time = time - day * 24 * 60 * 60;
            }
            int hour = time / 3600;
            result.append(hour).append("时");
            if (hour > 0) {
                time = time - hour * 3600;
            }
            int minute = time / 60;
            result.append(minute).append("分");
        }
        return result.toString();
    }

    // 资源信息
    private int[] logoResIds = new int[]{
            R.drawable.id_ssq, R.drawable.id_3d, R.drawable.id_qlc
    };
    private int[] titleResIds = new int[]{
            R.string.is_hall_ssq_title, R.string.is_hall_3d_title, R.string.is_hall_qlc_title
    };


    private class CategoryAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 3;
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView != null) {
                holder = (ViewHolder) convertView.getTag();
            } else {
                holder = new ViewHolder();
                convertView = View.inflate(context, R.layout.il_hall_lottery_item, null);


                holder.logo = (ImageView) convertView.findViewById(R.id.ii_hall_lottery_logo);
                holder.title = (TextView) convertView.findViewById(R.id.ii_hall_lottery_title);
                holder.summary = (TextView) convertView.findViewById(R.id.ii_hall_lottery_summary);
                //needUpdate.add(holder.summary);
                holder.bet = (ImageView) convertView.findViewById(R.id.ii_hall_lottery_bet);


                convertView.setTag(holder);
            }

            holder.logo.setImageResource(logoResIds[position]);
            holder.title.setText(titleResIds[position]);

            holder.summary.setTag(position);

            holder.bet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(position == 0) {
                        MiddleManager.getInstance().changeUI(PlaySSQ.class, ssqBundle);
                    }
                }
            });

//            if(!TextUtils.isEmpty(text) && position == 0) {
//                holder.summary.setText(text);
//            }
            return convertView;
        }

        // 依据item的layout
        class ViewHolder {
            ImageView logo;
            TextView title;
            TextView summary;
            ImageView bet;
        }
    }

    /**
     * ViewPager用Adapter
     */
    private class MyPagerAdapter extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = pagers.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = pagers.get(position);
            container.removeView(view);
        }

        @Override
        public int getCount() {
            return pagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

//    /**
//     * 异步访问网络的工具
//     * @param <Params>  传输的参数
//     * Void：下载相关的进度提示
//     * Message:服务器回复数据的封装
//     */
//    private class MyAsyncTask<Params> extends AsyncTask<Params, Void, Message> {
//
//        /**
//         * 同run方法，在子线程中运行
//         * @param params
//         * @return
//         */
//        @Override
//        protected Message doInBackground(Params... params) {
//            return null;
//        }
//
//        /**
//         * 显示滚动条
//         */
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        /**
//         * 修改界面提示信息
//         * @param message
//         */
//        @Override
//        protected void onPostExecute(Message message) {
//            super.onPostExecute(message);
//        }
//
//        /**
//         * 类似与Thread.start方法
//         * 由于final修饰，无法Override
//         * 省略掉网络判断
//         * @param params
//         * @return
//         */
//        public final AsyncTask<Params, Void, Message> executeProxy(Params... params) {
//            if(NetUtil.checkNet(context)) {
//                return super.execute(params);
//            } else{
//                PromptManager.showNoNetWork(context);
//            }
//            return null;
//        }
//    }
}
