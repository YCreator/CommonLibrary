package com.lib.tpl.share;

import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * Created by yzd on 2018/4/9 0009.
 */

public enum PLATFORM_TYPE {
    GOOGLEPLUS,
    GENERIC,
    SMS,
    EMAIL,
    SINA,
    QZONE,
    QQ,
    RENREN,
    WEIXIN,
    WEIXIN_CIRCLE,
    WEIXIN_FAVORITE,
    TENCENT,
    DOUBAN,
    FACEBOOK,
    FACEBOOK_MESSAGER,
    TWITTER,
    LAIWANG,
    LAIWANG_DYNAMIC,
    YIXIN,
    YIXIN_CIRCLE,
    INSTAGRAM,
    PINTEREST,
    EVERNOTE,
    POCKET,
    LINKEDIN,
    FOURSQUARE,
    YNOTE,
    WHATSAPP,
    LINE,
    FLICKR,
    TUMBLR,
    ALIPAY,
    KAKAO,
    DROPBOX,
    VKONTAKTE,
    DINGTALK,
    MORE;

    public SHARE_MEDIA getPlatform() {
        switch (this) {
            case QQ:
                return SHARE_MEDIA.QQ;
            case QZONE:
                return SHARE_MEDIA.QZONE;
            case WEIXIN:
                return SHARE_MEDIA.WEIXIN;
            case WEIXIN_CIRCLE:
                return SHARE_MEDIA.WEIXIN_CIRCLE;
            case SINA:
                return SHARE_MEDIA.SINA;
            case WEIXIN_FAVORITE:
                return SHARE_MEDIA.WEIXIN_FAVORITE;
        }
        return SHARE_MEDIA.MORE;
    }

    public static PLATFORM_TYPE getType(SHARE_MEDIA media) {
        switch (media) {
            case QQ:
                return PLATFORM_TYPE.QQ;
            case QZONE:
                return PLATFORM_TYPE.QZONE;
            case WEIXIN:
                return PLATFORM_TYPE.WEIXIN;
            case WEIXIN_CIRCLE:
                return PLATFORM_TYPE.WEIXIN_CIRCLE;
            case SINA:
                return PLATFORM_TYPE.SINA;
        }
        return PLATFORM_TYPE.MORE;
    }
}
