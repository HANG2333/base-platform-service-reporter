package com.jkr.modules.sys.menu.model.dto;

import lombok.Data;

import java.util.List;

/**
 * @author 曾令文
 */
@Data
public class MenuBtnDTO {

    private String parentCode;

    List<ChildBtn> code;
}
