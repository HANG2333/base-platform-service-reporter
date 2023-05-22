package com.jkr.modules.sys.log.model;


import com.baomidou.mybatisplus.annotation.TableField;
import com.jkr.core.base.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zenglingwen
 */
@Data
public class SysLog extends BaseModel {

    @ApiModelProperty(value = "请求IP")
    private String ip;

    @ApiModelProperty(value = "请求地址")
    private String url;

    @ApiModelProperty(value = "描述信息")
    private String modal;

    @ApiModelProperty(value = "请求方式")
    private String httpMethod;

    @ApiModelProperty(value = "请求方法")
    private String classMethod;

    @ApiModelProperty(value = "请求参数")
    private String requestParams;

    @ApiModelProperty(value = "返回结果")
    private String result;

    @ApiModelProperty(value = "请求耗时")
    private String timeCost;

    @ApiModelProperty(value = "请求状态（0：正常；1：异常）")
    private Integer requestStatus;

    @ApiModelProperty(value = "异常信息")
    private String exception;


    /**
     * 操作账号
     */
    @TableField(exist = false)
    private String loginName;


    /**
     * 操作名称
     */
    @TableField(exist = false)
    private String name;

    /**
     * 开始时间-查询
     */
    @TableField(exist = false)
    private String startTime;

    /**
     * 结束时间-查询
     */
    @TableField(exist = false)
    private String endTime;
}
