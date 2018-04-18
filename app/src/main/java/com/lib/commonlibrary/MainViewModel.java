package com.lib.commonlibrary;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;

import com.frame.core.mvvm.base.BaseViewModel;
import com.frame.core.mvvm.binding.command.BindingCommand;
import com.jcx.hnn.R;
import com.lib.tpl.ShareHelper;
import com.lib.tpl.TpHelper;
import com.lib.tpl.share.PLATFORM_TYPE;
import com.lib.tpl.share.ShareEntity;
import com.lib.tpl.share.ShareImage;

import java.io.File;
import java.util.ArrayList;

import io.reactivex.functions.Action;


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

    public BindingCommand shareClick = new BindingCommand(new Action() {
        @Override
        public void run() {
            ArrayList<String> list = new ArrayList<>();
            /*list.add("https://img.alicdn.com/bao/uploaded/i2/2888353680/TB2iKTbfYJmpuFjSZFwXXaE4VXa_!!2888353680.jpg_300x300.jpg");
            list.add("https://gd1.alicdn.com/imgextra/i1/324690070/TB2.4CSuiC9MuFjSZFoXXbUzFXa_!!324690070.jpg_300x300.jpg");
            list.add("https://img.alicdn.com/bao/uploaded/i3/858179654/TB1oxgNoInI8KJjSsziXXb8QpXa_!!0-item_pic.jpg_300x300.jpg");*/
            list.add(new File(getChileFile("59PiImage"), "a.jpg").getAbsolutePath());
            list.add(new File(getChileFile("59PiImage"), "b.jpg").getAbsolutePath());
            list.add(new File(getChileFile("59PiImage"), "c.jpg").getAbsolutePath());
            list.add(new File(getChileFile("59PiImage"), "d.jpg").getAbsolutePath());
            list.add(new File(getChileFile("59PiImage"), "e.jpg").getAbsolutePath());
            list.add(new File(getChileFile("59PiImage"), "f.jpg").getAbsolutePath());
            list.add(new File(getChileFile("59PiImage"), "g.jpg").getAbsolutePath());
            list.add(new File(getChileFile("59PiImage"), "h.jpg").getAbsolutePath());
            list.add(new File(getChileFile("59PiImage"), "i.jpg").getAbsolutePath());
            TpHelper.share().shareToQzone((Activity) context, list, "test", new ShareHelper.ShareQzoneListener() {
                @Override
                public void onComplete(Object o) {

                }

                @Override
                public void onError(String s) {

                }

                @Override
                public void onCancel() {

                }
            });
            if(true) return;
            ShareImage image = new ShareImage(context, R.drawable.umeng_socialize_qq);
            image.setThumb(new ShareImage(context, R.mipmap.ic_launcher));
            ShareEntity entity = new ShareEntity("test", "https://www.baidu.com", "hello world", image);
            TpHelper.share().shareWeb((Activity) context, entity, PLATFORM_TYPE.QQ);
        }
    });

    public File getMainDirectory() {
        File appDir;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            appDir = new File(Environment.getExternalStorageDirectory(), "59wang");
        } else {
            appDir = new File(Environment.getDownloadCacheDirectory(), "59wang");
        }
        if (!appDir.exists()) appDir.mkdirs();
        return appDir;
    }

    /**
     * 创建并获取app子存储文件
     */
    public File getChileFile(@NonNull String fileName) {
        File childFile = new File(getMainDirectory(), fileName);
        if (!childFile.exists()) childFile.mkdir();
        return childFile;
    }

}
