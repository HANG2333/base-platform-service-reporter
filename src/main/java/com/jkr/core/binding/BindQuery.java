package com.jkr.core.binding;

import java.lang.annotation.*;

/**
 * @Author：jikeruan
 * @Description: 绑定管理器
 * @Date: 2020-02-12 08:49
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BindQuery {

    /**
     * 查询条件
     * 无@BindQuery注解默认会映射为=条件
     *
     * @return
     */
    Comparison comparison() default Comparison.EQ;

    /**
     * 数据库字段，默认为空，自动根据驼峰转下划线
     *
     * @return
     */
    String field() default "";

    /**
     * 忽略该字段
     *
     * @return
     */
    boolean ignore() default false;

    /**
     * 别名
     *
     * @return
     */
    String alias() default "";
}