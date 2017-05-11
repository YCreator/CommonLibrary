package com.lib.commonlibrary;

/**
 * Created by Administrator on 2016/1/11.
 */
public class JsonDataEntity<T> extends BaseJsonEntity  {

    private T data;

    public JsonDataEntity(int error, String msg) {
        super(error, msg);
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
