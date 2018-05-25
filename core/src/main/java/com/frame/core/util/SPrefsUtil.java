package com.frame.core.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Map;

/**
 * sharedpreferences工具，可加密
 */
public final class SPrefsUtil {

    private static String FILE_NAME = "file_name";
    private static String MAK = "innoview";// = "3Wri9i2abNXlLhme"; // "innoview";
    private Context context;
    private static SPrefsUtil instance;

    private SPrefsUtil(Context context) {
        this.context = context.getApplicationContext();
    }

    public synchronized static SPrefsUtil getInstance(Context context, String filename) {
        if (instance == null) {
            instance = new SPrefsUtil(context);
        }
        if (filename != null) {
            FILE_NAME = filename;
        }
        return instance;
    }

    public static void encryptKey(String key) {
        MAK = key;
    }

    /**
     * 普通保存
     * param key
     * param value
     * return
     */
    public void save(String key, String value) {
        SharedPreferences.Editor editor = getShare().edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * 普通读取
     * param key
     * param defaultValue
     * return
     */
    public String load(String key, String defaultValue) {
        return getShare().getString(key, defaultValue);
    }

    /**
     * 加密保存
     * param key
     * param value
     * return
     */
    public void encryptSave(String key, String value) {
        try {
            SharedPreferences.Editor editor = getShare().edit();
            editor.putString(key, StringUtils.isEmpty(value) ? "" : Encryptor.encrypt(MAK, value));
            editor.apply();
        } catch (Exception e) {
            TLog.i("encryptSave", "error");
            e.printStackTrace();
        }
    }

    /**
     * 解密读取
     * param key
     * param value
     * return
     */
    public String decryptLoad(String key) {
        try {
            String str = getShare().getString(key, "");
            TLog.i("decryptLoad", str+"_"+"_"+key+"_"+FILE_NAME);
            return !"".equals(str) ? Encryptor.decrypt(MAK, str) : "";
        } catch(Exception e) {
            TLog.i("decryptLoad", "error");
            return "";
        }
    }

    public synchronized SharedPreferences getShare() {
        return this.context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

    public void saveSharedPreferences(String key, int value) {
        SharedPreferences.Editor editor = getShare().edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public int loadIntSharedPreference(String key) {
        return getShare().getInt(key, 0);
    }

    public void saveSharedPreferences(String key, float value) {
        SharedPreferences.Editor editor = getShare().edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    public float loadFloatSharedPreference(String key) {
        return getShare().getFloat(key, 0f);
    }

    public void saveSharedPreferences(String key, Long value) {
        SharedPreferences.Editor editor = getShare().edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public long loadLongSharedPreference(String key) {
        return getShare().getLong(key, 0L);
    }

    public void saveSharedPreferences(String key, Boolean value) {
        SharedPreferences.Editor editor = getShare().edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean loadBooleanSharedPreference(String key) {
        return getShare().getBoolean(key, false);
    }

    public void saveObject(String key, Object obj) {
        try {
            SharedPreferences.Editor editor = getShare().edit();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            String stringBase64 = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
            editor.putString(key, stringBase64);
            editor.apply();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object getObject(String key) {
        try {
            String stringBase64 = load(key, "");
            if (TextUtils.isEmpty(stringBase64))return null;
            byte[] base64Bytes = Base64.decode(stringBase64.getBytes(), Base64.DEFAULT);
            ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveAllSharePreference(String keyName, List<?> list) {
        int size = list.size();
        if (size < 1) {
            return;
        }
        SharedPreferences.Editor editor = getShare().edit();
        if (list.get(0) instanceof String) {
            for (int i = 0; i < size; i++) {
                editor.putString(keyName + i, (String) list.get(i));
            }
        } else if (list.get(0) instanceof Long) {
            for (int i = 0; i < size; i++) {
                editor.putLong(keyName + i, (Long) list.get(i));
            }
        } else if (list.get(0) instanceof Float) {
            for (int i = 0; i < size; i++) {
                editor.putFloat(keyName + i, (Float) list.get(i));
            }
        } else if (list.get(0) instanceof Integer) {
            for (int i = 0; i < size; i++) {
                editor.putLong(keyName + i, (Integer) list.get(i));
            }
        } else if (list.get(0) instanceof Boolean) {
            for (int i = 0; i < size; i++) {
                editor.putBoolean(keyName + i, (Boolean) list.get(i));
            }
        }
        editor.apply();
    }

    /**
     * 保存所有数据类型 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param context
     * @param key
     * @param object
     */
    public void setParam(Context context, String key, Object object) {

        String type = object.getClass().getSimpleName();
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if ("String".equals(type)) {
            editor.putString(key, (String) object);
        } else if ("Integer".equals(type)) {
            editor.putInt(key, (Integer) object);
        } else if ("Boolean".equals(type)) {
            editor.putBoolean(key, (Boolean) object);
        } else if ("Float".equals(type)) {
            editor.putFloat(key, (Float) object);
        } else if ("Long".equals(type)) {
            editor.putLong(key, (Long) object);
        }

        key = null;
        object = null;
        editor.apply();
        editor = null;
        sp = null;
        context = null;
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param context
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object getParam(Context context, String key,
                                  Object defaultObject) {
        String type = defaultObject.getClass().getSimpleName();
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);

        if ("String".equals(type)) {
            return sp.getString(key, (String) defaultObject);
        } else if ("Integer".equals(type)) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if ("Boolean".equals(type)) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if ("Float".equals(type)) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if ("Long".equals(type)) {
            return sp.getLong(key, (Long) defaultObject);
        }

        return null;
    }

    public Map<String, ?> loadAllSharePreference(String key) {
        return getShare().getAll();
    }

    public void removeKey(String key) {
        SharedPreferences.Editor editor = getShare().edit();
        editor.remove(key);
        editor.apply();
    }

    public void removeAllKey() {
        SharedPreferences.Editor editor = getShare().edit();
        editor.clear();
        editor.apply();
    }

}
