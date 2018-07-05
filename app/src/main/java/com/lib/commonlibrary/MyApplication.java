package com.lib.commonlibrary;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.multidex.MultiDex;

import com.frame.core.BaseApplication;
import com.frame.core.crash.CaocConfig;
import com.frame.core.crash.DefaultErrorActivity;
import com.jcx.hnn.debug.R;
import com.jcx.hnn.debug.bt.BTManager;
import com.lib.glideloader.GlideLoader;
import com.lib.imagelib.ImageEngine;

/**
 * Created by yzd on 2017/2/10 0010.
 */

public class MyApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        //Utils.setDebug(true);
        //initCrash();
        ImageEngine.init(this, new GlideLoader());
       /* TpHelper.init(this, new TpHelper.Options().setSina("2635404820", "ec743121ef7d06c707e0a01f69e615cc")
                .setWeixin("wx3802cab2c5681ca8", "23f56f1b2c6e45225dcd01145cf4ede3")
                .setQQ("1104741678", "3sFFig9X22svZsXo")
                .setDebug(true)
                .canShare(true)
                .setUmeng("587c8ddc734be4160b001c6d"));*/
        BTManager.getInstance().init();

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void initCrash() {
        CaocConfig.Builder.create()
                .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT) //背景模式,开启沉浸式
                .enabled(true) //是否启动全局异常捕获
                .showErrorDetails(true) //是否显示错误详细信息
                .showRestartButton(true) //是否显示重启按钮
                .trackActivities(true) //是否跟踪Activity
                .minTimeBetweenCrashesMs(2000) //崩溃的间隔时间(毫秒)
                .errorDrawable(R.mipmap.ic_launcher) //错误图标
                .restartActivity(Main2Activity.class) //重新启动后的activity
                .errorActivity(DefaultErrorActivity.class) //崩溃后的错误activity
//                .eventListener(new YourCustomEventListener()) //崩溃后的错误监听
                .apply();
    }

    private static Handler handler;

    public static Handler getHandler() {
        synchronized (MyApplication.class) {
            if (handler == null) {
                handler = new Handler(Looper.getMainLooper());
            }
            return handler;
        }
    }
}
