package com.jkr.core.binding;

/**
 * @Author：jikeruan
 * @Description: 比较条件枚举类
 * @Date: 2020-02-12 08:49
 */
public enum Comparison {
    // 相等，默认
    EQ,
    // IN
    IN,

    //左like
    LEFT_LIKE,
    // like
    LIKE,
    //右like
    RIGHT_LIKE,

    // 大于
    GT,
    // 大于等于
    GE,
    // 小于
    LT,
    // 小于等于
    LE,

    //介于-之间
    BETWEEN
}
