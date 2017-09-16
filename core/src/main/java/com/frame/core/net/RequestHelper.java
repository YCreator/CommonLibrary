package com.frame.core.net;

import com.frame.core.interf.http.Field;
import com.frame.core.interf.http.FieldMap;
import com.frame.core.net.okhttp.OkHttpEngine;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static com.frame.core.interf.Engine.CONTENT_TYPE_LABEL;
import static com.frame.core.interf.Engine.CONTENT_TYPE_VALUE_JSON;

/**
 * Created by yzd on 2017/9/15 0015.
 */

public final class RequestHelper {

    final HttpUrl mBaseUrl;
    final OkHttpClient okHttpClient;
    final Converter.Factory converterFactory;
    final Map<Method, MethodService> serviceMethodCache = new ConcurrentHashMap<>();

    private RequestHelper(HttpUrl mBaseUrl, OkHttpClient okHttpClient, Converter.Factory converterFactory) {
        this.mBaseUrl = mBaseUrl;
        this.okHttpClient = okHttpClient;
        this.converterFactory = converterFactory;
    }

    @SuppressWarnings("unchecked")
    public <T> T create(final Class<T> service) {
        if (!service.isInterface()) {
            throw new IllegalArgumentException("API declarations must be interfaces.");
        }
        if (service.getInterfaces().length > 0) {
            throw new IllegalArgumentException("API interfaces must not extend other interfaces.");
        }
        return (T) Proxy.newProxyInstance(service.getClassLoader(), new Class<?>[]{service}
                , new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        MethodService methodService = loadServiceMethod(method);
                        Request request = methodService.toRequest(args);
                        return okHttpClient.newCall(request);
                    }
                });
    }

    private MethodService loadServiceMethod(Method method) {
        MethodService result = serviceMethodCache.get(method);
        if (result != null) return result;
        synchronized (serviceMethodCache) {
            result = serviceMethodCache.get(method);
            if (result == null) {
                result = new MethodService.Builder(method, this).build();
                serviceMethodCache.put(method, result);
            }
        }
        return result;
    }

    private Request createPostRequest(String method, RequestBody paramRequestParams) {
        return new Request.Builder()
                .url(mBaseUrl.resolve(method))
                .addHeader(CONTENT_TYPE_LABEL, CONTENT_TYPE_VALUE_JSON)
                .post(paramRequestParams)
                .build();
    }

    private Request crateGetRequest(String method) {
        return new Request.Builder()
                .url(mBaseUrl.newBuilder(method).build())
                .get()
                .build();
    }

    @SuppressWarnings("unchecked")
    private RequestBody createRequestBody(Annotation[][] annotations, Object[] args) {
        FormBody.Builder body = new FormBody.Builder();
        if (annotations.length > 0) {
            for (int i = 0; i < annotations.length; i++) {
                Annotation[] annotations1 = annotations[i];
                for (Annotation annotation : annotations1) {
                    if (annotation instanceof Field) {
                        Field field = (Field) annotation;
                        body.add(field.value(), args[i].toString());
                    } else if (annotation instanceof FieldMap) {
                        Map<String, String> params = (Map<String, String>) args[i];
                        return OkHttpEngine.getInstance().joinParams(params);
                    }
                }
            }
        }
        return body.build();
    }

    public static final class Builder {
        private HttpUrl mBaseUrl;
        private OkHttpClient okHttpClient;
        private Converter.Factory converterFactory;

        public Builder client(OkHttpClient client) {
            this.okHttpClient = client;
            return this;
        }

        public Builder baseUrl(String baseUrl) {
            mBaseUrl = HttpUrl.parse(baseUrl);
            return this;
        }

        public RequestHelper build() {
            return new RequestHelper(mBaseUrl, okHttpClient, new BuiltConverters());
        }
    }
}
