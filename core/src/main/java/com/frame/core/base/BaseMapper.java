package com.frame.core.base;

import com.frame.core.interf.Mapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by yzd on 2016/7/15.
 */
public abstract class BaseMapper<T, K> implements Mapper<T, K> {

    @Override
    public Collection<T> transformEntityCollection(Collection<K> obj) throws Exception {
        if (obj == null) {
            return new ArrayList<>();
        }
        List<T> list = new ArrayList<>();
        for (K k : obj) {
            list.add(transformEntity(k));
        }
        return list;
    }

    @Override
    public Collection<K> transformBeanCollection(Collection<T> obj) throws Exception {
        if (obj == null) {
            return new ArrayList<>();
        }
        List<K> list = new ArrayList<>();
        for (T t : obj) {
            list.add(transformBean(t));
        }
        return list;
    }

    @Override
    public K transformBean(T obj) throws Exception {
        return null;
    }

    @Override
    public Class<K> getEntityClass() {
        return null;
    }

    @Override
    public Class<T> getBeanClass() {
        return null;
    }
}
