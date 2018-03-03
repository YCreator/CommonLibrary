package com.lib.commonlibrary;

import android.app.Activity;
import android.content.Context;

import com.frame.core.base.BaseViewModel;
import com.frame.core.binding.command.BindingCommand;
import com.jcx.hnn.R;
import com.lib.sharelib.ShareEntity;
import com.lib.sharelib.ShareHelper;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import rx.functions.Action0;

/**
 * Created by yzd on 2018/2/10 0010.
 */

public class MainViewModel extends BaseViewModel {

    public UserModel userModel;

    public MainViewModel(Context context) {
        super(context);
        this.userModel = new UserModel();
        userModel.setUsername("你好");
        userModel.setPassword("asd");
    }

    public BindingCommand shareClick = new BindingCommand(new Action0() {
        @Override
        public void call() {
            UMImage image = new UMImage(context, R.drawable.umeng_socialize_qq);
            image.setThumb(new UMImage(context, R.mipmap.ic_launcher));
            ShareEntity entity = new ShareEntity("test", "https://www.baidu.com", "hello world", image);
            ShareHelper.getInstance().shareWeb((Activity) context, entity, SHARE_MEDIA.QQ);
        }
    });
}
