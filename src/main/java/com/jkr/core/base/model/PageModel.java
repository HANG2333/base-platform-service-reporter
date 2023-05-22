package com.jkr.core.base.model;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author：jikeruan
 * @Description: 分页类
 * @Date: 2020-02-14 09:08
 */
@Getter
@Setter
public class PageModel {

    @TableField(exist = false)
    @ApiModelProperty(value = "当前页面")
    private int current = 1;

    @TableField(exist = false)
    @ApiModelProperty(value = "每页多少条")
    private int size = 30;

    @TableField(exist = false)
    @ApiModelProperty(value = "排序字段")
    private String sort;

    @TableField(exist = false)
    @ApiModelProperty(value = "排序类型")
    private String order;

}
