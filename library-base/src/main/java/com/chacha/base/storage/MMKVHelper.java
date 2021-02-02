package com.chacha.base.storage;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tencent.mmkv.MMKV;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chacha
 * @project AwesomeAndroidApp
 * @package_name com.chacha.base.storage
 * @date 2/1/21
 * @time 5:52 PM
 *
 * <p>
 *     腾讯MMKV序列化存储工具类
 * </p>
 */
public class MMKVHelper {
    private static MMKV mmkv;

    private MMKVHelper() {
        mmkv = MMKV.defaultMMKV();
    }

    public static MMKVHelper getInstance() {
        return MMKVHolder.INSTANCE;
    }

    private static class MMKVHolder {
        private static final MMKVHelper INSTANCE = new MMKVHelper();
    }

    public MMKV getMmkv() {
        return mmkv;
    }

    /**
     * 存入map集合
     *
     * @param key String 存储标识
     * @param map Map<String, Object> 数据集合
     */
    public void saveInfo(String key, Map<String, Object> map) {
        Gson gson = new Gson();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject(gson.toJson(map));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        jsonArray.put(jsonObject);
        mmkv.encode(key, jsonArray.toString());
    }

    /**
     * 获取map集合
     *
     * @param key String 存储标识
     * @return Map<String, String> 数据集合
     */
    public Map<String, String> getInfo(String key) {
        Map<String, String> itemMap = new HashMap<>();
        String result = mmkv.decodeString(key, "");

        try {
            JSONArray jsonArray = new JSONArray(result);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject itemObject = jsonArray.getJSONObject(i);
                JSONArray names = itemObject.names();

                if (names != null) {
                    for (int j = 0; j < names.length(); j++) {
                        String name = names.getString(j);
                        String value = itemObject.getString(name);
                        itemMap.put(name, value);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return itemMap;
    }

    // 扩展MMKV类使其支持对象存储
    public <T> T getObject(String key, Class<T> t) {
        String str = mmkv.decodeString(key, null);
        if (!TextUtils.isEmpty(str)) {
            return new GsonBuilder().create().fromJson(str, t);
        }

        return null;
    }

    /**
     * 存储对象
     *
     * @param key String 存储标识
     * @param object Object 数据对象
     */
    public void putObject(String key, Object object) {
        String str = new GsonBuilder().create().toJson(object);
        mmkv.encode(key, str);
    }
}
