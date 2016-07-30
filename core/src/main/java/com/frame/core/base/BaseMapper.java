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
        List<T> list = new ArrayList<>();
        for (K k : obj) {
            list.add(transformEntity(k));
        }
        return list;
    }
}
