package com.jkr.modules.sys.role.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.jkr.core.base.model.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ApiModel(value = "SysRole对象", description = "角色表")
@TableName(value = "sys_role_menu")
public class SysRoleMenu {

    @ApiModelProperty(value = "角色Id")
    private String roleId;

    @ApiModelProperty(value = "菜单Id")
    private String menuId;

}