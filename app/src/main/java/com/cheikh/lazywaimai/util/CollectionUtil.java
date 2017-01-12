package com.cheikh.lazywaimai.util;

import java.util.Collection;

public class CollectionUtil {

    /**
     * 判断集合是否为空
     * @param collection
     * @return
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * 获取集合的大小
     * @param collection
     * @return
     */
    public static int size(Collection<?> collection) {
        return collection != null ? collection.size() : 0;
    }
}