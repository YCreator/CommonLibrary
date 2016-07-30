package com.frame.core.net.okhttp;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by yzd on 2016/7/20.
 */
public class DownloadRepCallback implements Callback {

    @Override
    public void onFailure(Call call, IOException e) {

    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        if (response.isSuccessful()) {
            InputStream stream = response.body().byteStream();
        }
    }
}
