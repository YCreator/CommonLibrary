package com.jcx.hnn.debug.bt;

import android.util.Log;

/**
 * Created by yzd on 2018/6/21 0021.
 */

public class HeartBeatThread implements Runnable {

    private IO io;
    private final Object lock = new Object();
    private boolean pause;
    private boolean exit;

    public HeartBeatThread(IO io) {
        this.io = io;
    }

    @Override
    public void run() {
        byte[] buffer = new byte[]{16, 4, 1};

        while(!this.exit) {
            if(this.pause) {
                this.WaitMs(10L);
            } else {
                if(this.io == null || !this.io.IsOpened()) {
                    break;
                }

                Object var2 = this.lock;
                synchronized(this.lock) {
                    if(!this.pause && this.io.Write(buffer, 0, buffer.length) == -1) {
                        this.exit = true;
                    }
                }

                this.WaitMs(1000L);
            }
        }
    }

    public void PauseHeartBeat() {
        Object var1 = this.lock;
        synchronized(this.lock) {
            this.pause = true;
            Log.i("HeartBeatThread", "PauseHeartBeat");
        }
    }

    public void ResumeHeartBeat() {
        this.pause = false;
        Log.i("HeartBeatThread", "ResumeHeartBeat");
    }

    public void BeginHeartBeat() {
        this.pause = false;
        this.exit = false;
    }

    public void EndHeartBeat() {
        this.exit = true;
    }

    private void WaitMs(long ms) {
        long time = System.currentTimeMillis();

        while(System.currentTimeMillis() - time < ms) {
            ;
        }

    }
}
