package com.lib.commonlibrary;

import android.app.Application;

import com.lib.sharelib.ShareHelper;

/**
 * Created by yzd on 2017/2/10 0010.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ShareHelper.getInstance()
                .setSina("1998847084", "22e2df16b773588f445f5ec02bf3cddf")
                .setQQ("1104741678", "3sFFig9X22svZsXo")
                .setDebug(true)
                .init(this, "551cb168fd98c5845300070d");
    }
}
