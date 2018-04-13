package com.lib.tpl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ali.auth.third.core.model.Session;
import com.alibaba.baichuan.android.trade.AlibcTradeSDK;
import com.alibaba.baichuan.android.trade.adapter.login.AlibcLogin;
import com.alibaba.baichuan.android.trade.callback.AlibcLoginCallback;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeInitCallback;
import com.lib.tpl.auth.ShareLogin;
import com.lib.tpl.share.PLATFORM_TYPE;

/**
 * 授权工具
 * Created by yzd on 2017/2/23 0023.
 */

public class AuthHelper {

    private static AuthHelper helper;

    // private Tencent tencent;

    private AuthHelper() {
    }

    static AuthHelper getInstance() {
        if (helper == null) {
            helper = new AuthHelper();
        }
        return helper;
    }

    void initAuth(Context context) {
        initAlibaba(context, new AlibcTradeInitCallback() {
            @Override
            public void onSuccess() {
                Log.i("alibaba", "success");
            }

            @Override
            public void onFailure(int i, String s) {
                Log.i("alibaba", "failure:" + i + "_" + s);
            }
        });
    }

    /**
     * 初始化阿里sdk
     */
    private void initAlibaba(Context context, AlibcTradeInitCallback callback) {
        AlibcTradeSDK.asyncInit(context, callback);
    }

    public void taobaoLogin(Activity activity, final TaobaoLoginCallback callback) {
        final AlibcLogin alibcLogin = AlibcLogin.getInstance();
        alibcLogin.showLogin(activity, new AlibcLoginCallback() {
            @Override
            public void onSuccess() {
                if (callback != null) {
                    callback.onSuccess(alibcLogin.getSession());
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                if (callback != null) {
                    callback.onFailure(code, msg);
                }
            }
        });
    }

    /**
     * 其他第三方登录
     * @param activity
     * @param type
     * @param listener
     */
    public void otherLogin(Activity activity, PLATFORM_TYPE type, final ShareLogin.AuthListener listener) {
        ShareLogin.login(activity, type, listener);
    }

    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        ShareLogin.onActivityResult(activity, requestCode, resultCode, data);
    }

    public interface TaobaoLoginCallback {

        void onSuccess(Session session);

        void onFailure(int code, String msg);

    }
}
