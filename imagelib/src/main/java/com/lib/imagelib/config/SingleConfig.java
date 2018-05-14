package com.lib.imagelib.config;


import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;

import com.lib.imagelib.loader.ImageListener;
import com.lib.imagelib.utils.ImageUtil;

import java.io.File;

/**
 * Created by yzd on 2018/3/17 0017.
 */

public class SingleConfig {
    private Context context;
    private boolean ignoreCertificateVerify;    //https是否忽略校验
    private String url;

    private float thumbnail;                    //缩略图缩放倍数
    private String filePath;                    //文件路径

    private File file;                          //文件路径
    private int resId;                          //资源id
    private String rawPath;                     //raw路径
    private String assertspath;                 //asserts路径
    private String contentProvider;             //内容提供者
    private boolean isGif;                      //是否是GIF图
    private View target;
    private int width;
    private int height;
    private int oWidth;
    private int oHeight;

    //滤镜
    private boolean isNeedVignette;             //是否需要晕映
    private boolean isNeedSketch;               //是否需要素描
    private float pixelationLevel;              //是否需要马赛克
    private boolean isNeedPixelation;           //是否需要马赛克
    private boolean isNeedInvert;               //是否需要胶片
    public float contrastLevel;                 //锐化等级
    private boolean isNeedContrast;             //是否需要锐化
    private boolean isNeedSepia;                //是否需要墨画
    private boolean isNeedToon;                 //是否需要油画
    private boolean isNeedSwirl;                // 是否需要漩涡
    private boolean isNeedGrayscale;            //是否需要黑色
    private boolean isNeedBrightness;           //是否需要亮度
    private float brightnessLeve;               //是否需要亮度
    private boolean needBlur;                   //是否需要模糊
    private boolean needFilteColor;             //是否需要模糊
    private int filteColor;

    private int priority;

    private int animationType;
    private int animationId;
    private Animation animation;

    private int blurRadius;
    private int placeHolderResId;
    private int errorResId;

    private int shapeMode;                      //默认矩形,可选直角矩形,圆形/椭圆
    private int rectRoundRadius;                //圆角矩形时圆角的半径
    private int diskCacheMode;                  //是否跳过磁盘存储
    private int scaleMode;                      //填充模式,默认centercrop,可选fitXY,centerInside...

    private int roundOverlayColor;//圆角/圆外覆盖一层背景色
    private int borderWidth;//边框的宽度
    private int borderColor;//边框颜色
    private boolean isUseARGB8888;
    private boolean reuseable;
    private int placeHolderScaleType;
    private int errorScaleType;
    private int loadingScaleType;
    private int loadingResId;

    private ImageListener imageListener;


    public int getPlaceHolderScaleType() {
        return placeHolderScaleType;
    }

    public int getErrorScaleType() {
        return errorScaleType;
    }

    public int getLoadingResId() {
        return loadingResId;
    }

    private BitmapListener bitmapListener;

    public SingleConfig(ConfigBuilder builder) {
        this.url = builder.url;
        this.thumbnail = builder.thumbnail;
        this.filePath = builder.filePath;
        this.file = builder.file;
        this.resId = builder.resId;
        this.rawPath = builder.rawPath;
        this.assertspath = builder.assertspath;
        this.contentProvider = builder.contentProvider;

        this.ignoreCertificateVerify = builder.ignoreCertificateVerify;

        this.target = builder.target;

        this.width = builder.width;
        this.height = builder.height;

        this.oWidth = builder.oWidth;
        this.oHeight = builder.oHeight;

        this.shapeMode = builder.shapeMode;
        if (shapeMode == ShapeMode.RECT_ROUND) {
            this.rectRoundRadius = builder.rectRoundRadius;
        }
        this.scaleMode = builder.scaleMode;

        this.diskCacheMode = builder.diskCacheMode;

        this.animationId = builder.animationId;
        this.animationType = builder.animationType;
        this.animation = builder.animation;

        this.roundOverlayColor = builder.roundOverlayColor;
        this.borderColor = builder.borderColor;
        this.borderWidth = builder.borderWidth;

        this.priority = builder.priority;
        //滤镜
        this.isNeedVignette = builder.isNeedVignette; //是否需要晕映
        this.isNeedSketch = builder.isNeedSketch; //是否需要素描
        this.pixelationLevel = builder.pixelationLevel; //是否需要马赛克
        this.isNeedPixelation = builder.isNeedPixelation; //是否需要马赛克
        this.isNeedInvert = builder.isNeedInvert; //是否需要胶片
        this.contrastLevel = builder.contrastLevel; //锐化等级
        this.isNeedContrast = builder.isNeedContrast; //是否需要锐化
        this.isNeedSepia = builder.isNeedSepia; //是否需要亮度
        this.isNeedToon = builder.isNeedToon; //是否需要亮度
        this.isNeedSwirl = builder.isNeedSwirl; //是否需要亮度
        this.isNeedGrayscale = builder.isNeedGrayscale; //是否需要黑色
        this.isNeedBrightness = builder.isNeedBrightness; //是否需要亮度
        this.brightnessLeve = builder.brightnessLeve; //是否需要亮度
        this.filteColor = builder.filteColor;
        this.needBlur = builder.needBlur;
        this.needFilteColor = builder.needFilteColor;
        this.placeHolderResId = builder.placeHolderResId;

        this.asBitmap = builder.asBitmap;
        this.bitmapListener = builder.bitmapListener;
        this.isGif = builder.isGif;
        this.blurRadius = builder.blurRadius;
        this.errorResId = builder.errorResId;

        this.isUseARGB8888 = builder.isUseARGB8888;
        this.reuseable = builder.reuseable;
        this.placeHolderScaleType = builder.placeHolderScaleType;
        this.errorScaleType = builder.errorScaleType;
        this.loadingScaleType = builder.loadingScaleType;
        this.loadingResId = builder.loadingResId;
        this.imageListener = builder.imageListener;
    }

    public boolean isAsBitmap() {
        return asBitmap;
    }

    private boolean asBitmap;//只获取bitmap

    public Context getContext() {
        if (context == null) {
            context = GlobalConfig.context;
        }
        return context;
    }

    public ImageListener getImageListener() {
        return imageListener;
    }

    public boolean isUseARGB8888() {
        return isUseARGB8888;
    }

    public int getLoadingScaleType() {
        return loadingScaleType;
    }

    public int getDiskCacheMode() {
        return diskCacheMode;
    }

    public int getErrorResId() {
        return errorResId;
    }

    public String getContentProvider() {
        return contentProvider;
    }

    public String getFilePath() {
        return filePath;
    }

    public File getFile() {
        return file;
    }

    public boolean isNeedBlur() {
        return needBlur;
    }

    public int getPlaceHolderResId() {
        return placeHolderResId;
    }

    public int getRectRoundRadius() {
        return rectRoundRadius;
    }

    public int getResId() {
        return resId;
    }

    public String getRawPath() {
        return rawPath;
    }

    public String getAssertspath() {
        return assertspath;
    }

    public int getScaleMode() {
        return scaleMode;
    }

    public int getShapeMode() {
        return shapeMode;
    }

    public View getTarget() {
        return target;
    }

    public String getUrl() {
        return url;
    }

    public int getHeight() {
        if (height <= 0) {
            //先去imageview里取,如果为0,则赋值成matchparent
            if (target != null) {
                height = target.getMeasuredWidth();
            }
            if (height <= 0) {
                height = GlobalConfig.getWinHeight();
            }
        }
        return height;
    }

    public int getWidth() {
        if (width <= 0) {
            //先去imageview里取,如果为0,则赋值成matchparent
            if (target != null) {
                width = target.getMeasuredWidth();
            }
            if (width <= 0) {
                width = GlobalConfig.getWinWidth();
            }
        }

        return width;
    }

    public int getRoundOverlayColor() {
        return roundOverlayColor;
    }

    public int getBorderWidth() {
        return borderWidth;
    }

    public int getBorderColor() {
        return borderColor;
    }

    public int getoWidth() {
        return oWidth;
    }

    public int getoHeight() {
        return oHeight;
    }

    public int getAnimationType() {
        return animationType;
    }

    public int getAnimationId() {
        return animationId;
    }

    public Animation getAnimation() {
        return animation;
    }

    public int getPriority() {
        return priority;
    }

    public int getFilteColor() {
        return filteColor;
    }

    public float getContrastLevel() {
        return contrastLevel;
    }

    public boolean isNeedFilteColor() {
        return needFilteColor;
    }

    public float getBrightnessLeve() {
        return brightnessLeve;
    }

    public boolean isNeedBrightness() {
        return isNeedBrightness;
    }

    public boolean isIgnoreCertificateVerify() {
        return ignoreCertificateVerify;
    }

    public BitmapListener getBitmapListener() {

        return bitmapListener;
    }

    public float getThumbnail() {
        return thumbnail;
    }

    public void setBitmapListener(BitmapListener bitmapListener) {
        this.bitmapListener = ImageUtil.getBitmapListenerProxy(bitmapListener);
    }

    private void show() {
        GlobalConfig.getLoader().request(this);
    }

    public boolean isGif() {
        return isGif;
    }

    public int getBlurRadius() {
        return blurRadius;
    }

    public boolean isNeedGrayscale() {
        return isNeedGrayscale;
    }

    public boolean isNeedSwirl() {
        return isNeedSwirl;
    }

    public boolean isNeedToon() {
        return isNeedToon;
    }

    public boolean isNeedSepia() {
        return isNeedSepia;
    }

    public boolean isNeedContrast() {
        return isNeedContrast;
    }

    public boolean isNeedInvert() {
        return isNeedInvert;
    }

    public boolean isNeedPixelation() {
        return isNeedPixelation;
    }

    public float getPixelationLevel() {
        return pixelationLevel;
    }

    public boolean isNeedSketch() {
        return isNeedSketch;
    }

    public boolean isNeedVignette() {
        return isNeedVignette;
    }

    public interface BitmapListener {
        void onSuccess(Bitmap bitmap);

        void onFail();
    }

    public static class ConfigBuilder {
        private Context context;

        private boolean ignoreCertificateVerify = GlobalConfig.ignoreCertificateVerify;

        /**
         * 图片源
         * 类型	SCHEME	示例
         * 远程图片	http://, https://	HttpURLConnection 或者参考 使用其他网络加载方案
         * 本地文件	file://	FileInputStream
         * Content provider	content://	ContentResolver
         * asset目录下的资源	asset://	AssetManager
         * res目录下的资源	  res://	Resources.openRawResource
         * Uri中指定图片数据	data:mime/type;base64,	数据类型必须符合 rfc2397规定 (仅支持 UTF-8)
         *
         * @param config
         * @return
         */
        private String url;
        private float thumbnail;
        private String filePath;
        private File file;
        private int resId;
        private String rawPath;
        private String assertspath;
        private String contentProvider;
        private boolean isGif = false;

        private View target;
        private boolean asBitmap;//只获取bitmap
        private BitmapListener bitmapListener;

        // TODO: 2017/4/24 宽高的获取
        private int width;
        private int height;

        private int oWidth; //选择加载分辨率的宽
        private int oHeight; //选择加载分辨率的高

        //滤镜
        private boolean isNeedVignette; //是否需要晕映
        private boolean isNeedSketch; //是否需要素描
        private float pixelationLevel; //是否需要马赛克
        private boolean isNeedPixelation; //是否需要马赛克
        private boolean isNeedInvert; //是否需要胶片
        private float contrastLevel; //是否需要墨画
        private boolean isNeedContrast = false; //是否需要墨画
        private boolean isNeedSepia = false; //是否需要墨画
        private boolean isNeedToon = false; //是否需要油画
        private boolean isNeedSwirl = false; //是否需要漩涡
        private boolean isNeedGrayscale = false; //是否需要亮度
        private boolean isNeedBrightness = false; //是否需要亮度
        private float brightnessLeve; //亮度等级
        private boolean needBlur = false;//是否需要模糊
        private boolean needFilteColor = false;//是否需要滤镜颜色
        private int blurRadius;

        //UI:
        private int placeHolderResId;

        private int errorResId;

        private int shapeMode;//默认矩形,可选直角矩形,圆形/椭圆
        private int rectRoundRadius;//圆角矩形时圆角的半径

        private int diskCacheMode;

        private int scaleMode;//填充模式,默认centercrop,可选fitXY,centerInside...

        private int priority; //请求优先级

        private int filteColor; //滤镜颜色

        public int animationId; //动画资源id
        public int animationType; //动画资源Type
        public Animation animation; //动画资源

        private int roundOverlayColor;//圆角/圆外覆盖一层背景色
        private int borderWidth;//边框的宽度
        private int borderColor;//边框颜色

        private boolean isUseARGB8888;
        private boolean reuseable;
        private int placeHolderScaleType;
        private int errorScaleType;
        private int loadingScaleType;
        private int loadingResId;

        private ImageListener imageListener;

        public ConfigBuilder(Context context) {
            this.context = context;
        }

        public ConfigBuilder imageListener(ImageListener imageListener) {
            this.imageListener = imageListener;
            return this;
        }

        public ConfigBuilder isUseARGB8888(boolean isUseARGB8888) {
            this.isUseARGB8888 = isUseARGB8888;
            return this;
        }

        public ConfigBuilder reuseable(boolean reuseable) {
            this.reuseable = reuseable;
            return this;
        }

        public ConfigBuilder placeHolderScaleType(int placeHolderScaleType) {
            this.placeHolderScaleType = placeHolderScaleType;
            return this;
        }

        public ConfigBuilder errorScaleType(int errorScaleType) {
            this.errorScaleType = errorScaleType;
            return this;
        }

        public ConfigBuilder loadingScaleType(int loadingScaleType) {
            this.loadingScaleType = loadingScaleType;
            return this;
        }

        public ConfigBuilder loadingResId(int loadingResId) {
            this.loadingResId = loadingResId;
            return this;
        }

        public ConfigBuilder roundOverlayColor(int roundOverlayColor) {
            this.roundOverlayColor = roundOverlayColor;
            return this;
        }

        public ConfigBuilder borderWidth(int borderWidth) {
            this.borderWidth = borderWidth;
            return this;
        }

        public ConfigBuilder borderColor(int borderColor) {
            this.borderColor = borderColor;
            return this;
        }

        public ConfigBuilder ignoreCertificateVerify(boolean ignoreCertificateVerify) {
            this.ignoreCertificateVerify = ignoreCertificateVerify;
            return this;
        }

        /**
         * 缩略图
         *
         * @param thumbnail
         * @return
         */
        public ConfigBuilder thumbnail(float thumbnail) {
            this.thumbnail = thumbnail;
            return this;
        }

        /**
         * error图
         *
         * @param errorResId
         * @return
         */
        public ConfigBuilder error(int errorResId) {
            this.errorResId = errorResId;
            return this;
        }

        /**
         * 设置网络路径
         *
         * @param url
         * @return
         */
        public ConfigBuilder url(String url) {
            this.url = url;
            if (url.contains("gif")) {
                isGif = true;
            }
            return this;
        }

        /**
         * 加载SD卡资源
         *
         * @param filePath
         * @return
         */
        public ConfigBuilder file(String filePath) {
            if (filePath.startsWith("file:")) {
                this.filePath = filePath;
                return this;
            }

            if (!new File(filePath).exists()) {
                //throw new RuntimeException("文件不存在");
                Log.e("imageloader", "文件不存在");
                return this;
            }

            this.filePath = filePath;
            if (filePath.contains("gif")) {
                isGif = true;
            }
            return this;
        }

        /**
         * 加载SD卡资源
         *
         * @param file
         * @return
         */
        public ConfigBuilder file(File file) {
            this.file = file;

            return this;
        }

        /**
         * 加载drawable资源
         *
         * @param resId
         * @return
         */
        public ConfigBuilder res(int resId) {
            this.resId = resId;
            return this;
        }

        /**
         * 加载ContentProvider资源
         *
         * @param contentProvider
         * @return
         */
        public ConfigBuilder content(String contentProvider) {
            if (contentProvider.startsWith("content:")) {
                this.contentProvider = contentProvider;
                return this;
            }

            if (contentProvider.contains("gif")) {
                isGif = true;
            }

            return this;
        }

        /**
         * 加载raw资源
         *
         * @param rawPath
         * @return
         */
        public ConfigBuilder raw(String rawPath) {

            this.rawPath = rawPath;

            if (rawPath.contains("gif")) {
                isGif = true;
            }

            return this;
        }

        /**
         * 加载asserts资源
         *
         * @param assertspath
         * @return
         */
        public ConfigBuilder asserts(String assertspath) {
            this.assertspath = assertspath;

            if (assertspath.contains("gif")) {
                isGif = true;
            }

            return this;
        }

        public void into(View targetView) {
            this.target = targetView;
            new SingleConfig(this).show();
        }

        public void asBitmap(BitmapListener bitmapListener) {
            this.bitmapListener = ImageUtil.getBitmapListenerProxy(bitmapListener);
            this.asBitmap = true;
            new SingleConfig(this).show();
        }

        /**
         * 加载图片的分辨率
         *
         * @param oWidth
         * @param oHeight
         * @return
         */
        public ConfigBuilder override(int oWidth, int oHeight) {
            this.oWidth = ImageUtil.dip2px(oWidth);
            this.oHeight = ImageUtil.dip2px(oHeight);
            return this;
        }

        /**
         * 占位图
         *
         * @param placeHolderResId
         * @return
         */
        public ConfigBuilder placeHolder(int placeHolderResId) {
            this.placeHolderResId = placeHolderResId;
            return this;
        }


        /**
         * 是否需要高斯模糊
         *
         * @return
         */
        public ConfigBuilder blur(int blurRadius) {
            this.needBlur = true;
            this.blurRadius = blurRadius;
            return this;
        }

        /**
         * 圆角
         *
         * @return
         */
        public ConfigBuilder asCircle() {
            this.shapeMode = ShapeMode.OVAL;
            return this;
        }

        /**
         * 形状为圆角矩形时的圆角半径
         *
         * @param rectRoundRadius
         * @return
         */
        public ConfigBuilder rectRoundCorner(int rectRoundRadius) {
            this.rectRoundRadius = ImageUtil.dip2px(rectRoundRadius);
            this.shapeMode = ShapeMode.RECT_ROUND;
            return this;
        }


        /**
         * 正方形
         *
         * @return
         */
        public ConfigBuilder asSquare() {
            this.shapeMode = ShapeMode.SQUARE;
            return this;
        }


        /**
         * 磁盘缓存
         */
        public ConfigBuilder diskCacheMod(int diskCacheMode) {
            this.diskCacheMode = diskCacheMode;
            return this;
        }

        /**
         * 拉伸/裁剪模式
         *
         * @param scaleMode 取值ScaleMode
         * @return
         */
        public ConfigBuilder scale(int scaleMode) {
            this.scaleMode = scaleMode;
            return this;
        }


        public ConfigBuilder animate(int animationId) {
            this.animationType = AnimationMode.ANIMATIONID;
            this.animationId = animationId;
            return this;
        }

        public ConfigBuilder animate(Animation animation) {
            this.animationType = AnimationMode.ANIMATION;
            this.animation = animation;
            return this;
        }

        public ConfigBuilder priority(int priority) {
            this.priority = priority;

            return this;
        }

        public ConfigBuilder colorFilter(int filteColor) {
            this.filteColor = filteColor;
            this.needFilteColor = true;
            return this;
        }

        public ConfigBuilder brightnessFilter(float level) {
            this.isNeedBrightness = true;
            this.brightnessLeve = level;
            return this;
        }

        public ConfigBuilder grayscaleFilter() {
            this.isNeedGrayscale = true;
            return this;
        }

        public ConfigBuilder swirlFilter() {
            this.isNeedSwirl = true;
            return this;
        }

        public ConfigBuilder toonFilter() {
            this.isNeedToon = true;
            return this;
        }

        public ConfigBuilder sepiaFilter() {
            this.isNeedSepia = true;
            return this;
        }

        public ConfigBuilder contrastFilter(float constrasrLevel) {
            this.contrastLevel = constrasrLevel;
            this.isNeedContrast = true;
            return this;
        }

        public ConfigBuilder invertFilter() {
            this.isNeedInvert = true;
            return this;
        }

        public ConfigBuilder pixelationFilter(float pixelationLevel) {
            this.pixelationLevel = pixelationLevel;
            this.isNeedPixelation = true;
            return this;
        }

        public ConfigBuilder sketchFilter() {
            this.isNeedSketch = true;
            return this;
        }

        public ConfigBuilder vignetteFilter() {
            this.isNeedVignette = true;
            return this;
        }


    }

}
