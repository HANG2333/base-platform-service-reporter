package com.jkr.utils;

public class DataProcessingUtil {
    /**
     * 将数组转换成以逗号分隔的字符串
     *
     * @param needChange
     *            需要转换的数组
     * @return 以逗号分割的字符串
     */
    public static String arrayToStrWithComma(String[] needChange) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < needChange.length; i++) {
            sb.append(needChange[i]);
            if ((i + 1) != needChange.length) {
                sb.append(",");
            }
        }
        return sb.toString();
    }
}
