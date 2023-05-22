package com.jkr.modules.sys.area.controller;


import com.jkr.common.annotation.WebLog;
import com.jkr.common.model.ResponseData;
import com.jkr.core.base.controller.BaseController;
import com.jkr.modules.sys.area.model.SysArea;
import com.jkr.modules.sys.area.service.ISysAreaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 行政区划管理-前端控制器
 *
 * @author zenglingwen
 * @since 2022-04-11
 */
@RestController
@RequestMapping("/sys/area")
public class SysAreaController extends BaseController<ISysAreaService, SysArea> {

    /**
     * @title: treeList
     * @author: zenglingwen
     * @date: 2022年04月11日 16:04:12
     * @description: 行政区划列表树形查询
     * @param: sysArea
     * @return: com.jkr.common.model.ResponseData<java.util.List < com.jkr.modules.sys.area.model.SysArea>>
     */
    @WebLog(description = "行政区划列表树形查询")
    @GetMapping(value = "/treeList")
    public ResponseData<List<SysArea>> treeList(SysArea sysArea) {
        return ResponseData.success(baseService.getAreaTreeTableList(sysArea));
    }

    /**
     * @title: list
     * @author: zenglingwen
     * @date: 2022年04月11日 16:16:42
     * @description: 查询行政区划列表，用于上级行政区划树形结构
     * @param: sysArea
     * @return: com.jkr.common.model.ResponseData<java.util.List < com.jkr.modules.sys.area.model.SysArea>>
     */
    @WebLog(description = "查询行政区划列表，用于上级行政区划树形结构")
    @GetMapping(value = "/list")
    public ResponseData<List<SysArea>> list(SysArea sysArea) {
        return ResponseData.success(baseService.searchList(sysArea));
    }

    /**
     * @title: saveOrUpdate
     * @author: zenglingwen
     * @date: 2022年04月11日 16:06:20
     * @description: 保存或更新行政区划信息
     * @param: sysArea
     * @return: com.jkr.common.model.ResponseData<java.lang.Boolean>
     */
    @WebLog(description = "保存或更新行政区划信息")
    @PostMapping(value = "/saveOrUpdate")
    @Override
    public ResponseData<Boolean> saveOrUpdate(@RequestBody SysArea sysArea) {
        return ResponseData.success(baseService.saveOrUpdate(sysArea));
    }

    /**
     * @title: deleteSelfAndChildren
     * @author: zenglingwen
     * @date: 2022年04月11日 16:13:38
     * @description: 删除自己及下级
     * @param: id
     * @return: com.jkr.common.model.ResponseData<java.lang.Boolean>
     */
    @WebLog(description = "删除自己及下级")
    @PostMapping(value = "/deleteSelfAndChildren")
    public ResponseData<Boolean> deleteSelfAndChildren(@RequestParam String id) {
        return ResponseData.success(baseService.deleteSelfAndChildren(id));
    }

    /**
     * @title: checkNameUnique
     * @author: zenglingwen
     * @date: 2022年04月11日 16:13:56
     * @description: 验证行政区划名称唯一
     * @param: id
     * @param: name
     * @return: com.jkr.common.model.ResponseData<java.lang.Boolean>
     */
    @WebLog(description = "验证行政区划名称唯一")
    @PostMapping(value = "/checkNameUnique")
    public ResponseData<Boolean> checkNameUnique(@RequestParam(value = "id", required = false) String id, @RequestParam("name") String name) {
        return ResponseData.success(baseService.checkNameUnique(id, name));
    }

    /**
     * @title: checkCodeUnique
     * @author: zenglingwen
     * @date: 2022年04月11日 16:13:59
     * @description: 验证行政区划编码唯一
     * @param: id
     * @param: code
     * @return: com.jkr.common.model.ResponseData<java.lang.Boolean>
     */
    @WebLog(description = "验证行政区划编码唯一")
    @PostMapping(value = "/checkCodeUnique")
    public ResponseData<Boolean> checkCodeUnique(@RequestParam(value = "id", required = false) String id, @RequestParam("code") String code) {
        return ResponseData.success(baseService.checkCodeUnique(id, code));
    }


    /**
     * @title: getAreaByType
     * @author: py
     * @date: 2022/4/29 18:56
     * @description: 根据type parentId 获取行政区划
     * @param: [type, parentId]
     * @return: com.jkr.common.model.ResponseData
     */
    @WebLog(description = "根据type parentId 获取行政区划")
    @PostMapping(value = "/getAreaByType")
    public ResponseData getAreaByType(@RequestParam String type, @RequestParam String parentId) {
        return ResponseData.success(baseService.getAreaByType(type, parentId));
    }


    /**
     * @title: areaList
     * @author: py
     * @date: 2022/5/6 10:04
     * @description: 查询行政区划
     * @param: []
     * @return: com.jkr.common.model.ResponseData
     */
    @WebLog(description = "查询行政区划")
    @PostMapping(value = "/areaList")
    public ResponseData areaList() {
        return ResponseData.success(baseService.areaList());
    }

    /**
     * 懒加载 通过id查询子节点
     *
     * @param id
     * @return com.jkr.common.model.ResponseData<java.util.List < com.jkr.modules.sys.area.model.SysArea>>
     * @author jijinming
     * @date 16:39:43
     * @since
     */
    @WebLog(description = "懒加载 通过id查询子节点")
    @GetMapping(value = "/searchListByParentId")
    public ResponseData<List<SysArea>> list(@RequestParam String id) {
        return ResponseData.success(baseService.searchListByParentId(id));
    }



}