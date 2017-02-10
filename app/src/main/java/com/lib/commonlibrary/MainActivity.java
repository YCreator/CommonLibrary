package com.lib.commonlibrary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


import com.lib.sharelib.ShareEntity;
import com.lib.sharelib.ShareHelper;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void share(View v) {
        UMImage image = new UMImage(this, R.drawable.umeng_socialize_qq);
        image.setThumb(new UMImage(this, R.mipmap.ic_launcher));
        ShareEntity entity = new ShareEntity("test", "https://www.baidu.com", "hello world", image);
        ShareHelper.getInstance().share(this, entity, SHARE_MEDIA.QQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

    }
}
