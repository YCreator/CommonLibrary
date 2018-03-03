package com.lib.commonlibrary;

import android.content.Intent;

import com.frame.core.base.NewBaseActivity;
import com.jcx.hnn.BR;
import com.jcx.hnn.R;
import com.jcx.hnn.databinding.ActivityMainBinding;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.utils.Log;

public class MainActivity extends NewBaseActivity<ActivityMainBinding, MainViewModel> {

   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        ViewDataBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        UserModel model = new UserModel();
        model.setUsername("hello");
        model.setPassword("123456");
        binding.setVariable(BR.user, model);
        *//*GoodsBean.getGoods(new HashMap<String, String>()
                , new JsonDataObserver<List<GoodsBean>>() {
                    @Override
                    void onSuccess(List<GoodsBean> goodsBeen) {
                        for (GoodsBean bean : goodsBeen) {
                            TLog.i(bean.toString());
                        }
                    }

                    @Override
                    void onError(int errorCode, String message) {
                        TLog.i(message);
                    }

                    @Override
                    Lifeful lifeful() {
                        return null;
                    }
                }, new NetworkConsumer());*//*
    }*/

    @Override
    public int initContentView() {
        return R.layout.activity_main;
    }

    @Override
    public int initVariableId() {
        return BR.user;
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
}
