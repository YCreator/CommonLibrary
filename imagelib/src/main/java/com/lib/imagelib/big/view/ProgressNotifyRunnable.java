package com.lib.imagelib.big.view;

/**
 * Created by yzd on 2018/5/10 0010.
 */

public abstract class ProgressNotifyRunnable implements Runnable {
    protected int mProgress = -1;

    public boolean update(int progress) {
        boolean notified = mProgress == -1;
        mProgress = progress;
        return notified;
    }

    public void notified() {
        mProgress = -1;
    }

}
