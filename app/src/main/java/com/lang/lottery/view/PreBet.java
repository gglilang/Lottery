package com.lang.lottery.view;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.lang.lottery.ConstantValue;
import com.lang.lottery.GlobalParams;
import com.lang.lottery.R;
import com.lang.lottery.bean.ShoppingCart;
import com.lang.lottery.bean.Ticket;
import com.lang.lottery.bean.User;
import com.lang.lottery.engine.UserEngine;
import com.lang.lottery.net.protocal.Element;
import com.lang.lottery.net.protocal.Message;
import com.lang.lottery.net.protocal.Oelement;
import com.lang.lottery.net.protocal.element.BetElement;
import com.lang.lottery.util.BeanFactory;
import com.lang.lottery.util.PromptManager;
import com.lang.lottery.view.manager.MiddleManager;

import org.apache.commons.lang3.StringUtils;

/**
 * 近期和倍投的设置界面
 * Created by Lang on 2015/6/2.
 */
public class PreBet extends BaseUI {

    private TextView bettingNum;    // 注数
    private TextView bettingMoney;    // 金额

    private Button subAppnumbers;   //减少倍投
    private TextView appnumbersInfo;   // 倍数
    private Button addAppnumbers;   //增加倍投

    private Button subIssueflagNum;   //减少追期
    private TextView issueflagNumInfo;   // 追期
    private Button addIssueflagNum;   //增加追期

    private ImageButton lotteryPurchase;    // 投注
    private ListView shoppingList;  // 购物车展示

    private ShoppingAdapter adapter;


    public PreBet(Context context) {
        super(context);
    }

    @Override
    protected void setListener() {

        // 倍数
        addAppnumbers.setOnClickListener(this);
        subAppnumbers.setOnClickListener(this);
        // 追期
        addIssueflagNum.setOnClickListener(this);
        subIssueflagNum.setOnClickListener(this);
        // 投注
        lotteryPurchase.setOnClickListener(this);
    }

    @Override
    protected void init() {

        showInMiddle = (ViewGroup) View.inflate(context, R.layout.ll_play_prefectbetting, null);

        bettingNum = (TextView) findViewById(R.id.ll_shopping_list_betting_num);
        bettingMoney = (TextView) findViewById(R.id.ll_shopping_list_betting_money);

        subAppnumbers = (Button) findViewById(R.id.ll_sub_appnumbers);
        appnumbersInfo = (TextView) findViewById(R.id.ll_appnumbers);
        addAppnumbers = (Button) findViewById(R.id.ll_add_appnumbers);

        subIssueflagNum = (Button) findViewById(R.id.ll_sub_issueflagNum);
        issueflagNumInfo = (TextView) findViewById(R.id.ll_issueflagNum);
        addIssueflagNum = (Button) findViewById(R.id.ll_add_issueflagNum);

        lotteryPurchase = (ImageButton) findViewById(R.id.ll_lottery_purchase);

        shoppingList = (ListView) findViewById(R.id.ll_lottery_shopping_list);

        adapter = new ShoppingAdapter();
        shoppingList.setAdapter(adapter);
    }

    @Override
    public int getID() {
        return ConstantValue.VIEW_PREBET;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.ll_add_appnumbers:
                // 增加倍数
                final boolean result = ShoppingCart.getInstance().addAppnumbers(true);
                if(result){
                    changeNotice();
                }
                break;
            case R.id.ll_sub_appnumbers:
                // 减少倍数
                if(ShoppingCart.getInstance().addAppnumbers(false)){
                    changeNotice();
                }
                break;
            case R.id.ll_add_issueflagNum:
                // 增加追期
                if(ShoppingCart.getInstance().addIssuenumbers(true)){
                    changeNotice();
                }
                break;
            case R.id.ll_sub_issueflagNum:
                // 减少追期
                if(ShoppingCart.getInstance().addIssuenumbers(false)){
                    changeNotice();
                }
                break;
            case R.id.ll_lottery_purchase:
                // 投注请求
                User user = new User();
                user.setUsername(GlobalParams.USERNAME);
                new MyHttpTask<User>() {
                    @Override
                    protected Message doInBackground(User... params) {
                        UserEngine engine = BeanFactory.getImpl(UserEngine.class);

                        return engine.bet(params[0]);
                    }

                    @Override
                    protected void onPostExecute(Message message) {
                        super.onPostExecute(message);
                        if(message != null){
                            Oelement oelement = message.getBody().getOelement();
                            if(ConstantValue.SUCCESS.equals(oelement.getErrorcode())){
                                BetElement element = (BetElement) message.getBody().getElements().get(0);
                                // 修改用户的余额信息
                                GlobalParams.MONEY = Float.valueOf(element.getActvalue());
                                // 清理返回键
                                MiddleManager.getInstance().clear();
                                // 跳转到购彩大厅，提示对话框
                                MiddleManager.getInstance().changeUI(Hall.class);
                                PromptManager.showErrorDialog(context, "投注成功");
                                // 清空购物车
                                ShoppingCart.getInstance().clear();
                            }
                        }
                    }
                }.executeProxy(user);
                break;
        }
    }

    @Override
    public void onResume() {
        changeNotice();
        super.onResume();
    }

    private void changeNotice(){
        Integer lotterynumber = ShoppingCart.getInstance().getLotterynumber();
        Integer lotteryvalue = ShoppingCart.getInstance().getLotteryvalue();

        String number = context.getResources().getString(R.string.is_shopping_list_betting_num);
        String money = context.getResources().getString(R.string.is_shopping_list_betting_money);

        number = StringUtils.replace(number, "NUM", lotterynumber.toString());
        money = StringUtils.replaceEach(money, new String[]{"MONEY1", "MONEY2"}, new String[]{lotteryvalue.toString(), GlobalParams.MONEY.toString()});

        bettingNum.setText(Html.fromHtml(number));
        bettingMoney.setText(Html.fromHtml(money));

        // 修改倍数和追期
        appnumbersInfo.setText(ShoppingCart.getInstance().getAppnumbers().toString());
        issueflagNumInfo.setText(ShoppingCart.getInstance().getIssuenumbers().toString());
    }

    private class ShoppingAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return ShoppingCart.getInstance().getTickets().size();
        }

        @Override
        public Object getItem(int position) {
            return ShoppingCart.getInstance().getTickets().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if(convertView == null){

                holder = new ViewHolder();
                convertView = View.inflate(context, R.layout.ll_play_prefectbetting_row, null);

                holder.redNum = (TextView) convertView.findViewById(R.id.ii_shopping_item_reds);
                holder.blueNum = (TextView) convertView.findViewById(R.id.ii_shopping_item_blues);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Ticket ticket = (Ticket) getItem(position);
            holder.redNum.setText(ticket.getRedNum());
            holder.blueNum.setText(ticket.getBlueNum());

            return convertView;
        }

        class ViewHolder{
            TextView redNum;
            TextView blueNum;
        }
    }
}
