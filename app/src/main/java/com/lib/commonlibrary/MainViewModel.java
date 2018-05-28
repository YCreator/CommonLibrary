package com.lib.commonlibrary;

import android.app.Activity;
import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.frame.core.mvvm.adapter.ItemBinding;
import com.frame.core.mvvm.adapter.OnItemBind;
import com.frame.core.mvvm.base.BaseViewModel;
import com.frame.core.mvvm.binding.command.BindingAction;
import com.frame.core.mvvm.binding.command.BindingCommand;
import com.jcx.hnn.BR;
import com.jcx.hnn.R;
import com.lib.tpl.TpHelper;
import com.lib.tpl.share.PLATFORM_TYPE;
import com.lib.tpl.share.ShareEntity;
import com.lib.tpl.share.ShareImage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;


/**
 * Created by yzd on 2018/2/10 0010.
 */

public class MainViewModel extends BaseViewModel {

    public UserModel userModel;

    public String imgUrl = "https://img.alicdn.com/bao/uploaded/i1/2060208383/TB1AY4FeZnI8KJjSsziXXb8QpXa_!!0-item_pic.jpg_300x300.jpg";

    public MainViewModel(Context context) {
        super(context);
        this.userModel = new UserModel();
        userModel.setUsername("你好");
        userModel.setPassword("asd");
        observableList.add(new ItemViewModel(context, userModel));
        UserModel userModel1 = new UserModel();
        userModel1.setUsername("qqqqq");
        userModel1.setPassword("331414");
        observableList.add(new ItemViewModel(context, userModel1));
        UserModel userModel2 = new UserModel();
        userModel2.setUsername("hello");
        userModel2.setPassword("123432");
        observableList.add(new ItemViewModel(context, userModel2));
    }

    @Override
    public void registerEventBus() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void unregisterEventBus() {
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void sub(String value) {
        Toast.makeText(context, value, Toast.LENGTH_SHORT).show();
    }

    public BindingCommand shareClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            Log.i("hello", "nihao");
            EventBus.getDefault().post("你好!");
            if (true) return;
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

    public ObservableList<ItemViewModel> observableList = new ObservableArrayList<>();

    public ItemBinding<ItemViewModel> itemBinding = ItemBinding.of(new OnItemBind<ItemViewModel>() {
        @Override
        public void onItemBind(ItemBinding itemBinding, int position, ItemViewModel item) {
            itemBinding.variableId(BR.viewModel);
            if (item.model.getUsername().equals("qqqqq")) {
                itemBinding.layoutRes(R.layout.item_two);
            } else {
                itemBinding.layoutRes(R.layout.item_one);
            }
        }
    });

}
