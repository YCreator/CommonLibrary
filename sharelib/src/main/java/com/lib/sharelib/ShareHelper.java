package com.lib.sharelib;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
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
        PlatformConfig.setSinaWeibo(id, secret);
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
    public void share(Activity activity, ShareEntity entity, SHARE_MEDIA platform) {
        new ShareAction(activity)
                .setPlatform(platform)
                .withText(entity.getText())
                .withTitle(entity.getTitle())
                .withTargetUrl(entity.getTargetUrl())
                .withMedia(entity.getImage())
                .setCallback(umShareListener)
                .share();
    }

    private UMShareListener umShareListener = new UMShareListener() {
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
}
