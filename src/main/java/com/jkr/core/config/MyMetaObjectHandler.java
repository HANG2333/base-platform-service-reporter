package com.jkr.core.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.jkr.common.context.BaseContextHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author：jikeruan
 * @Description: 自动填充
 * @Date: 2019/9/6 10:44
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createBy", BaseContextHandler.getUserID(), metaObject);
        this.setFieldValByName("createDate", LocalDateTime.now(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateBy", BaseContextHandler.getUserID(), metaObject);
        this.setFieldValByName("updateDate", LocalDateTime.now(), metaObject);
    }
}
