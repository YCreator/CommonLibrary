package com.frame.core.util;

import com.frame.core.exception.InstanceFactoryException;
import com.frame.core.interf.Mapper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yzd on 2017/9/8 0008.
 * 映射类工厂
 */

public final class MapperFactory {

    private static MapperFactory factory;
    private final Map<String, Mapper> mMap;

    private MapperFactory() {
        mMap = new HashMap<>();
    }

    public static MapperFactory getInstance() {
        if (factory == null) {
            factory = new MapperFactory();
        }
        return factory;
    }

    @SuppressWarnings("unchecked")
    public <T extends Mapper> T achieveObj(Class<? extends Mapper> implClazz) throws InstanceFactoryException {
        String simpleName = implClazz.getSimpleName();
        if (mMap.containsKey(simpleName)) {
            return (T) mMap.get(simpleName);
        }
        try {
            T t = (T) implClazz.newInstance();
            mMap.put(simpleName, t);
            return t;
        } catch (InstantiationException e) {
            throw new InstanceFactoryException("需被实例化的类不符合要求");
        } catch (IllegalAccessException e) {
            throw new InstanceFactoryException("需被实例化的类构造函数不可访问");
        }
    }

}
