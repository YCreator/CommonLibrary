package com.frame.core.net.okhttp;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.SparseArray;

import com.frame.core.entity.JsonEntity;
import com.frame.core.interf.Mapper;
import com.frame.core.util.EntityType;
import com.frame.core.util.TLog;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 远程服务器数据回调
 * Created by Administrator on 2015/10/16.
 */
public final class RepCallback implements Callback {

    private static final String TAG = RepCallback.class.getSimpleName();

    private static CustomHandler handler;
    public static final String ERROR_MSG_1 = "网络请求失败";
    public static final String ERROR_MSG_2 = "数据异常";
    public static final String ERROR_MSG_3 = "网络连接中断";
    public static final int ERROR_CODE_0 = 0; //服务器返回的错误类型
    public static final int ERROR_CODE_1 = 1; //网络异常返回的错误类型
    public static final int ERROR_CODE_2 = 2; //其他异常返回的错误类型
    private final SparseArray<Object> obj;
    private OkCallbackListener httpData;
    private Mapper mapper;
    private Class clazz;
    private Class<? extends JsonEntity> templateClazz;
    private EntityType type;

    private RepCallback() {
        obj = new SparseArray<>();
        httpData = new OkCallbackListener() {
            @Override
            public void onSuccess(Object data, String resBody) {

            }

            @Override
            public void onFailure(int errorCode, String msg) {

            }
        };
    }

    private RepCallback(@NonNull OkCallbackListener httpData
            ,@NonNull Class<? extends JsonEntity> templateClazz) {
        obj = new SparseArray<>();
        this.httpData = httpData;
        this.templateClazz = templateClazz;
    }

    private RepCallback(@NonNull OkCallbackListener httpData, @NonNull Mapper mapper
            , Class clazz, @NonNull Class<? extends JsonEntity> templateClazz, EntityType type) {
        obj = new SparseArray<>();
        this.httpData = httpData;
        this.mapper = mapper;
        this.clazz = clazz;
        this.templateClazz = templateClazz;
        this.type = type;
    }

    @SuppressWarnings("unchecked")
    private boolean analyzeJson(String body) throws Exception {
        TLog.i(TAG, "start====>"+body);
        JsonEntity data = JsonEntity.fromJson(body, clazz, templateClazz);
        if (data != null) {
            TLog.i(TAG, "notNull");
            if (data.isSuccess() && data instanceof JsonEntity.ArrayData) {
                TLog.i(TAG, "ArrayData");
                obj.put(0, mapper.transformEntityCollection(((JsonEntity.ArrayData)data).getArrayData()));
            } else if (data.isSuccess() && data instanceof JsonEntity.Data) {
                TLog.i(TAG, "Data");
                obj.put(0, mapper.transformEntity(((JsonEntity.Data)data).getData()));
            } else if (data.isSuccess() && clazz != null) {
                TLog.i(TAG, "error");
                throw new Exception("模板中未实现相应的接口");
            } else if (!data.isSuccess()) {
                TLog.i(TAG, "msg===>"+data.getMessage());
                obj.put(0, data.getMessage());
            }
            return data.isSuccess();
        }
        TLog.i(TAG, "exception");
       /* if (type != null) {
            JsonEntity data = null;
            switch (type) {
                case LIST_ENTITY: {
                    data = JsonEntity.fromJson(body, clazz, templateClazz);
                    if (data != null && data instanceof JsonEntity.ArrayData) {
                        obj.put(0, mapper.transformEntityCollection(((JsonEntity.ArrayData)data).getArrayData()));
                        return data.isSuccess();
                    }
                }
                case OBJECT_ENTITY: {
                    data =  JsonEntity.fromJson(body, clazz, templateClazz);
                    if (data != null && data instanceof JsonEntity.Data) {
                        obj.put(0, mapper.transformEntity(((JsonEntity.Data)data).getData()));
                        return data.isSuccess();
                    }
                }
                default: {
                    if (data != null) {
                        data.setSuccess(false);
                        obj.put(0, data.getMessage());
                        return data.isSuccess();
                    }
                }
            }
        } else {
            JsonEntity data = JsonEntity.fromJson(body, templateClazz);
            return data.isSuccess();
        }*/
        throw new Exception();
    }

    private void sendResToMain(final boolean isSuccess) {
        getHandler().post(new Runnable() {
            @SuppressWarnings("unchecked")
            @Override
            public void run() {
                if (isSuccess) {
                    httpData.onSuccess(obj.get(0), obj.get(1).toString());
                } else {
                    httpData.onFailure(ERROR_CODE_0, obj.get(0).toString());
                }
            }
        });
    }

    @Override
    public void onFailure(Call call, IOException e) {
        obj.put(0, ERROR_MSG_1);
        sendResToMain(false);
        e.printStackTrace();
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        TLog.i(TAG, response + "");
        boolean isSuccess;
        if (response.isSuccessful()) {
            String entityBody = response.body().string();
            obj.put(1, entityBody);
            TLog.i(TAG, entityBody);
            try {
                isSuccess = analyzeJson(entityBody);
            } catch (Exception e) {
                isSuccess = false;
                obj.put(0, ERROR_MSG_2);
                e.printStackTrace();
            }
        } else {
            isSuccess = false;
            obj.put(0, ERROR_MSG_1);
        }
        sendResToMain(isSuccess);
    }

    private static class CustomHandler extends Handler {
        public CustomHandler() {
            super(Looper.getMainLooper());
        }
    }

    private static Handler getHandler() {
        synchronized (RepCallback.class) {
            if (handler == null) {
                handler = new CustomHandler();
            }
            return handler;
        }
    }

    public interface OkCallbackListener<T> {

        void onSuccess(T data, String resBody);

        void onFailure(int errorCode, String msg);
    }

    public static final class Builder {

        private OkCallbackListener httpData;    //回调监听
        private Mapper mapper;                  //数据交接
        private Class clazz;                    //实体模型
        private Class<? extends JsonEntity> templateClazz;         //解析模板
        private EntityType type;                //数据类型

        public Builder setListener(OkCallbackListener httpData) {
            this.httpData = httpData;
            return this;
        }

        public Builder setMapper(Mapper mapper) {
            this.mapper = mapper;
            return this;
        }

        public Builder setClass(Class clazz) {
            this.clazz = clazz;
            return this;
        }

        public Builder setTempletClass(Class<? extends JsonEntity> templateClazz) {
            this.templateClazz = templateClazz;
            return this;
        }

        public Builder setType(EntityType type) {
            this.type = type;
            return this;
        }

        public RepCallback build() {
            if (httpData != null && mapper != null && clazz != null
                     && templateClazz != null) {
                return new RepCallback(httpData, mapper, clazz, templateClazz, type);
            } else if (httpData != null && templateClazz != null) {
                return new RepCallback(httpData, templateClazz);
            } else {
                return new RepCallback();
            }
        }
    }

}

