package com.jkr.common.context;

import com.jkr.common.constant.CommonConstant;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author：jikeruan
 * @Description: 线程帮助类
 * @Date: 2019/9/6 13:34
 */
public class BaseContextHandler {

    public static ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal<>();

    public static void set(String key, Object value) {
        Map<String, Object> map = threadLocal.get();
        if (map == null) {
            map = new HashMap<String, Object>();
            threadLocal.set(map);
        }
        map.put(key, value);
    }

    public static Object get(String key) {
        Map<String, Object> map = threadLocal.get();
        if (map == null) {
            map = new HashMap<String, Object>();
            threadLocal.set(map);
        }
        return map.get(key);
    }

    public static void setUserID(String userID) {
        set(CommonConstant.CONTEXT_KEY_USER_ID, userID);
    }

    public static String getUserID() {
        Object value = get(CommonConstant.CONTEXT_KEY_USER_ID);
        return returnObjectValue(value);
    }

    public static void setUsername(String username) {
        set(CommonConstant.CONTEXT_KEY_USERNAME, username);
    }

    public static String getUsername() {
        Object value = get(CommonConstant.CONTEXT_KEY_USERNAME);
        return returnObjectValue(value);
    }

    public static void setTenantId(String tenantId) {
        set(CommonConstant.CONTEXT_KEY_TENANT_ID, tenantId);
    }

    public static String getTenantId() {
        Object value = get(CommonConstant.CONTEXT_KEY_TENANT_ID);
        return returnObjectValue(value);
    }

    private static String returnObjectValue(Object value) {
        return value == null ? null : value.toString();
    }

    public static void remove() {
        threadLocal.remove();
    }

    public static void setUnitID(String unitID) {
        set(CommonConstant.CONTEXT_KEY_UNIT_ID, unitID);
    }

    public static String getUnitID() {
        Object value = get(CommonConstant.CONTEXT_KEY_UNIT_ID);
        return returnObjectValue(value);
    }

}
