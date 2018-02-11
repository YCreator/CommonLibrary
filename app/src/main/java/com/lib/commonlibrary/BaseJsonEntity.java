package com.lib.commonlibrary;


import com.frame.core.entity.JsonEntity;

/**
 * Created by yzd on 2016/7/11.
 */
public class BaseJsonEntity extends JsonEntity {

    private int success;
    private int error;
    private int fault;
    private String msg;

    public BaseJsonEntity(int error, String msg) {
        this.error = error;
        this.msg = msg;
    }

    @Override
    public boolean isSuccess() {
        return success == 1;
    }

    @Override
    public void setSuccess(boolean isSuccess) {
        this.success = isSuccess ? 1 : 0;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    @Override
    public boolean isError() {
        return error == 1;
    }

    @Override
    public String getMessage() {
        return msg == null ? "" : msg;
    }

    @Override
    public int getCode() {
        return 0;
    }

    @Override
    public void setCode(int code) {

    }

    public boolean isFault() {
        return fault == 1;
    }

}
