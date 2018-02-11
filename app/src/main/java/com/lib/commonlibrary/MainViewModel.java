package com.lib.commonlibrary;

import com.frame.core.base.BaseViewModel;

/**
 * Created by yzd on 2018/2/10 0010.
 */

public class MainViewModel extends BaseViewModel {

    public UserModel userModel;

    public MainViewModel() {
        this.userModel = new UserModel();
        userModel.setUsername("你好");
        userModel.setPassword("asd");
    }
}
