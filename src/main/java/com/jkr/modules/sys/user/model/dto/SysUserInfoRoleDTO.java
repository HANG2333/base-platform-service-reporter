package com.jkr.modules.sys.user.model.dto;

import lombok.Data;

import java.util.List;

/**
 * @Author：jikeruan
 * @Description: 授权角色使用
 * @Date: 2019/9/12 9:26
 */
@Data
public class SysUserInfoRoleDTO {
    /**
     * 用户Id
     */
    private String userId;
    /**
     * 授权角色Ids
     */
    private List<String> roleIds;
}
