package com.lib.commonlibrary;

import com.frame.core.BaseApplication;
import com.lib.sharelib.ShareHelper;

/**
 * Created by yzd on 2017/2/10 0010.
 */

public class MyApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        ShareHelper.getInstance()
                .setSina("2635404820", "ec743121ef7d06c707e0a01f69e615cc")
                .setWeixin("wx3802cab2c5681ca8", "23f56f1b2c6e45225dcd01145cf4ede3")
                .setQQ("1104741678", "3sFFig9X22svZsXo")
                .setDebug(true)
                .init(this, "587c8ddc734be4160b001c6d");
    }
}
