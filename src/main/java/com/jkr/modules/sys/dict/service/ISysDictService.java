package com.jkr.modules.sys.dict.service;

import com.jkr.core.base.service.BaseService;
import com.jkr.modules.sys.dict.model.SysDict;

import java.util.List;

/**
 * 字典管理-服务类
 *
 * @author zenglingwen
 * @since 2022-04-11
 */
public interface ISysDictService extends BaseService<SysDict> {

    /**
     * @title: getDictTreeTableList
     * @author: zenglingwen
     * @date: 2022年04月11日 15:39:46
     * @description: 字典列表树形查询
     * @param: dict
     * @return: java.util.List<com.jkr.modules.sys.dict.model.SysDict>
     */
    List<SysDict> getDictTreeTableList(SysDict dict);

    /**
     * @title: searchList
     * @author: zenglingwen
     * @date: 2022年04月11日 15:40:24
     * @description: 查询字典列表，用于父级字典树形结构
     * @param: dict
     * @return: java.util.List<com.jkr.modules.sys.dict.model.SysDict>
     */
    List<SysDict> searchList(SysDict dict);


    /**
     * @title: saveOrUpdate
     * @author: zenglingwen
     * @date: 2022年04月11日 15:40:57
     * @description: 保存或修改字典信息
     * @param: sysDict
     * @return: boolean
     */
    @Override
    boolean saveOrUpdate(SysDict sysDict);

    /**
     * @title: checkColumnUnique
     * @author: zenglingwen
     * @date: 2022年04月11日 15:41:21
     * @description: 验证唯一性
     * @param: sysDictDTO
     * @return: boolean
     */
    boolean checkColumnUnique(SysDict sysDict);

    /**
     * @title: deleteSelfAndChildren
     * @author: zenglingwen
     * @date: 2022年04月11日 15:40:42
     * @description: 删除自己及下级
     * @param: id
     * @return: boolean
     */
    boolean deleteSelfAndChildren(String id);

    /**
     * @title: searchChildrenListByType
     * @author: lzy
     * @date: 2022/4/12 16:22
     * @description: 根据type查询字典项子集
     * @param: 
     * @return: 
     */
    List<SysDict> searchChildrenListByType(SysDict sysDict);
}
