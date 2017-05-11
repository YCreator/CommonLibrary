package com.lib.commonlibrary;

import com.frame.core.net.okhttp.OkHttpEngine;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by yzd on 2017/5/9 0009.
 */

public class RetroFactory {
    private static String baseUrl = "https://api.59iwh.com/";

    private static RetrofitService retrofitService;

    private RetroFactory() {

    }

    public static RetrofitService getInstance() {
        if (retrofitService == null) {
            retrofitService = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(OkHttpEngine.getInstance().getOkHttpClient())
                    .build()
                    .create(RetrofitService.class);
        }
        return retrofitService;
    }
}
