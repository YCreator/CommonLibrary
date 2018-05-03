package com.frame.core.base;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.frame.core.R;
import com.frame.core.entity.JsObjectAndNameEntity;
import com.frame.core.util.StringUtils;
import com.frame.core.util.TLog;
import com.frame.core.util.ToastUtil;
import com.frame.core.util.utils.AppUtils;
import com.frame.core.util.utils.IntentUtils;

/**
 * Created by yzd on 2017/9/18 0018.
 */

public abstract class BaseWebViewActivity extends BaseAppCompatActivity {

    private WebView webView;
    private TextView tvTitle;
    private ProgressBar pb;

    public Dialog dialog;

    @SuppressLint({"AddJavascriptInterface", "SetJavaScriptEnabled", "JavascriptInterface"})
    @Override
    public void initPageView() {
        webView = findViewById(R.id.webview);
        tvTitle = findViewById(R.id.title);
        pb = findViewById(R.id.pb);
        setTitle("");
        getActionBarToolbar().setNavigationOnClickListener(v -> {
            if (webView.canGoBack()) {
                webView.goBack();
            } else {
                finishPage();
            }
        });

        webView.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {  //表示按返回键
                    webView.goBack();
                    return true;
                }
            }
            return false;
        });
        tvTitle.setLines(1);
        pb.setMax(100);
        webView.getSettings().setDomStorageEnabled(true);//是否开启本地DOM存储  鉴于它的安全特性（任何人都能读取到它，尽管有相应的限制，将敏感数据存储在这里依然不是明智之举），Android 默认是关闭该功能的。
        webView.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        webView.getSettings().setAppCachePath(appCachePath);
        webView.getSettings().setAppCacheEnabled(true);
        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webView.getSettings().setJavaScriptEnabled(true);

        //缩放操作
        webView.getSettings().setSupportZoom(true);             //支持缩放，默认为true。是下面那个的前提。
        webView.getSettings().setBuiltInZoomControls(true);     //设置内置的缩放控件。若为false，则该WebView不可缩放
        webView.getSettings().setDisplayZoomControls(false);    //隐藏原生的缩放控件

        //设置自适应屏幕，两者合用
        webView.getSettings().setUseWideViewPort(true);         //将图片调整到适合webview的大小
        webView.getSettings().setLoadWithOverviewMode(true);    // 缩放至屏幕的大小

        //其他细节操作
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);     //支持通过JS打开新窗口
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);  //关闭webview中缓存
        webView.getSettings().setLoadsImagesAutomatically(true);                  //支持自动加载图片
        webView.getSettings().setAllowFileAccess(true);                           //设置可以访问文件
        webView.getSettings().setDefaultTextEncodingName("UTF-8");                //设置编码格式
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); //自适应屏幕
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setSavePassword(true);//保存密码
        JsObjectAndNameEntity jsOb = inJavaScriptLocalObj();
        if (jsOb != null) {
            webView.addJavascriptInterface(jsOb.getObJs(), jsOb.getName());
        }
        //Android webview 从Lollipop(5.0)开始webview默认不允许混合模式，https当中不能加载http资源，需要设置开启
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webView.setSaveEnabled(true);
        webView.setKeepScreenOn(true);
        webView.setFocusableInTouchMode(true);
        webView.setFocusable(true);
        webView.requestFocus(R.id.webview);
        webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        //webView.setInitialScale(100);
        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (pb != null) {
                    pb.setProgress(newProgress);
                    if (newProgress > 90) {
                        pb.setVisibility(View.GONE);
                    }
                }
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (tvTitle != null) tvTitle.setText(title);
            }


        });

        webView.setWebViewClient(new MyWebViewClient());
        webView.setDownloadListener((url, userAgent, contentDisposition, mimetype, contentLength) -> {
            Intent intent = IntentUtils.getSystemWebIntent(url);
            startActivity(intent);
        });
        customWebSetting(webView);
        loadWeb(loadWeb());
    }

    @Override
    protected void onSaveInstanceState(Bundle paramBundle) {
        super.onSaveInstanceState(paramBundle);
        paramBundle.putString("url", webView.getUrl());
    }

    @Override
    public void initPageViewListener() {

    }

    @Override
    protected int initPageLayoutID() {
        return R.layout.activity_web;
    }

    /**
     * 对&nbsp;|&quot;|&amp;|&lt;|&gt;等html字符转义
     *
     * @param htmlData
     */
    private String formatHtmlData(String htmlData) {
        htmlData = htmlData.replaceAll("&nbsp;", " ");
        htmlData = htmlData.replaceAll("&amp;", "&");
        htmlData = htmlData.replaceAll("&quot;", "\"");
        htmlData = htmlData.replaceAll("&lt;", "<");
        htmlData = htmlData.replaceAll("&gt;", ">");
        //去除所有width和height和style属性
        htmlData = htmlData.replaceAll("(<img[^>]*?)\\s+width\\s*=\\s*\\S+", "$1");
        htmlData = htmlData.replaceAll("(<img[^>]*?)\\s+height\\s*=\\s*\\S+", "$1");
        htmlData = htmlData.replaceAll("(<img[^>]*?)\\s+style\\s*=\\s*\\S+", "$1");
        htmlData = htmlData.replaceAll("<img ", "<img width=\"100%\" ");
        return htmlData;
    }

    public void customWebSetting(WebView webView) {

    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.stopLoading();
            webView.clearCache(true);
        }
        super.onDestroy();
        if (dialog != null && dialog.isShowing()) dialog.dismiss();
        dialog = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_function, menu);
        menu.findItem(R.id.action_filter).setTitle(null);
//        menu.findItem(R.id.action_filter).setIcon(R.drawable.close);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (webView.canGoBack()) {
            menu.findItem(R.id.action_filter).setIcon(R.drawable.close);
        } else {
            menu.findItem(R.id.action_filter).setIcon(null);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_filter) {
            finishPage();
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * 加载内容
     */
    protected abstract String loadWeb();

    /**
     * js与原生交互接口
     */
    protected abstract JsObjectAndNameEntity inJavaScriptLocalObj();

    private final class MyWebViewClient extends WebViewClient {

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("taobao://") || url.startsWith("tbopen://")) {
                if (AppUtils.isAppInstalled("com.taobao.taobao")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                return true;
            } else if (url.startsWith("tel:")) {
                if (ActivityCompat.checkSelfPermission(BaseWebViewActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ToastUtil.showLong(BaseWebViewActivity.this, "拨打电话权限未开启");
                    view.goBack();
                    return false;
                }
                Intent intent = IntentUtils.getCallIntent(url.replace("tel:", ""));
                startActivity(intent);
                return true;
            }
            if (url.startsWith("http:") || url.startsWith("https:")) {
                view.loadUrl(url);   //此方法始终在一同一个webView 中显示
                return false;
            }
            try {
                Intent intent = IntentUtils.getSystemWebIntent(url);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            TLog.i("shouldOverrideUrlLoading"
                    , request.getMethod()
                    , request.getUrl().getHost()
                    , request.getUrl().getPath()
                    , request.getUrl().getPort()
                    , request.getUrl().getScheme());

//            if (webView.canGoBack()) {
//                //重新绘制
//                invalidateOptionsMenu();
//            }
            return super.shouldOverrideUrlLoading(view, request);
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (pb != null) {
                pb.setVisibility(View.VISIBLE);
                pb.setProgress(0);
            }
            //重新绘制
            invalidateOptionsMenu();
//            LogUtils.e(" onPageStarted webView.canGoBack() ",webView.canGoBack());
            super.onPageStarted(view, url, favicon);
        }

        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (!view.getSettings().getLoadsImagesAutomatically()) {
                view.getSettings().setLoadsImagesAutomatically(true);
            }
            // view.loadUrl("javascript:window.app.showSource(document.getElementsByTagName('html')[0].innerHTML)");
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            if (errorCode == WebViewClient.ERROR_HOST_LOOKUP) {
                view.loadUrl("file:///android_asset/error.htm?url=" + failingUrl);
            }
            TLog.i(TAG, "-MyWebViewClient->onReceivedError()--\n errorCode=" + errorCode + " \ndescription=" + description + " \nfailingUrl=" + failingUrl);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            //解决Android升级到7.0使得webview加载https页面为空白页
            if (error.getPrimaryError() == SslError.SSL_DATE_INVALID
                    || error.getPrimaryError() == SslError.SSL_EXPIRED
                    || error.getPrimaryError() == SslError.SSL_INVALID
                    || error.getPrimaryError() == SslError.SSL_UNTRUSTED) {
                handler.proceed();
            } else {
                handler.cancel();
            }
        }

    }

    public void loadWeb(String body) {
        if (!StringUtils.isEmpty(body)) {
            if (body.indexOf("http:") == 0 || body.indexOf("https:") == 0 || body.indexOf("file:") == 0 || body.indexOf("javascript:") == 0) {
                webView.loadUrl(body);
            } else {
                webView.loadDataWithBaseURL("", formatHtmlData(body), "text/html", "UTF-8", null);
            }
        }
    }

}
