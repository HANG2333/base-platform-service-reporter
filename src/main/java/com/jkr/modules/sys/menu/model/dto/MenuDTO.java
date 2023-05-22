package com.jkr.modules.sys.menu.model.dto;

import lombok.Data;

/**
 * @author lzy
 * SysMenu查询条件DTO对象
 */
@Data
public class MenuDTO {
    /**
     * 类型范围开始值
     */
    Integer typeBegin;
    /**
     * 类型范围结束值
     */
    Integer typeEnd;

}
