package com.jkr.modules.sys.role.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jkr.common.annotation.WebLog;
import com.jkr.common.model.ResponseData;
import com.jkr.core.base.controller.BaseController;
import com.jkr.modules.sys.role.model.SysRole;
import com.jkr.modules.sys.role.model.dto.SysRoleMenuDTO;
import com.jkr.modules.sys.role.service.ISysRoleService;
import com.jkr.utils.EscapeUtil;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author jikeruan
 * @since 2019-09-06
 */
@RestController
@RequestMapping("/sys/role")
@Api(value = "SysRoleController", tags = {"角色操作接口"})
public class SysRoleController extends BaseController<ISysRoleService, SysRole> {

    @WebLog(description = "角色管理-列表查询")
    @GetMapping(value = "/page")
    @Override
    public ResponseData<IPage<SysRole>> page(SysRole sysRole) {
        sysRole.setName(EscapeUtil.escapeChar(sysRole.getName()));
        return ResponseData.success(baseService.pageList(sysRole));
    }

    @WebLog(description = "角色管理-验证名称唯一")
    @PostMapping(value = "/checkNameUnique")
    public ResponseData<Boolean> checkNameUnique(@RequestBody SysRole sysRole) {
        return ResponseData.success(baseService.checkColumnUnique(sysRole));
    }

    @WebLog(description = "角色管理-验证编码唯一")
    @PostMapping(value = "/checkCodeUnique")
    public ResponseData<Boolean> checkCodeUnique(@RequestBody SysRole sysRole) {
        return ResponseData.success(baseService.checkColumnUnique(sysRole));
    }

    @WebLog(description = "角色管理-新增保存角色")
    @PostMapping(value = "/saveOrUpdate")
    @Override
    public ResponseData<Boolean> saveOrUpdate(@RequestBody SysRole sysRole) {
        return ResponseData.success(baseService.saveOrUpdate(sysRole));
    }

    @WebLog(description = "角色管理-通过Id删除角色")
    @GetMapping(value = "/deleteRoleById")
    public ResponseData<Boolean> deleteRoleById(String roleId) {
        return ResponseData.success(baseService.deleteRoleById(roleId));
    }

    @WebLog(description = "角色管理-保存角色菜单授权")
    @PostMapping(value = "/saveAuthMenu")
    public ResponseData<Boolean> saveAuthMenu(@RequestBody SysRoleMenuDTO sysRoleDTO) {
        return ResponseData.success(baseService.saveAuthMenu(sysRoleDTO));
    }


}
