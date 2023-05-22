package com.jkr.modules.sys.unit.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jkr.core.base.model.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 单位表
 * </p>
 *
 * @author jikeruan
 * @since 2020-05-21
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SysUnit对象", description = "单位表")
@TableName("sys_unit")
public class SysUnit extends BaseModel {

    @ApiModelProperty(value = "父级id")
    private String parentId;

    @ApiModelProperty(value = "所有父级id")
    private String parentIds;

    @ApiModelProperty(value = "单位名称")
    private String name;

    @ApiModelProperty(value = "单位编码")
    private String code;

    @ApiModelProperty(value = "节点类型（0：根节点；1：单位节点；2：处室节点）")
    private String type;

    @ApiModelProperty(value = "行政区划id")
    private String areaId;

    @ApiModelProperty(value = "行政区划名称")
    private String areaName;

    @ApiModelProperty(value = "排序")
    private Integer location;

    @ApiModelProperty(value = "负责人")
    private String contact;

    @ApiModelProperty(value = "联系电话")
    private String phone;

    @TableField(exist = false)
    private List<SysUnit> children;

    @TableField(exist = false)
    private String areaNameAlias;

    @TableField(exist = false)
    private String typeName;

}
