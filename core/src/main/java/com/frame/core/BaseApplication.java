package com.frame.core;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import com.frame.core.base.AppManager;
import com.frame.core.db.DatabaseManager;
import com.frame.core.net.okhttp.CookiesManager;
import com.frame.core.util.utils.DeviceUtils;
import com.frame.core.util.utils.Utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * application基类
 * Created by yzd on 2016/6/17.
 */
public class BaseApplication extends Application {

    @SuppressLint("StaticFieldLeak")
    private static Context _context;
    private static Resources _resource;
    private static AssetManager _asset;
    private static CookiesManager cookiesManager;
    private static ExecutorService executor;
    public static String deviceId;
    public static int status = -1;
    public static int MODEL;
    public volatile static boolean DEBUG = false;       //控制开发和生产模式
    public static final int PRO = 0;    //生产
    public static final int DEV = 1;    //开发

    @Override
    public void onCreate() {
        Utils.init(this);
        super.onCreate();
        _context = this.getApplicationContext();
        DEBUG = Utils.isDebug();
        _resource = _context.getResources();
        _asset = _context.getAssets();
        //注册监听每个activity的生命周期,便于堆栈式管理
        registerActivityLifecycleCallbacks(mCallbacks);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    /**
     * cookie管理器
     *
     * @return
     */
    public static CookiesManager getCookiesManager() {
        if (cookiesManager == null) {
            cookiesManager = new CookiesManager(get_context());
        }
        return cookiesManager;
    }

    /**
     * 数据库初始化
     *
     * @param helper
     */
    public void initDatabase(SQLiteOpenHelper helper) {
        DatabaseManager.initializeInstance(helper);
    }

    /**
     * 获取设备id
     *
     * @return
     */
    public static String getDeviceId() {
        if (deviceId == null) {
            deviceId = DeviceUtils.getDeviceId();
        }
        if (deviceId == null) deviceId = "";
        return deviceId;
    }

    /**
     * 应用内通用一个线程池
     *
     * @return
     */
    public static ExecutorService getExecutor() {
        if (executor == null) {
            //executor = new ThreadPoolExecutor(3, 3, 200, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(50));
            executor = Executors.newCachedThreadPool();
        }
        return executor;
    }

    public static Context get_context() {
        return _context;
    }

    public static Resources get_resource() {
        return _resource;
    }

    public static AssetManager get_asset() {
        return _asset;
    }

    public static boolean isDebug() {
        //return BuildConfig.DEBUG;
        return DEBUG;
    }

    private ActivityLifecycleCallbacks mCallbacks = new ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            AppManager.getAppManager().addActivity(activity);
        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            AppManager.getAppManager().removeActivity(activity);
        }
    };

}
