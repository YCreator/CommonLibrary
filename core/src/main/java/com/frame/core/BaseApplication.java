package com.frame.core;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.sqlite.SQLiteOpenHelper;

import com.frame.core.db.DatabaseManager;
import com.frame.core.net.okhttp.CookiesManager;
import com.frame.core.util.AppHelper;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * application基类
 * Created by yzd on 2016/6/17.
 */
public class BaseApplication extends Application {

    static Context _context;
    static Resources _resource;
    static AssetManager _asset;
    static CookiesManager cookiesManager;
    static ThreadPoolExecutor executor;
    static String deviceId;
    public static int MODEL;
    public static final boolean DEBUG = false;       //控制开发和生产模式

    @Override
    public void onCreate() {
        super.onCreate();
        _context = this.getApplicationContext();
        _resource = _context.getResources();
        _asset = _context.getAssets();
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
     * @param helper
     */
    public void initDatabase(SQLiteOpenHelper helper) {
        DatabaseManager.initializeInstance(helper);
    }

    /**
     * 获取设备id
     * @return
     */
    public static String getDeviceId() {
        if (deviceId == null) {
            deviceId = AppHelper.getDeviceId(get_context());
        }
        return deviceId;
    }

    /**
     * 应用内通用一个线程池
     * @return
     */
    public static ThreadPoolExecutor getExecutor() {
        if (executor == null) {
            executor = new ThreadPoolExecutor(3, 3, 200, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(20));
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
}
