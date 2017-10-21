package com.frame.core.interf;

import java.util.Collection;

/**
 *  领域模型映射
 *  T 模型类型
 *  K 领域类型
 * Created by yzd on 2016/5/11.
 */
public interface Mapper<T, K> {

    T transformEntity(K obj) throws Exception;

    K transformBean(T obj) throws Exception;

    Collection<T> transformEntityCollection(Collection<K> obj) throws Exception;

    Collection<K> transformBeanCollection(Collection<T> obj) throws Exception;

    Class<K> getEntityClass();

    Class<T> getBeanClass();

}
