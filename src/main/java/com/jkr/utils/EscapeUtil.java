package com.jkr.utils;

import org.apache.commons.lang3.StringUtils;
/**
 * @title: 模糊查询工具类
 * @author: DaiFuyou
 * @date: 2020/9/8
 * @description:特殊字符转义
 * @param:
 * @return:
 */
public class EscapeUtil {
    //mysql的模糊查询时特殊字符转义
    public static String escapeChar(String before) {
        if (StringUtils.isNotBlank(before)) {
            before = before.replaceAll("\\\\", "\\\\\\\\");
            before = before.replaceAll("_", "\\\\_");
            before = before.replaceAll("%", "\\\\%");
        }
        return before;
    }
}