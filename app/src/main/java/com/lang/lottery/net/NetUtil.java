package com.lang.lottery.net;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import com.lang.lottery.GlobalParams;

import org.apache.http.conn.ClientConnectionManager;

/**
 * Created by Lang on 2015/5/12.
 */
public class NetUtil {

    /**
     * 检查用户的网络
     */
    public static boolean checkNet(Context context){
        //判断：WIFI连接
        boolean isWIFI = isWiFIConnection(context);
        //判断：Mobile连接
        boolean isMobile = isMobileConnection(context);

        //如果Mobile在连接，判断是哪个APN被选中了
        if(isMobile){
            //APN被选中的代理信息是否有内容，如果有wap方式
            readAPN(context);
        }

        if(!isWIFI && !isMobile){
            return false;
        }
        return true;
    }

    /**
     * APN被选中的代理信息是否有内容，如果有wap方式
     * @param context
     */
    private static void readAPN(Context context) {

        Uri PREFERRED_APN_URI = Uri.parse("content://telephony/carriers/preferapn");    //4.0模拟器屏蔽掉该权限

        //和操作联系人类似
        ContentResolver resolver = context.getContentResolver();
        //判断是那个APN被选中了
        Cursor cursor = resolver.query(PREFERRED_APN_URI, null, null, null, null);
        if(cursor != null && cursor.moveToFirst()){
            GlobalParams.PROXY = cursor.getString(cursor.getColumnIndex("proxy"));
            GlobalParams.PORT = cursor.getInt(cursor.getColumnIndex("port"));
        }
    }

    /**
     * //判断：Mobile连接
     * @param context 上下文
     * @return
     */
    private static boolean isMobileConnection(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if(networkInfo != null){
            return networkInfo.isConnected();
        }
        return false;
    }

    /**
     * //判断：WIFI连接
     * @param context 上下文
     * @return
     */
    private static boolean isWiFIConnection(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if(networkInfo != null){
            return networkInfo.isConnected();
        }
        return false;
    }
}

