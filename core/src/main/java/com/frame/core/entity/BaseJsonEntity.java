package com.frame.core.entity;

/**
 * Created by yzd on 2017/10/17 0017.
 */

public abstract class BaseJsonEntity<T> {

    public abstract boolean isSuccess();

    public abstract String getMessage();

    public abstract int getCode();

    public abstract T getData();
}
