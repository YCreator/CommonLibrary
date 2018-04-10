package com.lib.tpl.share;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.tencent.connect.share.QzonePublish;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.utils.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 分享工具
 * Created by yzd on 2017/2/10 0010.
 */

public final class ShareHelper {

    private static ShareHelper helper;
    private Context mContext;

    private ShareHelper() {
    }

    public static ShareHelper getInstance() {
        if (helper == null) {
            helper = new ShareHelper();
        }
        return helper;
    }

    public ShareHelper setWeixin(String id, String secret) {
        PlatformConfig.setWeixin(id, secret);
        return this;
    }

    public ShareHelper setSina(String id, String secret) {
        PlatformConfig.setSinaWeibo(id, secret, "http://sns.whalecloud.com");
        return this;
    }

    public ShareHelper setQQ(String id, String secret) {
        PlatformConfig.setQQZone(id, secret);
        return this;
    }

    public ShareHelper setAlipay(String id) {
        PlatformConfig.setAlipay(id);
        return this;
    }

    public ShareHelper setDing(String id) {
        PlatformConfig.setDing(id);
        return this;
    }

    public ShareHelper setDebug(boolean debug) {
        Config.DEBUG = debug;
        return this;
    }

    /**
     * 分享初始化
     *
     * @param context
     * @param key
     */
    public void init(Context context, String key) {
        mContext = context.getApplicationContext();
        UMShareAPI.init(context, key);
        UMShareAPI.get(context);
    }

    /**
     * 友盟分享
     *
     * @param activity
     * @param entity
     * @param platform
     */
    /*public void share(Activity activity, ShareEntity entity, SHARE_MEDIA platform) {
        new ShareAction(activity)
                .setPlatform(platform)
                .withText(entity.getText())
                .withSubject(entity.getTitle())
                .withTargetUrl(entity.getTargetUrl())
                .withMedia(entity.getImage())
                .setCallback(umShareListener)
                .share();
    }*/

    /**
     * 友盟分享链接
     *
     * @param activity
     * @param entity
     * @param platform
     */
    public void shareWeb(Activity activity, ShareEntity entity, PLATFORM_TYPE platform) {
        UMWeb web = new UMWeb(entity.getTargetUrl());
        web.setTitle(entity.getTitle());
        web.setDescription(entity.getText());
        web.setThumb(entity.getImage());
        new ShareAction(activity)
                .withMedia(web)
                .setPlatform(platform.getPlatform())
                .setCallback(umShareListener)
                .share();
    }

    public void shareWeb(Activity activity, ShareEntity entity, PLATFORM_TYPE platform, final ShareListener listener) {
        UMWeb web = new UMWeb(entity.getTargetUrl());
        web.setTitle(entity.getTitle());
        web.setDescription(entity.getText());
        web.setThumb(entity.getImage());
        new ShareAction(activity)
                .withMedia(web)
                .setPlatform(platform.getPlatform())
                .setCallback(new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {
                        listener.onStart(PLATFORM_TYPE.getType(share_media));
                    }

                    @Override
                    public void onResult(SHARE_MEDIA share_media) {
                        listener.onResult(PLATFORM_TYPE.getType(share_media));
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                        listener.onError(PLATFORM_TYPE.getType(share_media), throwable);
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media) {
                        listener.onCancel(PLATFORM_TYPE.getType(share_media));
                    }
                })
                .share();
    }

    public interface ShareListener {
        void onStart(PLATFORM_TYPE type);

        void onResult(PLATFORM_TYPE type);

        void onError(PLATFORM_TYPE type, Throwable throwable);

        void onCancel(PLATFORM_TYPE type);
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.d("plat", "platform" + platform);
            Toast.makeText(mContext, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(mContext, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            Log.d("throw", "throw:" + (t != null ? t.getMessage() : "t==null"));
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(mContext, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

    //分享微图到朋友圈
    public void shareToTimeLines(Context context, @NonNull File parentFile, List<String> list, String title) {
        if (list == null) {
            Toast.makeText(context.getApplicationContext(), "未发现图片资源", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent();
        ComponentName comp = new ComponentName("com.tencent.mm",
                "com.tencent.mm.ui.tools.ShareToTimeLineUI");
        intent.setComponent(comp);
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
        intent.setType("image/*");
        intent.putExtra("Kdescription", title);

        ArrayList<Uri> uris = new ArrayList<>();
        for (String path : list) {
            File file = new File(parentFile, path);
            Uri data = Uri.fromFile(file);
            uris.add(data);
        }

        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void shareToQzone(final Activity context, ArrayList<String> imgUrls, String title, final ShareQzoneListener listener) {
        String appid = ((PlatformConfig.APPIDPlatform) PlatformConfig.getPlatform(SHARE_MEDIA.QZONE)).appId;
        final Tencent mTencent = Tencent.createInstance(appid, context);
        final Bundle params = new Bundle();
        params.putInt(QzonePublish.PUBLISH_TO_QZONE_KEY_TYPE, QzonePublish.PUBLISH_TO_QZONE_TYPE_PUBLISHMOOD);//类型
        params.putString(QzonePublish.PUBLISH_TO_QZONE_SUMMARY, title);//概要
        //下面这个必须加上  不然无法调动 qq空间
        params.putStringArrayList(QzonePublish.PUBLISH_TO_QZONE_IMAGE_URL, imgUrls);
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTencent.publishToQzone(context, params, new IUiListener() {
                    @Override
                    public void onComplete(Object o) {
                        Log.i(o.toString());
                        listener.onComplete(o);
                    }

                    @Override
                    public void onError(UiError uiError) {
                        Log.i(uiError.errorMessage);
                        listener.onError(uiError.errorMessage);
                    }

                    @Override
                    public void onCancel() {
                        listener.onCancel();
                    }
                });
            }
        });

    }

    public interface ShareQzoneListener {
        void onComplete(Object o);

        void onError(String message);

        void onCancel();
    }

}
