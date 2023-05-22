package com.jkr.modules.sys.menu.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jkr.common.annotation.WebLog;
import com.jkr.common.model.ResponseData;
import com.jkr.core.base.controller.BaseController;
import com.jkr.modules.sys.dict.model.SysDict;
import com.jkr.modules.sys.menu.model.SysMenu;
import com.jkr.modules.sys.menu.model.dto.MenuDTO;
import com.jkr.modules.sys.menu.service.ISysMenuService;
import com.jkr.modules.sys.role.model.SysRole;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 菜单表 前端控制器
 * </p>
 *
 * @author jikeruan
 * @since 2019-09-06
 */
@RestController
@RequestMapping("/sys/menu")
public class SysMenuController extends BaseController<ISysMenuService, SysMenu> {

    /**
     * @title: treeList
     * @author: lzy
     * @date: 2022/4/12 15:05
     * @description: 菜单列表树形结构查询
     * @param: []
     * @return: com.jkr.common.model.ResponseData<java.util.List < com.jkr.modules.sys.menu.model.SysMenu>>
     */
    @WebLog(description = "菜单列表树形结构查询")
    @GetMapping(value = "/treeList")
    public ResponseData<List<SysMenu>> treeList() {
        return ResponseData.success(baseService.getMenuTreeTableList());
    }

    @WebLog(description = "菜单列表查询")
    @GetMapping(value = "/getAllMenuList")
    public ResponseData<List<SysMenu>> getAllMenuList() {
        return ResponseData.success(baseService.list(new QueryWrapper<>()));
    }

    /**
     * @title: list
     * @author: lzy
     * @date: 2022/4/12 20:10
     * @description: 菜单列表查询
     * @param: [menuDTO]
     * @return: com.jkr.common.model.ResponseData<java.util.List < com.jkr.modules.sys.menu.model.SysMenu>>
     */
    @WebLog(description = "菜单列表查询")
    @GetMapping(value = "/list")
    public ResponseData<List<SysMenu>> list(MenuDTO menuDTO) {
        return ResponseData.success(baseService.menuList(menuDTO));
    }

    @WebLog(description = "菜单保存修改")
    @PostMapping(value = "/saveOrUpdate")
    @Override
    public ResponseData<Boolean> saveOrUpdate(@RequestBody SysMenu sysMenu) {
        return ResponseData.success(baseService.saveOrUpdate(sysMenu));
    }

}
