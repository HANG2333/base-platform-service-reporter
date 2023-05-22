package com.jkr.modules.sys.role.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jkr.common.annotation.WebLog;
import com.jkr.common.constant.CommonConstant;
import com.jkr.common.model.ResponseData;
import com.jkr.core.base.controller.BaseController;
import com.jkr.modules.sys.role.model.SysRole;
import com.jkr.modules.sys.role.model.SysRoleMenu;
import com.jkr.modules.sys.role.model.dto.SysRoleMenuDTO;
import com.jkr.modules.sys.role.service.ISysRoleMenuService;
import com.jkr.modules.sys.role.service.ISysRoleService;
import com.jkr.utils.EscapeUtil;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author jikeruan
 * @since 2019-09-06
 */
@RestController
@RequestMapping("/sys/roleMenu")
@Api(value = "SysRoleMenuController", tags = {"角色操作接口"})
public class SysRoleMenuController extends BaseController<ISysRoleMenuService, SysRoleMenu> {

    @WebLog(description = "根据角色id查询")
    @GetMapping(value = "/getAuthMenuIdsById")
    public ResponseData<List<String>> getAuthMenuIdsById(String roleId) {
        return ResponseData.success(baseService.getAuthMenuIdsById(roleId));
    }


}
