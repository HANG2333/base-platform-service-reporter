package com.jkr.modules.sys.user.model.dto;

import lombok.Data;

/**
 * @Author：jikeruan
 * @Description: 修改密码使用
 * @Date: 2020/1/9 13:22
 */
@Data
public class UpdPasswordDTO {

    /**
     * 用户id
     */
    private String userId;
    /**
     * 旧密码
     */
    private String oldPass;
    /**
     * 新密码
     */
    private String newPass;
    /**
     * 确认密码
     */
    private String conPass;
}
