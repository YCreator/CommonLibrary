package com.frame.core;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;

/**
 * application基类
 * Created by yzd on 2016/6/17.
 */
public class BaseApplication extends Application {

    static Context _context;
    static Resources _resource;
    static AssetManager _asset;

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
