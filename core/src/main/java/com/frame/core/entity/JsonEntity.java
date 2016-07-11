package com.frame.core.entity;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Administrator on 2016/1/11.
 */
public abstract class JsonEntity {

    public abstract boolean isSuccess();

    public abstract void setSuccess(boolean isSuccess);

    public abstract boolean isError();

    public abstract String getMessage();

    public static JsonEntity fromJson(String json, Class<? extends JsonEntity> conClazz) throws JsonSyntaxException {
        return new Gson().fromJson(json, conClazz);
    }

    /**
     * 对象转换为json
     * @param clazz         模型类
     * @param conClazz      父容器类
     * @return
     */
    public String toJson(Class<?> clazz, Class conClazz) {
        Gson gson = new Gson();
        Type objectType = type(conClazz, clazz);
        return gson.toJson(this, objectType);
    }

    /**
     * json解析为对象
     * @param json          json数据
     * @param clazz         数据模型类
     * @param conClazz      父容器类
     * @param <T>           返回类型
     * @return
     * @throws JsonSyntaxException
     */
    public static <T> T fromJson(String json, Class clazz, Class conClazz) throws JsonSyntaxException {
        Gson gson = new Gson();
        Type objectType = type(conClazz, clazz);
        return gson.fromJson(json, objectType);
    }

    static ParameterizedType type (final Class raw, final Type... args) {
        return new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return args;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }

            @Override
            public Type getRawType() {
                return raw;
            }
        };
    }

    public interface ArrayData<T> {
        List<T> getArrayData();
    }

    public interface Data<T> {
        T getData();
    }
}
