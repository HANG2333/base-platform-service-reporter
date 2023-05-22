/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.jkr.config;

/**
 * 全局配置类
 *
 * @author DaiFuyou
 * @version 2020-05-26
 */
public class Global {


    /**
     * 白山市城市编码
     */
    public static final String BAISHAN_CITY_CODE = "101060901";
    /**
     * 网路连接超时时间
     */
    public static final int CONNECTION_TIMEOUT = 1000;
    /**
     * 中央气象台的API地址
     */
    public static final String CENTRAL_METEOROLOGICAL_OBSERVATORY_API_ADDRESS = "http://t.weather.sojson.com/api/weather/city/";
    /**
     * HTML文件后缀名
     */
    public static final String HTML_FILE_EXTENSION = ".html";


    /**
     * 分隔符
     */
    public static final String DELIMITER = "@@";


    /**
     * 消息状态
     */
    public static final String SUCCESS = "SUCCESS";
    public static final String FAIL = "FAIL";
    /**
     * 全部单位标识
     */
    public static final String ALLUNITS = "0";
    /**
     * 逗号分隔符
     */
    public static final String SEPARATE = ",";


    /**
     * 白山市本级区域编码
     */
    public static final String BAISHANCITYLEVELCODE = "220601";

    /**
     * redis推送订阅通道（消息提醒通道）
     */
    public static final String NEWS_REMINDER_PUSH_CHANNEL = "NEWS_REMINDER_PUSH_CHANNEL";




}
