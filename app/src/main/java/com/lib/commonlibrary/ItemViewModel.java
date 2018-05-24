package com.lib.commonlibrary;

import android.content.Context;

import com.frame.aop.annotation.Trace;
import com.frame.core.mvvm.base.BaseViewModel;

/**
 * Created by yzd on 2018/5/24 0024.
 */

public class ItemViewModel extends BaseViewModel {

    public UserModel model;

    public ItemViewModel(Context context, UserModel model) {
        super(context);
        this.model = model;
        model = test(model.getUsername() + ",hi", model.getPassword() + ",hello");
    }

    @Trace
    private UserModel test(String name, String password) {
        UserModel m = new UserModel();
        m.setPassword(password);
        m.setUsername(name);
        return m;
    }
}
