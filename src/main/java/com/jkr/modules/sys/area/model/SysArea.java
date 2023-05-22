package com.jkr.modules.sys.area.model;

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
 * 行政区划管理-实体类
 *
 * @author zenglingwen
 * @since 2022-04-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "SysArea对象", description = "区域表")
@TableName(value = "sys_area")
public class SysArea extends BaseModel {

    @ApiModelProperty(value = "父级编号")
    private String parentId;

    @ApiModelProperty(value = "所有父级编号")
    private String parentIds;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "区域编码")
    private String code;

    @ApiModelProperty(value = "区域类型")
    private String type;

    @ApiModelProperty(value = "排序")
    private Integer location;



    @TableField(exist = false)
    private List<SysArea> children;
    /**
     * 是否有子集
     */
    @TableField(exist = false)
    private boolean hasChildren;

}
