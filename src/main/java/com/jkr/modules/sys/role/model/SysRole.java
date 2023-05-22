package com.jkr.modules.sys.role.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.jkr.core.base.model.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author jikeruan
 * @since 2019-09-06
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SysRole对象", description = "角色表")
@TableName(value = "sys_role")
public class SysRole extends BaseModel {

    @ApiModelProperty(value = "角色名称")
    private String name;

    @ApiModelProperty(value = "角色权限字符串")
    private String code;

    @ApiModelProperty(value = "角色状态（0：正常；1：禁用）")
    private String status;

}