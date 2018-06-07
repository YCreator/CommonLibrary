package com.lib.commonlibrary;

import android.content.Intent;
import android.os.Looper;
import android.util.Log;

import com.frame.aop.annotation.MethodLog;
import com.jcx.hnn.debug.BR;
import com.jcx.hnn.debug.R;
import com.jcx.hnn.debug.databinding.ActivityMainBinding;
import com.umeng.socialize.UMShareAPI;

public class MainActivity extends MyBaseActivity<ActivityMainBinding, MainViewModel> {

    @Override
    public int initContentView() {
        return R.layout.activity_main;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public MainViewModel initViewModel() {
        return new MainViewModel(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("onActivityResult", requestCode + "_" + resultCode);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void initViewObservable() {
        async();
    }

    //@Permission({Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE})
    @MethodLog
    private void async() {
        Log.i("thread", Looper.myLooper() == Looper.getMainLooper() ? "TRUE" : "FALSE");
    }

}
