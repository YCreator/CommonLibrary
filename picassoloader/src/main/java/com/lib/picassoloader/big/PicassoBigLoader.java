package com.lib.picassoloader.big;

import android.net.Uri;
import android.view.View;

import com.lib.imagelib.big.event.CacheHitEvent;
import com.lib.imagelib.big.event.CacheHitEvent2;
import com.lib.imagelib.big.event.ErrorEvent;
import com.lib.imagelib.big.loader.BigLoader;
import com.lib.imagelib.big.progress.ProgressInterceptor;
import com.lib.imagelib.big.view.BigImageView;
import com.lib.imagelib.config.GlobalConfig;
import com.lib.imagelib.utils.ImageUtil;
import com.lib.imagelib.utils.ThreadPoolFactory;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSource;
import okio.Okio;
import okio.Sink;

/**
 * Created by yzd on 2018/5/11 0011.
 */

public class PicassoBigLoader implements BigLoader {
    private Picasso picasso;
    private OkHttpClient client;
    private static volatile int count;

    public PicassoBigLoader(OkHttpClient client) {
        OkHttpClient client1 = client.newBuilder().addNetworkInterceptor(new ProgressInterceptor()).build();
        this.client = client1;
        picasso = new Picasso.Builder(GlobalConfig.context)
                .downloader(new OkHttp3Downloader(client1))
                .build();
    }

    public static void clearCache() {
        File dir2 = new File(GlobalConfig.context.getCacheDir(), "picassobig");
        if (dir2.exists()) {
            ImageUtil.deleteFolderFile(dir2.getAbsolutePath(), false);
        }
    }

    @Override
    public void loadImage(final Uri uri) {
        if (!uri.toString().startsWith("http")) {
            if (uri.toString().startsWith("file")) {
                File file = new File(uri.getPath());
                if (file.exists()) {
                    EventBus.getDefault().post(new CacheHitEvent(file, uri.toString()));
                } else {
                    EventBus.getDefault().post(new ErrorEvent(uri.toString()));
                }
            } else {
                EventBus.getDefault().post(new CacheHitEvent2(uri, uri.toString()));
            }

            return;
        }

        ThreadPoolFactory.getDownLoadPool().execute(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder().url(uri.toString()).build();
                client.newCall(request).enqueue(new okhttp3.Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        EventBus.getDefault().post(new ErrorEvent(uri.toString()));

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                        if (!response.isSuccessful()) {
                            EventBus.getDefault().post(new ErrorEvent(uri.toString()));
                            return;
                        }
                        File dir = new File(GlobalConfig.context.getCacheDir(), "picassobig");
                        if (!dir.exists()) {
                            dir.mkdirs();
                        }
                        final File file = new File(dir, count % 100 + "-tmp.jpg");
                        BufferedSource source = response.body().source();
                        Sink sink = Okio.sink(file);
                        source.readAll(sink);
                        source.close();
                        sink.close();
                        if (file.exists() && file.length() > 80) {
                            count++;
                            EventBus.getDefault().post(new CacheHitEvent(file, uri.toString()));
                        } else {
                            EventBus.getDefault().post(new ErrorEvent(uri.toString()));
                        }


                    }
                });
            }
        });
    }

    @Override
    public View showThumbnail(BigImageView parent, Uri thumbnail, int scaleType) {
        return null;
    }

    @Override
    public void prefetch(Uri uri) {
        //Picasso.with(ImageLoader.context)
        picasso.load(uri.toString()).fetch(new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {

            }
        });

    }
}
