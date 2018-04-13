package com.frame.core.net.Retrofit;

import android.os.Handler;
import android.os.Looper;

import com.frame.core.entity.JsonEntity;
import com.frame.core.crash.exception.InstanceFactoryException;
import com.frame.core.gson.GsonFactory;
import com.frame.core.interf.Mapper;
import com.frame.core.rx.Lifeful;
import com.frame.core.rx.LifefulRunnable;
import com.frame.core.util.MapperFactory;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 远程服务器数据回调
 * Created by Administrator on 2015/10/16.
 */
public final class RespCallback<E extends JsonEntity> implements Callback {

    private static CustomHandler handler;

    private Mapper mapper;
    private CallbackListener listener;

    private RespCallback(Builder<E> builder) {
        listener = builder.listener;
        if (listener == null) {
            listener = new CallbackListener() {
                @Override
                public void onStart() {

                }

                @Override
                public void onSuccess(Object data, String body) {

                }

                @Override
                public void onError(int code, String message) {

                }

                @Override
                public void onComplete() {

                }

                @Override
                public Lifeful getLifeful() {
                    return null;
                }

                @Override
                public boolean inUIThread() {
                    return false;
                }

                @Override
                public Class getBeanClass() {
                    return null;
                }

            };
        }
        if (builder.mapperClazz != null) {
            try {
                this.mapper = MapperFactory.getInstance().achieveObj(builder.mapperClazz);
            } catch (InstanceFactoryException e) {
                callBackError(-1, "mapper inteface error");
            }
        }
        listener.onStart();
    }


    @Override
    public void onFailure(Call call, IOException e) {
        callBackError(-1, e.getMessage());
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        if (response.isSuccessful()) {
            Gson gson = GsonFactory.getGson();
            JsonReader jsonReader = gson.newJsonReader(response.body().charStream());
            TypeAdapter<?> adapter = gson.getAdapter(new TypeToken<E>() {
            });
            try {
                E entity = (E) adapter.read(jsonReader);
                analyzeBody(entity, response.body().string());
            } finally {
                response.body().close();
            }
        } else {
            callBackError(response.code(), response.message());
        }
    }

    @SuppressWarnings("unchecked")
    private void analyzeBody(E body, String respBody) {
        if (body != null) {
            if (body.isSuccess()) {
                try {
                    if (body instanceof JsonEntity.ArrayData) {
                        callBackSuccess(mapper.transformEntityCollection(((JsonEntity.ArrayData) body).getArrayData()), respBody);
                    } else if (body instanceof JsonEntity.Data) {
                        callBackSuccess(mapper.transformEntity(((JsonEntity.Data) body).getData()), respBody);
                    } else {
                        callBackSuccess(null, respBody);
                    }
                } catch (Exception e) {
                    callBackError(-2, e.getMessage());
                }
            } else {
                callBackError(body.getCode(), body.getMessage());
            }
        } else {
            callBackError(-2, "body == null");
        }
    }

    @SuppressWarnings("unchecked")
    private void callBackSuccess(Object data, String body) {
        if (listener.inUIThread()) {
            Runnable runnable = () -> {
                listener.onSuccess(data, body);
                listener.onComplete();
            };
            getHandler().post(new LifefulRunnable(runnable, listener.getLifeful()));
        } else {
            listener.onSuccess(data, body);
            listener.onComplete();
        }
    }

    private void callBackError(int code, String message) {
        if (listener.inUIThread()) {
            Runnable runnable = () -> {
                listener.onError(code, message);
                listener.onComplete();
            };
            getHandler().post(new LifefulRunnable(runnable, listener.getLifeful()));
        } else {
            listener.onError(code, message);
            listener.onComplete();
        }
    }

    private static class CustomHandler extends Handler {
        private CustomHandler() {
            super(Looper.getMainLooper());
        }
    }

    private static Handler getHandler() {
        synchronized (RespCallback.class) {
            if (handler == null) {
                handler = new CustomHandler();
            }
            return handler;
        }
    }

    public interface CallbackListener<T> {

        void onStart();

        void onSuccess(T data, String body);

        void onError(int code, String message);

        void onComplete();

        Lifeful getLifeful();

        boolean inUIThread();

        Class<T> getBeanClass();
    }

    public static final class Builder<E extends JsonEntity> {

        private CallbackListener listener;
        private Class<? extends Mapper> mapperClazz;

        public Builder<E> setListener(CallbackListener callback) {
            this.listener = callback;
            return this;
        }

        public Builder<E> setMapper(Class<? extends Mapper> mapperClazz) {
            this.mapperClazz = mapperClazz;
            return this;
        }

        public RespCallback<E> build() {
            return new RespCallback<>(this);
        }
    }
}


