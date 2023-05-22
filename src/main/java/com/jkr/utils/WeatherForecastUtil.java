package com.jkr.utils;


import com.jkr.config.Global;
import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 获取天气预报工具类
 * </p>
 *
 * @author DaiFuyou
 * @since 2020-05-28
 */
public class WeatherForecastUtil {

    /**
    * method_name:getTodayWeather
    * create_user: DaiFuyou
    * create_date:2020/9/3
    * create_time:10:54
    * describe: 根据城市编码获取实时天气
    * param:[cityId]
    * return:java.util.Map<java.lang.String,java.lang.Object>
    */
    public static Map<String, Object> getTodayWeather(String cityId)
            throws IOException, NullPointerException {
        String result = "";
        // 连接中央气象台的API
        URL url = new URL(Global.CENTRAL_METEOROLOGICAL_OBSERVATORY_API_ADDRESS + cityId);
        URLConnection connectionData = url.openConnection();
        connectionData.setConnectTimeout(Global.CONNECTION_TIMEOUT);
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    connectionData.getInputStream(), "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            String datas = sb.toString();
            JSONObject jsonData = JSONObject.fromObject(datas);
            Object o = jsonData.getJSONObject("data").get("forecast");
            List<Map<String, Object>> list = (List<Map<String, Object>>) jsonData.getJSONObject("data").get("forecast");
            if (null != list && list.size() > 0) {
                Map<String, Object> stringObjectMap = list.get(0);
                result = "  " + (String) stringObjectMap.get("week") + "  " + (String) stringObjectMap.get("type") + "  " + (String) stringObjectMap.get("fx") + (String) stringObjectMap.get("fl") + "  " + stringObjectMap.get("low").toString().substring(3, 5) + "-" + stringObjectMap.get("high").toString().substring(3, 6);
                map.put("result", result);
            }
        } catch (Exception e) {
            System.out.println("第三方天气接口服务异常，请联系系统管理员");
        }
        return map;
    }

}
