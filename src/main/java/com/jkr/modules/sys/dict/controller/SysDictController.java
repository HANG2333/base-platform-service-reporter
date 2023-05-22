package com.jkr.modules.sys.dict.controller;

import com.jkr.common.annotation.WebLog;
import com.jkr.common.model.ResponseData;
import com.jkr.core.base.controller.BaseController;
import com.jkr.modules.sys.dict.model.SysDict;
import com.jkr.modules.sys.dict.service.ISysDictService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 字典管理-前端控制器
 *
 * @author zenglingwen
 * @since 2022-04-11
 */
@RestController
@RequestMapping("/sys/dict")
public class SysDictController extends BaseController<ISysDictService, SysDict> {

    /**
     * @title: getDictTreeTableList
     * @author: zenglingwen
     * @date: 2022年04月11日 14:53:33
     * @description: 字典列表树形查询
     * @param: dict
     * @return: com.jkr.common.model.ResponseData<java.util.List < com.jkr.modules.sys.dict.model.SysDict>>
     */
    @WebLog(description = "字典列表树形查询")
    @GetMapping(value = "/getDictTreeTableList")
    public ResponseData<List<SysDict>> getDictTreeTableList(SysDict sysDict) {
        return ResponseData.success(baseService.getDictTreeTableList(sysDict));
    }

    /**
     * @title: list
     * @author: zenglingwen
     * @date: 2022年04月11日 14:55:04
     * @description: 查询字典列表，用于父级字典树形结构
     * @param: dict
     * @return: com.jkr.common.model.ResponseData<java.util.List < com.jkr.modules.sys.dict.model.SysDict>>
     */
    @WebLog(description = "查询字典列表，用于父级字典树形结构")
    @GetMapping(value = "/list")
    public ResponseData<List<SysDict>> list(SysDict sysDict) {
        return ResponseData.success(baseService.searchList(sysDict));
    }

    /**
     * @title: deleteSelfAndChildren
     * @author: zenglingwen
     * @date: 2022年04月11日 14:55:21
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
     * @title: saveOrUpdate
     * @author: zenglingwen
     * @date: 2022年04月11日 14:55:37
     * @description: 保存或修改字典信息
     * @param: sysDict
     * @return: com.jkr.common.model.ResponseData<java.lang.Boolean>
     */
    @WebLog(description = "保存或修改字典信息")
    @PostMapping(value = "/saveOrUpdate")
    @Override
    public ResponseData<Boolean> saveOrUpdate(@RequestBody SysDict sysDict) {
        return ResponseData.success(baseService.saveOrUpdate(sysDict));
    }

    /**
     * @title: checkTypeUnique
     * @author: zenglingwen
     * @date: 2022年04月11日 15:12:15
     * @description: 检查字典类型是否唯一
     * @param: sysDict
     * @return: com.jkr.common.model.ResponseData<java.lang.Boolean>
     */
    @WebLog(description = "检查字典类型是否唯一")
    @PostMapping(value = "/checkTypeUnique")
    public ResponseData<Boolean> checkTypeUnique(@RequestBody SysDict sysDict) {
        return ResponseData.success(baseService.checkColumnUnique(sysDict));
    }

    /**
     * @title: checkValueUniqueWithType
     * @author: zenglingwen
     * @date: 2022年04月11日 15:12:23
     * @description: 检查同一字典类型，字典名称是否唯一
     * @param: sysDict
     * @return: com.jkr.common.model.ResponseData<java.lang.Boolean>
     */
    @WebLog(description = "检查同一字典类型，字典名称是否唯一")
    @PostMapping(value = "/checkValueUniqueWithType")
    public ResponseData<Boolean> checkValueUniqueWithType(@RequestBody SysDict sysDict) {
        return ResponseData.success(baseService.checkColumnUnique(sysDict));
    }

    /**
     * @title: checkCodeUniqueWithType
     * @author: zenglingwen
     * @date: 2022年04月11日 15:12:27
     * @description: 检查同一字典类型，字典键值是否唯一
     * @param: sysDict
     * @return: com.jkr.common.model.ResponseData<java.lang.Boolean>
     */
    @WebLog(description = "检查同一字典类型，字典键值是否唯一")
    @PostMapping(value = "/checkCodeUniqueWithType")
    public ResponseData<Boolean> checkCodeUniqueWithType(@RequestBody SysDict sysDict) {
        return ResponseData.success(baseService.checkColumnUnique(sysDict));
    }
    @WebLog(description = "根据type查询字典项子集")
    @GetMapping(value = "/searchChildrenListByType")
    public ResponseData<List<SysDict>> searchChildrenListByType(SysDict sysDict) {
        return ResponseData.success(baseService.searchChildrenListByType(sysDict));
    }

    /**
     * 获取字典项
     *
     * @param sysDict
     * @return com.jkr.common.model.ResponseData<java.util.List < com.jkr.modules.sys.dict.model.SysDict>>
     * @author jijinming
     * @date 11:39:03
     * @since
     */
    @WebLog(description = "获取字典项")
    @PostMapping(value = "/findChildrenListByType")
    public ResponseData<List<SysDict>> findChildrenListByType(@RequestBody SysDict sysDict) {
        return ResponseData.success(baseService.searchChildrenListByType(sysDict));
    }
}
