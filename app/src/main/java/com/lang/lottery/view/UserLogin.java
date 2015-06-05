package com.lang.lottery.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.lang.lottery.ConstantValue;
import com.lang.lottery.GlobalParams;
import com.lang.lottery.R;
import com.lang.lottery.bean.User;
import com.lang.lottery.engine.UserEngine;
import com.lang.lottery.net.protocal.Message;
import com.lang.lottery.net.protocal.Oelement;
import com.lang.lottery.net.protocal.element.BalanceElement;
import com.lang.lottery.util.BeanFactory;
import com.lang.lottery.util.PromptManager;
import com.lang.lottery.view.manager.MiddleManager;


/**
 * 用户登录 + 余额获取
 * Created by Lang on 2015/5/15.
 */
public class UserLogin extends BaseUI{

    private EditText username;
    private ImageView clear;    // 清空用户名
    private EditText password;
    private Button login;

    public UserLogin(Context context) {
        super(context);
    }

    @Override
    protected void setListener() {

        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(username.getText().toString().length() > 0){
                    clear.setVisibility(View.VISIBLE);
                }
            }
        });

        clear.setOnClickListener(this);
        login.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.ll_clear:
                // 清除用户名
                username.setText("");
                clear.setVisibility(View.INVISIBLE);
                break;
            case R.id.ll_user_login:
                // 登陆
                // 用户输入信息
                if(checkUser()){
                    User user = new User();
                    user.setUsername(username.getText().toString());
                    user.setPassword(username.getText().toString());
                    new MyHttpTask<User>() {

                        @Override
                        protected void onPreExecute() {
                            PromptManager.showProgressDialog(context);
                            super.onPreExecute();
                        }

                        @Override
                        protected Message doInBackground(User... params) {
                            UserEngine engine = BeanFactory.getImpl(UserEngine.class);
                            Message login = engine.login(params[0]);
                            if(login != null){
                                System.out.println("login");
                                Oelement oelement = login.getBody().getOelement();
                                if(ConstantValue.SUCCESS.equals(oelement.getErrorcode())){
                                    // 登录成功了
                                    GlobalParams.isLogin = true;

                                    GlobalParams.USERNAME = params[0].getUsername();

                                    // 成功获取金额
                                    Message balance = engine.getBalance(params[0]);
                                    if(balance != null){
                                        System.out.println("login");
                                        oelement = balance.getBody().getOelement();
                                        if(ConstantValue.SUCCESS.equals(oelement.getErrorcode())){
                                            BalanceElement element = (BalanceElement) balance.getBody().getElements().get(0);
                                            GlobalParams.MONEY = Float.parseFloat(element.getInvestvalues());
                                            return balance;
                                        }
                                    }
                                }
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Message message) {
                            PromptManager.closeProgressDialog();
                            System.out.println(message+"message");
                            if(message != null){
                                // 界面跳转
                                PromptManager.showToast(context, "登录成功");
                                MiddleManager.getInstance().goBack();

                            } else {
                                PromptManager.showToast(context, "服务器繁忙");
                            }
                            super.onPostExecute(message);
                        }
                    }.executeProxy(user);
                    // 成功获取了余额
                }
                break;
        }
    }

    /**
     * 用户信息判断
     * @return
     */
    private boolean checkUser() {
        return true;
    }

    @Override
    protected void init() {
        showInMiddle = (ViewGroup) View.inflate(context, R.layout.ll_user_login, null);

        username = (EditText) findViewById(R.id.ll_user_login_username);
        clear = (ImageView) findViewById(R.id.ll_clear);
        password = (EditText) findViewById(R.id.ll_user_login_password);
        login = (Button) findViewById(R.id.ll_user_login);
    }

    @Override
    public int getID() {
        return ConstantValue.VIEW_LOGIN;
    }
}
