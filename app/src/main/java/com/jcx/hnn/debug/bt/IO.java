package com.jcx.hnn.debug.bt;

import java.io.IOException;

/**
 * Created by yzd on 2018/6/21 0021.
 */

public class IO {

    private HeartBeatThread hbThd = new HeartBeatThread(this);

    public int Write(byte buffer) {
        return -1;
    }

    public int Write(byte[] buffer) {
        return -1;
    }

    public int Write(byte[] buffer, int offset, int count) {
        return -1;
    }

    public int Read(byte[] buffer, int offset, int count, int timeout) {
        return -1;
    }

    public int Read() throws IOException {
        return -1;
    }

    public void Flush() throws IOException { }

    public boolean IsOpened() {
        return false;
    }

    protected void OnOpen() {

    }

    protected void OnClose() {
        this.EndHeartBeat();
    }

    public void Close() {
    }

    private void StartHeartBeat() {
        this.hbThd.BeginHeartBeat();
        (new Thread(this.hbThd)).start();
    }

    private void EndHeartBeat() {
        this.hbThd.EndHeartBeat();
    }

    public void PauseHeartBeat() {
        this.hbThd.PauseHeartBeat();
    }

    public void ResumeHeartBeat() {
        this.hbThd.ResumeHeartBeat();
    }
}
