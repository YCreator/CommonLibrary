package com.frame.core.net;

import android.content.Context;

import com.frame.core.BaseApplication;
import com.frame.core.exception.InstanceFactoryException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * remote对象构造工厂
 *
 * Created by yzd on 2016/5/20.
 */
public final class RemoteFactory {

    private static RemoteFactory factory;
    private Map<String, Object> remoteMap;
    private Context mContext;

    private RemoteFactory() {
        this.remoteMap = new HashMap<>();
        this.mContext = BaseApplication.get_context();
    }

    public static RemoteFactory getInstance() {
        if (factory == null) {
            factory = new RemoteFactory();
        }
        return factory;
    }

    /**
     * 取得对象
     * @param clazz         接口
     * @param implClazz     接口实现类(注意：该类不能为接口，抽象类以及构造方法的访问权限不能为私有
     *                      ，否则抛出InstantiationException异常，构造函数必须可以被访问，否则抛出
     *                      IllegalAccessException异常，参数类型要对应类内部的构造函数里的参数)
     * @param <T>           返回预期结果类型
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T achieveObj(Class clazz, Class<T> implClazz) throws InstanceFactoryException {
        String simpleName = clazz.getSimpleName();
        if (remoteMap.containsKey(simpleName)) {
            return (T) remoteMap.get(simpleName);
        }
        try {
            Constructor<T> c = implClazz.getDeclaredConstructor(Context.class);
            c.setAccessible(true);
            T t = c.newInstance(mContext);
            remoteMap.put(simpleName, t);
            return t;
        } catch (InstantiationException e) {
            throw new InstanceFactoryException("需被实例化的类不符合要求");
        } catch (IllegalAccessException e) {
            throw new InstanceFactoryException("需被实例化的类构造函数不可访问");
        } catch (NoSuchMethodException e) {
            throw new InstanceFactoryException("需被实例化的类构造函数内的参数与所传递的参数类型不统一");
        } catch (InvocationTargetException e) {
            throw new InstanceFactoryException("需被实例化的类构造函数内出现异常");
        }
    }

}
