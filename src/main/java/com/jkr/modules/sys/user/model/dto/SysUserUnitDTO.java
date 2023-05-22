package com.jkr.modules.sys.user.model.dto;

import lombok.Data;

import java.util.List;

/**
 * @Author：jikeruan
 * @Description: 授权权限和单位
 * @Date: 2019/9/24 9:53
 */
@Data
public class SysUserUnitDTO {
    /**
     * 用户id
     */
    private String userId;
    /**
     * 单位ids
     */
    private List<String> unitIds;
}
