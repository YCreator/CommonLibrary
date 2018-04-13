package com.lib.imagelib.pub;

/**
 * Created by yzd on 2018/3/17 0017.
 */

public class ImageException extends Exception {
    private int code;
    private String message;

    public ImageException(String message, int code) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
