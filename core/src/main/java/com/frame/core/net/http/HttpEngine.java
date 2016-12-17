package com.frame.core.net.http;

import com.frame.core.interf.Engine;
import com.frame.core.util.TLog;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * HttpURLConnection引擎
 * Created by Administrator on 2016/1/11.
 */
public class HttpEngine implements Engine {

    private static final String TAG = HttpEngine.class.getSimpleName();
    private final static int TIMEOUT = 20000;
    private final static int TIMEOUT_SOCKET = 20000;

    private static HttpEngine httpEngine = null;
    private static CookieManager manager = new CookieManager();

    private HttpEngine() {
    }

    public static HttpEngine getInstance() {
        if (httpEngine == null) {
            httpEngine = new HttpEngine();
        }
        return httpEngine;
    }

    @Override
    public String post(String method, Map<String, String> paramsMap) throws IOException {
        String data = joinParams(paramsMap);
        return postHttp(method, data);
    }

    @Override
    public String post(String method, String[] paramKeys, String[] paramValues) throws IOException {
        String data = joinParams(paramKeys, paramValues);
        return postHttp(method, data);
    }

    public String postHttp(String url, String params) throws IOException {
        HttpURLConnection connection = getConnection(url);
        connection.setRequestProperty("Content-Length", String.valueOf(params.getBytes().length));
        connection.connect();
        OutputStream os = connection.getOutputStream();
        os.write(params.getBytes());
        os.flush();
        TLog.i(TAG, connection.getResponseCode() + "");
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            // 获取响应的输入流对象
            InputStream is = connection.getInputStream();
            // 创建字节输出流对象
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // 定义读取的长度
            int len = 0;
            // 定义缓冲区
            byte buffer[] = new byte[1024];
            // 按照缓冲区的大小，循环读取
            while ((len = is.read(buffer)) != -1) {
                // 根据读取的长度写入到os对象中
                baos.write(buffer, 0, len);
            }
            // 释放资源
            is.close();
            baos.close();
            connection.disconnect();
            // 返回字符串
            final String result = new String(baos.toByteArray());
            TLog.i(TAG, result);
            return result;
        } else {
            connection.disconnect();
            return null;
        }
    }

    private HttpURLConnection getConnection(String strUrl) {
        HttpURLConnection connection = null;
        // 初始化connection
        try {
            // 根据地址创建URL对象
            URL url = new URL(strUrl);
            // 根据URL对象打开链接
            connection = (HttpURLConnection) url.openConnection();
            // 设置请求的方式
            connection.setRequestMethod(METHOD_POST);
            // 发送POST请求必须设置允许输入，默认为true
            connection.setDoInput(true);
            // 发送POST请求必须设置允许输出
            connection.setDoOutput(true);
            // 设置不使用缓存
            connection.setUseCaches(false);
            // 设置请求的超时时间
            connection.setReadTimeout(TIMEOUT);
            connection.setConnectTimeout(TIMEOUT_SOCKET);
            connection.setRequestProperty(CONTENT_TYPE_LABEL, "application/x-www-form-urlencoded");
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("Response-Type", "json");
            connection.setChunkedStreamingMode(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void storecoo(URI uri, String strcoo) {

        // 创建一个默认的 CookieManager

        // 将规则改掉，接受所有的 Cookie
        manager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

        // 保存这个定制的 CookieManager
        CookieHandler.setDefault(manager);

        // 接受 HTTP 请求的时候，得到和保存新的 Cookie
        HttpCookie cookie = new HttpCookie("Cookie: ", strcoo);
        //cookie.setMaxAge(60000);//没这个也行。
        manager.getCookieStore().add(uri, cookie);
    }

    public static HttpCookie getcookies() {

        HttpCookie res = null;
        // 使用 Cookie 的时候：
        // 取出 CookieStore
        CookieStore store = manager.getCookieStore();

        // 得到所有的 URI
        List<URI> uris = store.getURIs();
        for (URI ur : uris) {
            // 筛选需要的 URI
            // 得到属于这个 URI 的所有 Cookie
            List<HttpCookie> cookies = store.get(ur);
            for (HttpCookie coo : cookies) {
                res = coo;
            }
        }
        return res;
    }

    private String joinParams(String[] paramsKeys, String[] paramsValues) throws IOException {
        if (paramsKeys == null || paramsValues == null) return "";
        if (paramsKeys.length != paramsValues.length) throw new IOException("参数长度不匹配");
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < paramsKeys.length; i++) {
            stringBuilder.append(paramsKeys[i]);
            stringBuilder.append("=");
            try {
                stringBuilder.append(URLEncoder.encode(paramsValues[i], CHARSET_NAME));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            stringBuilder.append("&");
        }
        return stringBuilder.substring(0, stringBuilder.length() - 1);
    }

    private String joinParams(Map<String, String> paramsMap) {
        if (paramsMap == null) return "";
        StringBuilder stringBuilder = new StringBuilder();
        for (String key : paramsMap.keySet()) {
            stringBuilder.append(key);
            stringBuilder.append("=");
            try {
                stringBuilder.append(URLEncoder.encode(paramsMap.get(key), CHARSET_NAME));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            stringBuilder.append("&");
        }
        return stringBuilder.substring(0, stringBuilder.length() - 1);
    }
}
