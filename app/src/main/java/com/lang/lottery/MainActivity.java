package com.lang.lottery;

import android.app.Activity;
import android.os.Bundle;

import com.lang.lottery.net.NetUtil;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NetUtil.checkNet(this);
    }

}
