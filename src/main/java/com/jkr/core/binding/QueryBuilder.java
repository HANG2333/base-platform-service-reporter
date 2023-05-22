package com.jkr.core.binding;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

/**
 * @Author：jikeruan
 * @Description: QueryWrapper构建器 - Entity，DTO -> 注解绑定查询条件 并转换为QueryWrapper对象
 * @Date: 2020-02-12 08:49
 */
@Slf4j
public class QueryBuilder {

    /**
     * Entity或者DTO对象转换为QueryWrapper
     *
     * @param dto
     * @param <T>
     * @param <DTO>
     * @return
     */
    public static <T, DTO> QueryWrapper<T> toWrapper(DTO dto) {
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        return dtoToWrapper(wrapper, dto);
    }

    /**
     * 转换具体实现
     *
     * @param wrapper
     * @param dto
     * @param <T>
     * @return
     */
    private static <T, DTO> QueryWrapper<T> dtoToWrapper(QueryWrapper<T> wrapper, DTO dto) {
        // 转换
        List<Field> declaredFields = CollUtil.newArrayList(ReflectUtil.getFields(dto.getClass()));
        for (Field field : declaredFields) {
            //忽略static，以及final，transient
            boolean isStatic = Modifier.isStatic(field.getModifiers());
            boolean isFinal = Modifier.isFinal(field.getModifiers());
            boolean isTransient = Modifier.isTransient(field.getModifiers());
            if (isStatic || isFinal || isTransient) {
                continue;
            }
            //忽略注解 @TableField(exist = false) 的字段
            TableField tableField = field.getAnnotation(TableField.class);
            if (tableField != null && !tableField.exist()) {
                continue;
            }
            BindQuery query = field.getAnnotation(BindQuery.class);
            //忽略字段
            if (query != null && query.ignore()) {
                continue;
            }
            //打开私有访问 获取值
            field.setAccessible(true);
            Object value = null;
            try {
                value = field.get(dto);
            } catch (IllegalAccessException e) {
                log.error("通过反射获取属性值出错：" + e);
            }
            if (value == null) {
                continue;
            }
            // 对比类型
            Comparison comparison = (query != null) ? query.comparison() : Comparison.EQ;
            // 转换条件
            String columnName = getColumnName(field);
            switch (comparison) {
                case EQ:
                    wrapper.eq(columnName, value);
                    break;
                case IN:
                    if (value.getClass().isArray()) {
                        Object[] valueArray = (Object[]) value;
                        if (valueArray.length == 1) {
                            wrapper.in(columnName, valueArray[0]);
                        } else if (valueArray.length >= 2) {
                            wrapper.in(columnName, valueArray);
                        }
                    } else if (value instanceof String && ((String) value).contains(",")) {
                        String[] valueArray = ((String) value).split(",");
                        wrapper.in(columnName, valueArray);
                    } else {
                        wrapper.in(columnName, value);
                    }
                    break;
                case LEFT_LIKE:
                    wrapper.likeLeft(columnName, value);
                    break;
                case LIKE:
                    wrapper.like(columnName, value);
                    break;
                case RIGHT_LIKE:
                    wrapper.likeRight(columnName, value);
                    break;
                case GT:
                    wrapper.gt(columnName, value);
                    break;
                case GE:
                    wrapper.ge(columnName, value);
                    break;
                case LT:
                    wrapper.lt(columnName, value);
                    break;
                case LE:
                    wrapper.le(columnName, value);
                    break;
                case BETWEEN:
                    if (value.getClass().isArray()) {
                        Object[] valueArray = (Object[]) value;
                        if (valueArray.length == 1) {
                            wrapper.ge(columnName, valueArray[0]);
                        } else if (valueArray.length >= 2) {
                            wrapper.between(columnName, valueArray[0], valueArray[1]);
                        }
                    }
                    // 支持逗号分隔的字符串
                    else if (value instanceof String && ((String) value).contains(",")) {
                        Object[] valueArray = ((String) value).split(",");
                        wrapper.between(columnName, valueArray[0], valueArray[1]);
                    } else {
                        wrapper.ge(columnName, value);
                    }
                    break;
                default:
            }
        }
        return wrapper;
    }

    /**
     * 获取数据表的列名（驼峰转下划线蛇形命名）
     * <br>
     * 列名取值优先级： @BindQuery.field > @TableField.value > field.name
     *
     * @param field
     * @return
     */
    private static String getColumnName(Field field) {
        String columnName = null;
        if (field.isAnnotationPresent(BindQuery.class)) {
            BindQuery bindQuery = field.getAnnotation(BindQuery.class);
            if (StrUtil.isNotBlank(bindQuery.alias()) && StrUtil.isNotBlank(bindQuery.field())) {
                columnName = StrUtil.format("{}.{}", bindQuery.alias(), bindQuery.field());
            } else {
                columnName = bindQuery.field();
            }
        } else if (field.isAnnotationPresent(TableField.class)) {
            columnName = field.getAnnotation(TableField.class).value();
        }
        return StrUtil.isNotBlank(columnName) ? columnName : StrUtil.toUnderlineCase(field.getName());
    }
}
