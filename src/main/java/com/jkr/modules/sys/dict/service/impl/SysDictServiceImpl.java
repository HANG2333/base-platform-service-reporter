package com.jkr.modules.sys.dict.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jkr.common.constant.CommonConstant;
import com.jkr.common.context.BaseContextHandler;
import com.jkr.core.base.service.impl.BaseServiceImpl;
import com.jkr.modules.sys.dict.mapper.SysDictMapper;
import com.jkr.modules.sys.dict.model.SysDict;
import com.jkr.modules.sys.dict.service.ISysDictService;
import com.jkr.utils.EscapeUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 字典管理-服务实现类
 *
 * @author zenglingwen
 * @since 2022-04-11
 */
@Service
public class SysDictServiceImpl extends BaseServiceImpl<SysDictMapper, SysDict> implements ISysDictService {

    /**
     * @title: getDictTreeTableList
     * @author: zenglingwen
     * @date: 2022年04月11日 15:39:46
     * @description: 字典列表树形查询
     * @param: dict
     * @return: java.util.List<com.jkr.modules.sys.dict.model.SysDict>
     */
    @Override
    public List<SysDict> getDictTreeTableList(SysDict dict) {
        QueryWrapper<SysDict> wrapper = new QueryWrapper<>();
        List<SysDict> dictList;
        // 树形结构，如果输入了查询条件，就按列表展示
        // 字典名称
        if (StrUtil.isNotBlank(dict.getDicValue())) {
            wrapper.like("dic_value", EscapeUtil.escapeChar(dict.getDicValue()));
        }
        // 字典类型
        if (StrUtil.isNotBlank(dict.getDicType())) {
            wrapper.like("dic_type", EscapeUtil.escapeChar(dict.getDicType()));
        }
        // 字典键值
        if (StrUtil.isNotBlank(dict.getDicCode())) {
            wrapper.like("dic_code", EscapeUtil.escapeChar(dict.getDicCode()));
        }
        boolean listFlag = StrUtil.isNotBlank(dict.getDicValue()) || StrUtil.isNotBlank(dict.getDicType()) || StrUtil.isNotBlank(dict.getDicCode());
        dictList = baseMapper.selectList(wrapper.orderByAsc("location"));
        if (listFlag) {
            return dictList;
        } else {
            // 获取所有字典项
            List<SysDict> treeDataList = new ArrayList<>();
            dictList.forEach(sysDict -> {
                if (CommonConstant.DEFAULT_PARENT_VAL.equalsIgnoreCase(sysDict.getParentId())) {
                    treeDataList.add(sysDict);
                }
            });
            recursionTreeTableChildren(treeDataList, dictList);
            return treeDataList;
        }
    }

    /**
     * @title: searchList
     * @author: zenglingwen
     * @date: 2022年04月11日 15:40:24
     * @description: 查询字典列表，用于父级字典树形结构
     * @param: dict
     * @return: java.util.List<com.jkr.modules.sys.dict.model.SysDict>
     */
    @Override
    public List<SysDict> searchList(SysDict dict) {
        QueryWrapper<SysDict> queryWrapper = new QueryWrapper<>();
        return baseMapper.selectList(queryWrapper);
    }

    /**
     * @title: deleteSelfAndChildren
     * @author: zenglingwen
     * @date: 2022年04月11日 15:40:42
     * @description: 删除自己及下级
     * @param: id
     * @return: boolean
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteSelfAndChildren(String id) {
        SysDict sysDict = new SysDict();
        sysDict.setId(id);
        sysDict.setUpdateBy(BaseContextHandler.getUserID());
        sysDict.setUpdateDate(LocalDateTime.now());
        return baseMapper.deleteSelfAndChildren(sysDict);
    }

    @Override
    public List<SysDict> searchChildrenListByType(SysDict sysDict) {
        QueryWrapper<SysDict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dic_type",sysDict.getDicType());
        queryWrapper.ne("parent_id",CommonConstant.DEFAULT_PARENT_VAL);
        queryWrapper.orderByAsc("location");
        return baseMapper.selectList(queryWrapper);
    }

    /**
     * @title: saveOrUpdate
     * @author: zenglingwen
     * @date: 2022年04月11日 15:40:57
     * @description: 保存或修改字典信息
     * @param: sysDict
     * @return: boolean
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveOrUpdate(SysDict sysDict) {
        // 获取parentId，用于新增或更新时的parentIds赋值
        String parentId = sysDict.getParentId();
        String parentIds = "0";
        if (StrUtil.isNotBlank(parentId)) {
            QueryWrapper<SysDict> queryWrapper = new QueryWrapper<>();
            // QueryWrapper如果select的字段为null，则整个查询结果为null
            queryWrapper.select("id", "parent_ids").eq("id", parentId);
            SysDict parentDict = super.getOne(queryWrapper);
            if (null != parentDict) {
                parentIds = parentDict.getParentIds() + "," + sysDict.getParentId();
            }
        } else {
            parentId = "0";
        }
        sysDict.setParentId(parentId);
        sysDict.setParentIds(parentIds);
        return super.saveOrUpdate(sysDict);
    }

    /**
     * @title: checkColumnUnique
     * @author: zenglingwen
     * @date: 2022年04月11日 15:41:21
     * @description: 验证唯一性
     * @param: sysDict
     * @return: boolean
     */
    @Override
    public boolean checkColumnUnique(SysDict sysDict) {
        QueryWrapper<SysDict> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id");
        queryWrapper.ne("parent_id", "0");
        queryWrapper.last("limit 1");
        if (StrUtil.isNotBlank(sysDict.getDicType())) {
            queryWrapper.eq("dic_type", sysDict.getDicType());
            if (StrUtil.isNotBlank(sysDict.getDicCode())) {
                queryWrapper.eq("dic_code", sysDict.getDicCode());
            } else if (StrUtil.isNotBlank(sysDict.getDicValue())) {
                queryWrapper.eq("dic_value", sysDict.getDicValue());
            }
        }
        if (StrUtil.isNotBlank(sysDict.getId())) {
            queryWrapper.ne("id", sysDict.getId());
        }
        SysDict selectSysDict = super.getOne(queryWrapper);
        return selectSysDict == null;
    }

    /**
     * @title: recursionTreeTableChildren
     * @author: zenglingwen
     * @date: 2022年04月11日 15:41:40
     * @description: 构造树形
     * @param: treeDataList
     * @param: dictList
     * @return: void
     */
    private void recursionTreeTableChildren(List<SysDict> treeDataList, List<SysDict> dictList) {
        for (SysDict treeData : treeDataList) {
            List<SysDict> childrenList = new ArrayList<>();
            for (SysDict sysDict : dictList) {
                if (sysDict.getParentId().equals(treeData.getId())) {
                    childrenList.add(sysDict);
                }
            }
            if (!CollUtil.isEmpty(childrenList)) {
                treeData.setChildren(childrenList);
                recursionTreeTableChildren(childrenList, dictList);
            }
        }
    }
}
