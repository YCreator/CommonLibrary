package com.lib.commonlibrary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.frame.core.rx.Lifeful;
import com.frame.core.util.TLog;
import com.jcx.hnn.R;
import com.lib.sharelib.ShareEntity;
import com.lib.sharelib.ShareHelper;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.utils.Log;

import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GoodsBean.getGoods(new HashMap<String, String>()
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
                }, new NetworkConsumer());
    }

    public void share(View v) {
        UMImage image = new UMImage(this, R.drawable.umeng_socialize_qq);
        image.setThumb(new UMImage(this, R.mipmap.ic_launcher));
        ShareEntity entity = new ShareEntity("test", "https://www.baidu.com", "hello world", image);
        ShareHelper.getInstance().shareWeb(this, entity, SHARE_MEDIA.QQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("onActivityResult", requestCode + "_" + resultCode);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

    }
}
