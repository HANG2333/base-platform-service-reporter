package com.jkr.modules.sys.user.model.dto;

import com.jkr.modules.sys.user.model.SysUser;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 曾令文
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserDTO extends SysUser {

    private String roleId;

    private String roleNames;

}
