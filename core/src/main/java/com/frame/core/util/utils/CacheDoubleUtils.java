package com.frame.core.util.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.util.SimpleArrayMap;

import com.frame.core.util.constant.CacheConstants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * <pre>
 *     author: admin
 *     blog  : http://core.frame.com
 *     time  : 2017/05/24
 *     desc  : utils about cache(二级缓存工具)
 *     menu
 *          getInstance             : 1.获取缓存实例
            Instance.put            : 2.缓存中写入数据
            Instance.getBytes       : 3.缓存中读取字节数组
            Instance.getString      : 4.缓存中读取 String
            Instance.getJSONObject  : 5.缓存中读取 JSONObject
            Instance.getJSONArray   : 6.缓存中读取 JSONArray
            Instance.getBitmap      : 7.缓存中读取 Bitmap
            Instance.getDrawable    : 8.缓存中读取 Drawable
            Instance.getParcelable  : 9.缓存中读取 Parcelable
            Instance.getSerializable: 10.缓存中读取 Serializable
            Instance.getCacheSize   : 11.获取缓存大小
            Instance.getCacheCount  : 12.获取缓存个数
            Instance.remove         : 13.根据键值移除缓存
            Instance.clear          : 14.清除所有缓存
 * </pre>
 */

public class CacheDoubleUtils implements CacheConstants {
    private static final SimpleArrayMap<String, CacheDoubleUtils> CACHE_MAP = new SimpleArrayMap<>();
    private CacheMemoryUtils mCacheMemoryUtils;
    private CacheUtils mCacheUtils;

    /**
     * Return the single {@link CacheDoubleUtils} instance.
     *
     * @return the single {@link CacheDoubleUtils} instance
     */
    public static CacheDoubleUtils getInstance() {
        return getInstance(CacheMemoryUtils.getInstance(), CacheUtils.getInstance());
    }

    /**
     * Return the single {@link CacheDoubleUtils} instance.
     *
     * @param cacheMemoryUtils The instance of {@link CacheMemoryUtils}.
     * @param cacheUtils   The instance of {@link CacheUtils}.
     * @return the single {@link CacheDoubleUtils} instance
     */
    public static CacheDoubleUtils getInstance(@NonNull final CacheMemoryUtils cacheMemoryUtils,
                                               @NonNull final CacheUtils cacheUtils
    ) {
        final String cacheKey = cacheUtils.toString() + "_" + cacheMemoryUtils.toString();
        CacheDoubleUtils cache = CACHE_MAP.get(cacheKey);
        if (cache == null) {
            cache = new CacheDoubleUtils(cacheMemoryUtils, cacheUtils);
            CACHE_MAP.put(cacheKey, cache);
        }
        return cache;
    }

    private CacheDoubleUtils(CacheMemoryUtils cacheMemoryUtils, CacheUtils cacheUtils) {
        mCacheMemoryUtils = cacheMemoryUtils;
        mCacheUtils = cacheUtils;
    }


    ///////////////////////////////////////////////////////////////////////////
    // about bytes
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Put bytes in cache.
     *
     * @param key   The key of cache.
     * @param value The value of cache.
     */
    public void put(@NonNull final String key, final byte[] value) {
        put(key, value, -1);
    }

    /**
     * Put bytes in cache.
     *
     * @param key      The key of cache.
     * @param value    The value of cache.
     * @param saveTime The save time of cache, in seconds.
     */
    public void put(@NonNull final String key, byte[] value, final int saveTime) {
        mCacheMemoryUtils.put(key, value, saveTime);
        mCacheUtils.put(key, value, saveTime);
    }

    /**
     * Return the bytes in cache.
     *
     * @param key The key of cache.
     * @return the bytes if cache exists or null otherwise
     */
    public byte[] getBytes(@NonNull final String key) {
        return getBytes(key, null);
    }

    /**
     * Return the bytes in cache.
     *
     * @param key          The key of cache.
     * @param defaultValue The default value if the cache doesn't exist.
     * @return the bytes if cache exists or defaultValue otherwise
     */
    public byte[] getBytes(@NonNull final String key, final byte[] defaultValue) {
        byte[] obj = mCacheMemoryUtils.get(key);
        if (obj != null) return obj;
        return mCacheUtils.getBytes(key, defaultValue);
    }

    ///////////////////////////////////////////////////////////////////////////
    // about String
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Put string value in cache.
     *
     * @param key   The key of cache.
     * @param value The value of cache.
     */
    public void put(@NonNull final String key, final String value) {
        put(key, value, -1);
    }

    /**
     * Put string value in cache.
     *
     * @param key      The key of cache.
     * @param value    The value of cache.
     * @param saveTime The save time of cache, in seconds.
     */
    public void put(@NonNull final String key, final String value, final int saveTime) {
        mCacheMemoryUtils.put(key, value, saveTime);
        mCacheUtils.put(key, value, saveTime);
    }

    /**
     * Return the string value in cache.
     *
     * @param key The key of cache.
     * @return the string value if cache exists or null otherwise
     */
    public String getString(@NonNull final String key) {
        return getString(key, null);
    }

    /**
     * Return the string value in cache.
     *
     * @param key          The key of cache.
     * @param defaultValue The default value if the cache doesn't exist.
     * @return the string value if cache exists or defaultValue otherwise
     */
    public String getString(@NonNull final String key, final String defaultValue) {
        String obj = mCacheMemoryUtils.get(key);
        if (obj != null) return obj;
        return mCacheUtils.getString(key, defaultValue);
    }

    ///////////////////////////////////////////////////////////////////////////
    // about JSONObject
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Put JSONObject in cache.
     *
     * @param key   The key of cache.
     * @param value The value of cache.
     */
    public void put(@NonNull final String key, final JSONObject value) {
        put(key, value, -1);
    }

    /**
     * Put JSONObject in cache.
     *
     * @param key      The key of cache.
     * @param value    The value of cache.
     * @param saveTime The save time of cache, in seconds.
     */
    public void put(@NonNull final String key,
                    final JSONObject value,
                    final int saveTime) {
        mCacheMemoryUtils.put(key, value, saveTime);
        mCacheUtils.put(key, value, saveTime);
    }

    /**
     * Return the JSONObject in cache.
     *
     * @param key The key of cache.
     * @return the JSONObject if cache exists or null otherwise
     */
    public JSONObject getJSONObject(@NonNull final String key) {
        return getJSONObject(key, null);
    }

    /**
     * Return the JSONObject in cache.
     *
     * @param key          The key of cache.
     * @param defaultValue The default value if the cache doesn't exist.
     * @return the JSONObject if cache exists or defaultValue otherwise
     */
    public JSONObject getJSONObject(@NonNull final String key, final JSONObject defaultValue) {
        JSONObject obj = mCacheMemoryUtils.get(key);
        if (obj != null) return obj;
        return mCacheUtils.getJSONObject(key, defaultValue);
    }


    ///////////////////////////////////////////////////////////////////////////
    // about JSONArray
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Put JSONArray in cache.
     *
     * @param key   The key of cache.
     * @param value The value of cache.
     */
    public void put(@NonNull final String key, final JSONArray value) {
        put(key, value, -1);
    }

    /**
     * Put JSONArray in cache.
     *
     * @param key      The key of cache.
     * @param value    The value of cache.
     * @param saveTime The save time of cache, in seconds.
     */
    public void put(@NonNull final String key, final JSONArray value, final int saveTime) {
        mCacheMemoryUtils.put(key, value, saveTime);
        mCacheUtils.put(key, value, saveTime);
    }

    /**
     * Return the JSONArray in cache.
     *
     * @param key The key of cache.
     * @return the JSONArray if cache exists or null otherwise
     */
    public JSONArray getJSONArray(@NonNull final String key) {
        return getJSONArray(key, null);
    }

    /**
     * Return the JSONArray in cache.
     *
     * @param key          The key of cache.
     * @param defaultValue The default value if the cache doesn't exist.
     * @return the JSONArray if cache exists or defaultValue otherwise
     */
    public JSONArray getJSONArray(@NonNull final String key, final JSONArray defaultValue) {
        JSONArray obj = mCacheMemoryUtils.get(key);
        if (obj != null) return obj;
        return mCacheUtils.getJSONArray(key, defaultValue);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Bitmap cache
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Put bitmap in cache.
     *
     * @param key   The key of cache.
     * @param value The value of cache.
     */
    public void put(@NonNull final String key, final Bitmap value) {
        put(key, value, -1);
    }

    /**
     * Put bitmap in cache.
     *
     * @param key      The key of cache.
     * @param value    The value of cache.
     * @param saveTime The save time of cache, in seconds.
     */
    public void put(@NonNull final String key, final Bitmap value, final int saveTime) {
        mCacheMemoryUtils.put(key, value, saveTime);
        mCacheUtils.put(key, value, saveTime);
    }

    /**
     * Return the bitmap in cache.
     *
     * @param key The key of cache.
     * @return the bitmap if cache exists or null otherwise
     */
    public Bitmap getBitmap(@NonNull final String key) {
        return getBitmap(key, null);
    }

    /**
     * Return the bitmap in cache.
     *
     * @param key          The key of cache.
     * @param defaultValue The default value if the cache doesn't exist.
     * @return the bitmap if cache exists or defaultValue otherwise
     */
    public Bitmap getBitmap(@NonNull final String key, final Bitmap defaultValue) {
        Bitmap obj = mCacheMemoryUtils.get(key);
        if (obj != null) return obj;
        return mCacheUtils.getBitmap(key, defaultValue);
    }

    ///////////////////////////////////////////////////////////////////////////
    // about Drawable
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Put drawable in cache.
     *
     * @param key   The key of cache.
     * @param value The value of cache.
     */
    public void put(@NonNull final String key, final Drawable value) {
        put(key, value, -1);
    }

    /**
     * Put drawable in cache.
     *
     * @param key      The key of cache.
     * @param value    The value of cache.
     * @param saveTime The save time of cache, in seconds.
     */
    public void put(@NonNull final String key, final Drawable value, final int saveTime) {
        mCacheMemoryUtils.put(key, value, saveTime);
        mCacheUtils.put(key, value, saveTime);
    }

    /**
     * Return the drawable in cache.
     *
     * @param key The key of cache.
     * @return the drawable if cache exists or null otherwise
     */
    public Drawable getDrawable(@NonNull final String key) {
        return getDrawable(key, null);
    }

    /**
     * Return the drawable in cache.
     *
     * @param key          The key of cache.
     * @param defaultValue The default value if the cache doesn't exist.
     * @return the drawable if cache exists or defaultValue otherwise
     */
    public Drawable getDrawable(@NonNull final String key, final Drawable defaultValue) {
        Drawable obj = mCacheMemoryUtils.get(key);
        if (obj != null) return obj;
        return mCacheUtils.getDrawable(key, defaultValue);
    }

    ///////////////////////////////////////////////////////////////////////////
    // about Parcelable
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Put parcelable in cache.
     *
     * @param key   The key of cache.
     * @param value The value of cache.
     */
    public void put(@NonNull final String key, final Parcelable value) {
        put(key, value, -1);
    }

    /**
     * Put parcelable in cache.
     *
     * @param key      The key of cache.
     * @param value    The value of cache.
     * @param saveTime The save time of cache, in seconds.
     */
    public void put(@NonNull final String key, final Parcelable value, final int saveTime) {
        mCacheMemoryUtils.put(key, value, saveTime);
        mCacheUtils.put(key, value, saveTime);
    }

    /**
     * Return the parcelable in cache.
     *
     * @param key     The key of cache.
     * @param creator The creator.
     * @param <T>     The value type.
     * @return the parcelable if cache exists or null otherwise
     */
    public <T> T getParcelable(@NonNull final String key,
                               @NonNull final Parcelable.Creator<T> creator) {
        return getParcelable(key, creator, null);
    }

    /**
     * Return the parcelable in cache.
     *
     * @param key          The key of cache.
     * @param creator      The creator.
     * @param defaultValue The default value if the cache doesn't exist.
     * @param <T>          The value type.
     * @return the parcelable if cache exists or defaultValue otherwise
     */
    public <T> T getParcelable(@NonNull final String key,
                               @NonNull final Parcelable.Creator<T> creator,
                               final T defaultValue) {
        T value = mCacheMemoryUtils.get(key);
        if (value != null) return value;
        return mCacheUtils.getParcelable(key, creator, defaultValue);
    }

    ///////////////////////////////////////////////////////////////////////////
    // about Serializable
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Put serializable in cache.
     *
     * @param key   The key of cache.
     * @param value The value of cache.
     */
    public void put(@NonNull final String key, final Serializable value) {
        put(key, value, -1);
    }

    /**
     * Put serializable in cache.
     *
     * @param key      The key of cache.
     * @param value    The value of cache.
     * @param saveTime The save time of cache, in seconds.
     */
    public void put(@NonNull final String key, final Serializable value, final int saveTime) {
        mCacheMemoryUtils.put(key, value, saveTime);
        mCacheUtils.put(key, value, saveTime);
    }

    /**
     * Return the serializable in cache.
     *
     * @param key The key of cache.
     * @return the bitmap if cache exists or null otherwise
     */
    public Object getSerializable(@NonNull final String key) {
        return getSerializable(key, null);
    }

    /**
     * Return the serializable in cache.
     *
     * @param key          The key of cache.
     * @param defaultValue The default value if the cache doesn't exist.
     * @return the bitmap if cache exists or defaultValue otherwise
     */
    public Object getSerializable(@NonNull final String key, final Object defaultValue) {
        Object obj = mCacheMemoryUtils.get(key);
        if (obj != null) return obj;
        return mCacheUtils.getSerializable(key, defaultValue);
    }

    /**
     * Return the size of cache in disk.
     *
     * @return the size of cache in disk
     */
    public long getCacheDiskSize() {
        return mCacheUtils.getCacheSize();
    }

    /**
     * Return the count of cache in disk.
     *
     * @return the count of cache in disk
     */
    public int getCacheDiskCount() {
        return mCacheUtils.getCacheCount();
    }

    /**
     * Return the count of cache in memory.
     *
     * @return the count of cache in memory.
     */
    public int getCacheMemoryCount() {
        return mCacheMemoryUtils.getCacheCount();
    }

    /**
     * Remove the cache by key.
     *
     * @param key The key of cache.
     */
    public void remove(@NonNull String key) {
        mCacheMemoryUtils.remove(key);
        mCacheUtils.remove(key);
    }

    /**
     * Clear all of the cache.
     */
    public void clear() {
        mCacheMemoryUtils.clear();
        mCacheUtils.clear();
    }
}
