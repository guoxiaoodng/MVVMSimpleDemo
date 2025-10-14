package com.tecent.tecentx5;

import android.content.Context;
import android.util.Log;

import com.tencent.smtt.sdk.QbSdk;


/**
 * @author guoxi
 */
public class TecentX5WebUtil {

    public static TecentX5WebUtil util;

    public static TecentX5WebUtil getUtil() {
        if (util == null) {
            util = new TecentX5WebUtil();
        }
        return util;
    }

    public void initX5Web(final Context context) {
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。

        try {
            QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

                @Override
                public void onViewInitFinished(boolean arg0) {
                    // TODO Auto-generated method stub
                    //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                    Log.d("app", " onViewInitFinished is " + arg0);
                }

                @Override
                public void onCoreInitFinished() {
                    // TODO Auto-generated method stub
                }
            };

            //x5内核初始化接口
            QbSdk.initX5Environment(context,  cb);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
