package com.frame.core.net.Retrofit;

import android.util.SparseArray;

/**
 * Created by yzd on 2017/4/8 0008.
 */

public class SuccessRep<T> {

    private SparseArray<Object> obj;

    public SuccessRep(SparseArray<Object> obj) {
        this.obj = obj;
    }

    @SuppressWarnings("unchecked")
    public T getData() {
        try {
            return (T) obj.get(0);
        } catch (Exception e) {
            return null;
        }
    }

    public String getRepString() {
        return obj.get(1).toString();
    }
}
