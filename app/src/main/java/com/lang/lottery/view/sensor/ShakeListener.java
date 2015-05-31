package com.lang.lottery.view.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Vibrator;

/**
 * 处理传感器监听
 * Created by Lang on 2015/5/31.
 */
public abstract class ShakeListener implements SensorEventListener {

    private Context context;
    private Vibrator vibrator;

    public ShakeListener(Context context) {
        this.context = context;
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    private float lastX;
    private float lastY;
    private float lastZ;
    private long lastTime;

    private long duration = 100;
    private float total;    // 累加的值
    private float switchValue = 200;    // 判断手机摇晃的阈值


    @Override
    public void onSensorChanged(SensorEvent event) {

        // 判断：是否是第一个点
        if(lastTime == 0){

            lastX = event.values[0];
            lastY = event.values[1];
            lastZ = event.values[2];

            lastTime = System.currentTimeMillis();

        } else {

            long currentTime = System.currentTimeMillis();
            // 尽可能屏蔽掉不同手机差异
            if((currentTime - lastTime) >= duration){
                // 第二点及以后
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                float dx = Math.abs(x - lastX);
                float dy = Math.abs(y - lastY);
                float dz = Math.abs(z - lastZ);

                // 屏蔽掉微小的增量（手机长时间的轻微晃动）
                if(dx < 1){
                    dx = 0;
                }
                if(dy < 1){
                    dy = 0;
                }
                if(dz < 1){
                    dz = 0;
                }

                // 两点之间的总增量
                float shake = dx + dy + dz;

                if(shake == 0){
                    init();
                }

                total += shake;

                if(total >= switchValue){
                    // 摇晃手机处理
                    // 机选一注彩票
                    randomCure();
                    // 提示用户（声音或震动）
                    vibrator.vibrate(100);
                    // 所有的数据都要初始化
                    init();
                }else{
                    lastX = event.values[0];
                    lastY = event.values[1];
                    lastZ = event.values[2];

                    lastTime = System.currentTimeMillis();
                }
            }
        }
    }

    public abstract void randomCure();

    private void init() {
        lastX = 0;
        lastY = 0;
        lastZ = 0;
        lastTime = 0;

        total = 0;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
