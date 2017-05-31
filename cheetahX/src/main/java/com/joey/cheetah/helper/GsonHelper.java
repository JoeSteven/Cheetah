package com.joey.cheetah.helper;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

/**
 * description - json解析帮助类
 * <p/>
 * author - Joe.
 * create on 16/7/15.
 * change
 * change on .
 */
public class GsonHelper {
    private static Gson gson;
    public static synchronized Gson createGson(){
        if(gson==null)gson = new Gson();
        return gson;
    }

    public static JsonObject toJsonObject(Object o){
        return (JsonObject)  createGson().toJsonTree(o);
    }

    @SuppressWarnings("unchecked")
    public static JsonObject toJsonObject(String[] key, Object[] values){
        Map map = new HashMap();
        for(int i = 0;i<key.length;i++){
            map.put(key[i],values[i]);
        }
        return toJsonObject(map);
    }


    public static <T> T fromJson(String json, Class<T> clazz){
        return createGson().fromJson(json,clazz);
    }

    public static <T> T fromJson(JsonObject jsonObject,Class<T> clazz){
        return createGson().fromJson(jsonObject,clazz);
    }

    public static <T> T fromJson(Map map, Class<T> clazz){
        Gson gson = createGson();
        return gson.fromJson(toJsonObject(map),clazz);
    }
}
