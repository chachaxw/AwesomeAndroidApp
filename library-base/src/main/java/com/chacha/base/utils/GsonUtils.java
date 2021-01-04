package com.chacha.base.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;

/**
 * @author chacha
 * @project AwesomeAndroidApp
 * @package_name com.chacha.base.utils
 * @date 12/21/20
 * @time 11:09 PM
 *
 * 应用模块: utils
 * <p>
 *      json解析工具类
 * <p>
 */
public class GsonUtils {
    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";

    public static final Gson localGson = createLocalGson();

    public static final Gson remoteGson = createRemoteGson();

    private static Gson createLocalGson() {
        return createLocalGsonBuilder().create();
    }

    private static Gson createRemoteGson() {
        return createLocalGsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    }

    private static GsonBuilder createLocalGsonBuilder() {
        final GsonBuilder gsonBuilder = new GsonBuilder();

        gsonBuilder.setLenient();
        gsonBuilder.setDateFormat(DATE_FORMAT);

        return gsonBuilder;
    }

    public static Gson getLocalGson() {
        return localGson;
    }

    public static <T> T fromLocalJson(String json, Class<T> clazz) throws JsonSyntaxException {
        try {
            return localGson.fromJson(json, clazz);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T fromLocalJson(String json, Type typeofT) {
        return localGson.fromJson(json, typeofT);
    }

    public static String toJson(Object src) {
        return localGson.toJson(src);
    }

    public static String toRemoteJson(Object src) {
        return remoteGson.toJson(src);
    }
}
