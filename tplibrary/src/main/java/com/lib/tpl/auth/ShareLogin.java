package com.lib.tpl.auth;

import android.app.Activity;
import android.content.Intent;

import com.lib.tpl.share.PLATFORM_TYPE;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

/**
 * Created by yzd on 2018/4/9 0009.
 */

public class ShareLogin {

    public static void login(Activity activity, PLATFORM_TYPE type, final AuthListener listener) {
        UMShareAPI.get(activity).getPlatformInfo(activity, type.getPlatform(), new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                listener.onComplete(PLATFORM_TYPE.getType(share_media), i, map);
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                listener.onError(PLATFORM_TYPE.getType(share_media), i, throwable);
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                listener.onCancel(PLATFORM_TYPE.getType(share_media), i);
            }
        });
    }

    public static void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        UMShareAPI.get(activity).onActivityResult(requestCode, resultCode, data);
    }

    public interface AuthListener {
        void onComplete(PLATFORM_TYPE type, int i, Map<String, String> map);

        void onError(PLATFORM_TYPE type, int i, Throwable throwable);

        void onCancel(PLATFORM_TYPE type, int i);
    }
}
