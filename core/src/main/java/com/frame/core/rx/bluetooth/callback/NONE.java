package com.frame.core.rx.bluetooth.callback;

/**
 * 这一个类用于表示什么也不是, 什么也没有
 * Created by yzd on 2018/6/13 0013.
 */

public final class NONE {

    public static final NONE NONE = new NONE();

    private NONE() {
    }

    @Override
    public String toString() {
        return "NONE";
    }
}
