package com.jkr.modules.sys.user.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jkr.core.base.model.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * <p>
 * 用户表
 * </p>
 *
 * @author jikeruan
 * @since 2019-09-06
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SysUser对象", description = "用户表")
@TableName(value = "sys_user")
public class SysUser extends BaseModel {

    @ApiModelProperty(value = "所属单位Id")
    private String unitId;

    @ApiModelProperty(value = "所属单位名称")
    private String unitName;

    @ApiModelProperty(value = "所属处室Id")
    private String deptId;

    @ApiModelProperty(value = "所属处室名称")
    private String deptName;

    @ApiModelProperty(value = "用户名")
    private String loginName;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "密码盐")
    private String salt;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "手机号码")
    private String phone;

    @ApiModelProperty(value = "帐号状态（0：正常；1：禁用）")
    private Integer status;

    @TableField(exist = false)
    private List<String> roleIds;

    @TableField(exist = false)
    private List<String> unitIds;
    /**
     * 所有的权限集
     */
    @TableField(exist = false)
    private Set<String> allPermissionSet;

    /**
     * 所有用户数据集合（菜单权限，角色权限，单位权限等）
     */
    @TableField(exist = false)
    private Map<String,Object> permissionMap;

}
