package com.jkr.modules.sys.file.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.jkr.core.base.model.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author jikeruan
 * @since 2019-09-18
 */

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_file")
@ApiModel(value = "FileInfo对象", description = "附件")
public class FileInfo extends BaseModel implements Serializable {

    @ApiModelProperty(value = "关联业务主表Id")
    private String businessId;

    @ApiModelProperty(value = "关联业务主表表名")
    private String businessTable;

    @ApiModelProperty(value = "附件名称")
    private String fileName;

    @ApiModelProperty(value = "附件类型")
    private String fileType;

    @ApiModelProperty(value = "附件大小（字节）")
    private Long fileSize;

    @ApiModelProperty(value = "物理路径")
    private String filePath;

    @ApiModelProperty(value = "URL访问地址")
    private String fileUrl;

    @ApiModelProperty(value = "URL访问地址")
    private String url;

}
