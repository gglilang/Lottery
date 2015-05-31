package com.lang.lottery.view;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.lang.lottery.ConstantValue;
import com.lang.lottery.R;
import com.lang.lottery.view.adapter.PoolAdapter;
import com.lang.lottery.view.custom.MyGridView;
import com.lang.lottery.view.manager.BottomManager;
import com.lang.lottery.view.manager.TitleManager;
import com.lang.lottery.view.sensor.ShakeListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 双色球选号界面
 * Created by Lang on 2015/5/19.
 */
public class PlaySSQ extends BaseUI {

    // 标题
    // 判断购彩大厅是否获取到期次信息
    // 如果获取到，拼装标题
    // 否则默认的标题展示
    // 填充选号容器
    // 选号：单击 + 机选红篮球
    // 手机摇晃处理
    // 提示信息+清空+选好了

    // 机选
    private Button randomRed;
    private Button randomBlue;
    // 选号容器
    private MyGridView redContainer;
    private GridView blueContainer;

    private PoolAdapter redAdapter;
    private PoolAdapter blueAdapter;

    private List<Integer> redNums;
    private List<Integer> blueNums;

    private SensorManager manager;
    private SensorEventListener listener;

    public PlaySSQ(Context context) {
        super(context);
    }


    @Override
    protected void setListener() {

        randomRed.setOnClickListener(this);
        randomBlue.setOnClickListener(this);

        redContainer.setOnActionUpListener(new MyGridView.OnActionUpListener() {
            @Override
            public void OnActionUp(View view, int position) {
                System.out.println(position);
                //判断当前点击的item是否被选中了
                if (!redNums.contains(position + 1)) {
                    // 如果没有被选中
                    // 背景图片切换到红色
                    view.setBackgroundResource(R.drawable.id_redball);
                    redNums.add(position + 1);
                } else {
                    // 被选中
                    // 还原背景图片
                    view.setBackgroundResource(R.drawable.id_defalut_ball);
                    redNums.remove((Object) (position + 1));
                }
                changeNotice();
            }
        });

//        redContainer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                // 判断当前点击的item是否被选中了
//                if (!redNums.contains(position + 1)) {
//                    // 如果没有被选中
//                    // 背景图片切换到红色
//                    view.setBackgroundResource(R.drawable.id_redball);
//                    // 摇晃的动画
//                    view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.la_ball_shake));
//                    redNums.add(position + 1);
//                } else {
//                    // 被选中
//                    // 还原背景图片
//                    view.setBackgroundResource(R.drawable.id_defalut_ball);
//                    redNums.remove((Object) (position + 1));
//                }
//            }
//        });

        blueContainer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 判断当前点击的item是否被选中了
                if (!blueNums.contains(position + 1)) {
                    // 如果没有被选中
                    // 背景图片切换到红色
                    view.setBackgroundResource(R.drawable.id_blueball);
                    // 摇晃的动画
                    view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.la_ball_shake));
                    blueNums.add(position + 1);
                } else {
                    // 被选中
                    // 还原背景图片
                    view.setBackgroundResource(R.drawable.id_defalut_ball);
                    blueNums.remove((Object) (position + 1));
                }
                changeNotice();
            }
        });
    }

    @Override
    protected void init() {
        showInMiddle = (ViewGroup) View.inflate(context, R.layout.ll_playssq, null);

        redContainer = (MyGridView) findViewById(R.id.ll_ssq_red_number_container);
        blueContainer = (GridView) findViewById(R.id.ll_ssq_blue_number_container);

        randomRed = (Button) findViewById(R.id.ll_ssq_random_red);
        randomBlue = (Button) findViewById(R.id.ll_ssq_random_blue);

        redNums = new ArrayList<>();
        blueNums = new ArrayList<>();

        redAdapter = new PoolAdapter(context, 33, redNums, R.drawable.id_redball);
        redContainer.setAdapter(redAdapter);
        blueAdapter = new PoolAdapter(context, 16, blueNums, R.drawable.id_blueball);
        blueContainer.setAdapter(blueAdapter);

        manager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    }

    @Override
    public int getID() {
        return ConstantValue.VIEW_SSQ;
    }

    @Override
    public void onClick(View v) {
        Random random = new Random();
        switch (v.getId()) {
            case R.id.ll_ssq_random_red:
                // 机选红球
                redNums.clear();    //清空已经选择的，重新选择
                while (redNums.size() < 6) {
                    int num = random.nextInt(33) + 1;
                    if (redNums.contains(num)) {
                        continue;
                    }
                    redNums.add(num);
                }

                // 处理展示
                redAdapter.notifyDataSetChanged();
                changeNotice();

                break;
            case R.id.ll_ssq_random_blue:
                // 机选篮球
                blueNums.clear();
                int num = random.nextInt(16) + 1;
                blueNums.add(num);
                // 处理展示
                blueAdapter.notifyDataSetChanged();
                changeNotice();
                break;
        }
        super.onClick(v);
    }

    @Override
    public void onResume() {
        changeTitle();
        changeNotice();

        // 注册
        listener = new ShakeListener(context) {
            @Override
            public void randomCure() {
                randomSSQ();
                changeNotice();
            }


        };
        manager.registerListener(listener, manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST);
        super.onResume();
    }

    /**
     * 机选一注
     */
    private void randomSSQ() {

        Random random = new Random();

        redNums.clear();    //清空已经选择的，重新选择
        blueNums.clear();
        // 机选红球
        while (redNums.size() < 6) {
            int num = random.nextInt(33) + 1;
            if (redNums.contains(num)) {
                continue;
            }
            redNums.add(num);
        }
        // 机选篮球

        int num = random.nextInt(16) + 1;
        blueNums.add(num);

        // 处理展示
        redAdapter.notifyDataSetChanged();
        blueAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        // 注销
        manager.unregisterListener(listener);
        super.onPause();
    }

    /**
     * 修改标题
     */
    private void changeTitle() {
        String titleInfo = "";
        // 标题--界面之间的数据传递（Bundle)
        // 判断购彩大厅是否获取到期次信息
        if (bundle != null) {
            // 如果获取到，拼装标题
            titleInfo = "双色球第" + bundle.getString("issue") + "期";
        } else {
            // 否则默认的标题展示
            titleInfo = "双色球选号";
        }
        TitleManager.getInstance().changeTitle(titleInfo);

    }

    /**
     * 改变底部导航的提示信息
     */
    private void changeNotice(){

        String notice = "";
        // 以一注为分割
        if(redNums.size() < 6){

            notice = "您还需要选择"+(6 - redNums.size())+"个红球";
        } else if(blueNums.size() == 0){
            notice = "您还需要选择1个篮球";

        } else{
            notice = "共 "+calCathectic()+" 注 "+calCathectic() * 2+" 元";
        }
        BottomManager.getInstance().changeGameBottomNotice(notice);
    }

    /**
     * 计算注数
     * @return
     */
    private int calCathectic(){
        // 计算红球的排列组合
        int redC = (int) (factorial(redNums.size()) / factorial(6) / factorial(redNums.size() - 6));
        // 计算篮球的排列组合
        int blueC = blueNums.size();
        return redC * blueC;
    }

    /**
     * 计算阶乘
     * @param num
     * @return
     */
    private long factorial(int num){
        if(num > 1){
            return num * factorial(num - 1);
        } else if(num == 1 || num ==0){
            return 1;
        } else {
            throw new IllegalArgumentException("num >= 0");
        }
    }
}
