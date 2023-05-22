package com.jkr.modules.sys.dict.model;

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
 * 字典管理-实体类
 *
 * @author zenglingwen
 * @since 2022-04-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "sys_dict")
@ApiModel(value = "SysDict对象", description = "字典表")
public class SysDict extends BaseModel {

    @ApiModelProperty(value = "父级id")
    private String parentId;

    @ApiModelProperty(value = "父级ids")
    private String parentIds;

    @ApiModelProperty(value = "字典类型")
    private String dicType;

    @ApiModelProperty(value = "字典键值")
    private String dicCode;

    @ApiModelProperty(value = "字典名称")
    private String dicValue;

    @ApiModelProperty(value = "排序")
    private Integer location;

    @ApiModelProperty(value = "是否有子节点（0：无；1：有）")
    private Integer hasChildren;

    @ApiModelProperty(value = "父级节点类型")
    private String parentType;

    /**
     * 子数据
     */
    @TableField(exist = false)
    private List<SysDict> children;

}
