package com.jkr.modules.sys.menu.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jkr.core.base.model.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 菜单表
 * </p>
 *
 * @author jikeruan
 * @since 2019-09-06
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SysMenu对象", description = "菜单表")
@TableName(value = "sys_menu")
public class SysMenu extends BaseModel {

    @ApiModelProperty(value = "父级Id")
    private String parentId;

    @ApiModelProperty(value = "所有父级Id")
    private String parentIds;

    @ApiModelProperty(value = "菜单名称")
    private String name;

    @ApiModelProperty(value = "菜单类型")
    private String type;

    @ApiModelProperty(value = "权限标识")
    private String code;

    @ApiModelProperty(value = "路由地址")
    private String href;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "启用状态（0：启用；1：禁用）")
    private String status;

    @ApiModelProperty(value = "排序")
    private Integer location;

    @ApiModelProperty(value = "菜单key")
    private String menuKey;

    @ApiModelProperty(value = "菜单子节点")
    @TableField(exist = false)
    private List<SysMenu> children = new ArrayList<>();


    @TableField(exist = false)
    private String parentCode;
}
