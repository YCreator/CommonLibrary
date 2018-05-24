package com.frame.core.mvvm.binding.command;

/**
 * Created by yzd on 2018/5/23 0023.
 */

public interface BindingConsumer<T> {
    void call(T t);
}
