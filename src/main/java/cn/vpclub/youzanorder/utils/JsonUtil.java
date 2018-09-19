/*
 * Copyright (c) 2012-2014, EpicSaaS Yuan Xin technology Co., Ltd.
 *
 * All rights reserved.
 */
package cn.vpclub.youzanorder.utils;

import cn.vpclub.moses.utils.common.StringUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by HuangQi on 2017/3/21.
 */
public class JsonUtil {

    private static ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    /**
     * 通过对象的json数组字符串返回一个对象list
     *
     * @return
     */
    public static <T> List<T> turnObjList(String str, Class<T> obj) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        try {
            List<T> list = new ArrayList<>();
            JSONArray array = new JSONArray(str);
            for (int i = 0; i < array.length(); i++) {
                String ss = array.getJSONObject(i).toString();
                T ao = mapper.readValue(ss, obj);
                list.add(ao);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据对象的json返回对象(对象里可以包含list)
     *
     * @param str
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> T turnObj(String str, Class<T> obj) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        try {
            T ao = mapper.readValue(str, obj);
            return ao;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 把对象转换成json字符串
     *
     * @param obj
     * @return
     */
    public static String turnJson(Object obj) {
        try {
            String json = mapper.writeValueAsString(obj);
            return json;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 判断微信返回值是否正常
     *
     * @param json
     * @return
     */
    public static boolean isYouZanError(String json) throws JSONException {
        if (StringUtil.isEmpty(json)) {
            return true;
        }
        JSONObject obj = new JSONObject(json);
        String s = String.valueOf(obj.get("code"));
        if (!s.equals("200")) {
            return true;
        }
        return false;
    }

    /**
     * 从指定的json字符串中获取指定String属性
     *
     * @param key
     * @param json
     * @return
     */
    public static String getJsonParam(String key, String json) {
        try {
            JSONObject obj = new JSONObject(json);
            String s = obj.get(key).toString();
            return s;
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }


}
