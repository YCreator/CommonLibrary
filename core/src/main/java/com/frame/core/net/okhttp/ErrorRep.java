package com.frame.core.net.okhttp;

/**
 * Created by yzd on 2017/4/8 0008.
 */

public class ErrorRep {

    private int errorCode;
    private String message;

    public ErrorRep(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }
}
