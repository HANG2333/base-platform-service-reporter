package com.jkr.modules.sys.unit.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jkr.common.annotation.WebLog;
import com.jkr.common.constant.CommonConstant;
import com.jkr.common.model.ResponseData;
import com.jkr.core.base.controller.BaseController;
import com.jkr.modules.sys.unit.model.SysUnit;
import com.jkr.modules.sys.unit.service.ISysUnitService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 单位表 前端控制器
 * </p>
 *
 * @author jikeruan
 * @since 2020-05-21
 */
@RestController
@RequestMapping("/sys/unit")
public class SysUnitController extends BaseController<ISysUnitService, SysUnit> {

    /**
     * @title: treeList
     * @author: SongYh
     * @date: 2022/4/11 11:15
     * @description: 机构列表树形结构查询
     * @param: * @param null
     * @return:
     */
    @WebLog(description = "机构列表树形结构查询")
    @GetMapping(value = "/treeList")
    public ResponseData<List<SysUnit>> treeList(SysUnit sysUnit) {
        return ResponseData.success(baseService.getUnitTreeTableList(sysUnit));
    }

    /**
     * @title: findAllList
     * @author: SongYh
     * @date: 2022/4/8 11:04
     * @description: 查询平布单位数据
     * @param: * @param null
     * @return:
     */
    @WebLog(description = "查询平布单位数据")
    @GetMapping(value = "/findAllList")
    public ResponseData<List<SysUnit>> findAllList() {
        return ResponseData.success(baseService.list(new LambdaQueryWrapper<SysUnit>().eq(SysUnit :: getDelFlag, CommonConstant.STATUS_NUMBER)));
    }

    /**
     * @title: findUnitDataById
     * @author: SongYh
     * @date: 2022/4/11 15:59
     * @description: 根据id查询机构详情（适用于修改和查看页面）
     * @param: * @param null
     * @return:
     */
    @WebLog(description = "根据id查询机构详情（适用于修改和查看页面）")
    @GetMapping(value = "/findUnitDataById")
    public ResponseData<SysUnit> findUnitDataById(@RequestParam("id") String id) {
        return ResponseData.success(baseService.findUnitDataById(id));
    }

    /**
     * @title: saveOrUpdateUnit
     * @author: SongYh
     * @date: 2022/4/11 17:43
     * @description: 新增、编辑机构
     * @param: * @param null
     * @return:
     */
    @WebLog(description = "新增、编辑机构")
    @PostMapping(value = "/saveOrUpdateUnit")
    public ResponseData<Boolean> saveOrUpdateUnit(@RequestBody SysUnit sysUnit) {
        return ResponseData.success(baseService.saveOrUpdateUnit(sysUnit));
    }

    /**
     * @title: deleteUnitDataById
     * @author: SongYh
     * @date: 2022/4/11 18:03
     * @description: 根据id删除机构数据，有子节点也都删除
     * @param: * @param null
     * @return:
     */
    @WebLog(description = "根据id删除机构数据，有子节点也都删除")
    @RequestMapping(value = "/deleteUnitDataById")
    public ResponseData<Boolean> deleteUnitDataById(@RequestParam String id) {
        return ResponseData.success(baseService.deleteUnitDataById(id));
    }

    /**
     * 根据id查询当前以及下级数据
     *
     * @param id
     * @return java.util.List<com.jkr.modules.sys.unit.model.SysUnit>
     * @author jijinming
     * @date 15:30:16
     * @since
     */
    @WebLog(description = "根据id查询当前以及下级数据")
    @GetMapping(value = "/getUnitListById")
    public ResponseData<List<SysUnit>> getUnitListById(String id) {
        return ResponseData.success(baseService.getUnitListById(id));
    }
}
