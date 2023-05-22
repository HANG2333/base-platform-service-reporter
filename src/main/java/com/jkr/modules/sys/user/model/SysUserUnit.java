package com.jkr.modules.sys.user.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @ClassName SysUserUnit
 * @Description 用户单位关联表
 * @Author songyuhang
 * @Date 2020/5/21 19:31
 * @Version 1.0
 **/
@Data
@EqualsAndHashCode
@Accessors(chain = true)
@ApiModel(value = "SysUserUnit", description = "用户单位关联表")
public class SysUserUnit implements Serializable {



    @ApiModelProperty(value = "菜单id")
    private String unitId;

    @ApiModelProperty(value = "权限id")
    private String userId;

}

