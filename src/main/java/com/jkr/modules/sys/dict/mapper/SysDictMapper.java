package com.jkr.modules.sys.dict.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jkr.modules.sys.dict.model.SysDict;

/**
 * 字典管理-Mapper 接口
 *
 * @author zenglingwen
 * @since 2022-04-11
 */
public interface SysDictMapper extends BaseMapper<SysDict> {

    /**
     * @title: deleteSelfAndChildren
     * @author: zenglingwen
     * @date: 2022年04月11日 15:40:42
     * @description: 删除自己及下级
     * @param: sysDict
     * @return: boolean
     */
    boolean deleteSelfAndChildren(SysDict sysDict);


}
