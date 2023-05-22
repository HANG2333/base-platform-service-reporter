package com.jkr.modules.sys.unit.service.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jkr.common.constant.CommonConstant;
import com.jkr.common.context.BaseContextHandler;
import com.jkr.core.base.service.impl.BaseServiceImpl;
import com.jkr.modules.sys.unit.mapper.SysUnitMapper;
import com.jkr.modules.sys.unit.model.SysUnit;
import com.jkr.modules.sys.unit.service.ISysUnitService;
import com.jkr.modules.sys.user.service.ISysUserService;
import com.jkr.modules.sys.user.service.ISysUserUnitService;
import com.jkr.utils.EscapeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 单位表 服务实现类
 * </p>
 *
 * @author jikeruan
 * @since 2020-05-21
 */
@Service
@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
public class SysUnitServiceImpl extends BaseServiceImpl<SysUnitMapper, SysUnit> implements ISysUnitService {

    @Autowired
    private ISysUserService sysUserInfoService;
    @Autowired
    private ISysUserUnitService iSysUserUnitService;

    /**
     * @title: treeList
     * @author: SongYh
     * @date: 2022/4/11 11:15
     * @description: 机构列表树形结构查询
     * @param: * @param null
     * @return:
     */
    @Override
    public List<SysUnit> getUnitTreeTableList(SysUnit sysUnit) {
        QueryWrapper<SysUnit> wrapper = new QueryWrapper<>();
        if (StrUtil.isNotBlank(sysUnit.getName())) {
            wrapper.like("name", EscapeUtil.escapeChar(sysUnit.getName()));
        }
        if (StrUtil.isNotBlank(sysUnit.getAreaName())){
            wrapper.like("area_name", EscapeUtil.escapeChar(sysUnit.getAreaName()));
        }
        wrapper.orderByAsc("location");
        List<SysUnit> sysUnitList = baseMapper.selectList(wrapper);
        // 如果带筛选条件，就返回列表数据，无tree结构
        if (StrUtil.isNotBlank(sysUnit.getName()) || StrUtil.isNotBlank(sysUnit.getAreaName())){
            return sysUnitList;
        }else{
            List<SysUnit> treeDataList = new ArrayList<>();
            sysUnitList.forEach(result -> {
                if (CommonConstant.DEFAULT_PARENT_VAL.equalsIgnoreCase(result.getParentId())) {
                    treeDataList.add(result);
                }
            });
            recursionTreeTableChildren(treeDataList, sysUnitList);
            return treeDataList;
        }
    }

    /**
     * @title: recursionTreeTableChildren
     * @author: SongYh
     * @date: 2022/4/11 11:16
     * @description: 递归单位树
     * @param: * @param null
     * @return:
     */
    private void recursionTreeTableChildren(List<SysUnit> treeDataList, List<SysUnit> sysUnitList) {
        for (SysUnit treeData : treeDataList) {
            List<SysUnit> childrenList = new ArrayList<>();
            for (SysUnit sysUnit : sysUnitList) {
                if (sysUnit.getParentId().equals(treeData.getId())) {
                    childrenList.add(sysUnit);
                }
            }
            if (!CollUtil.isEmpty(childrenList)) {
                treeData.setChildren(childrenList);
                recursionTreeTableChildren(childrenList, sysUnitList);
            }
        }
    }


    /**
     * @title: findUnitDataById
     * @author: SongYh
     * @date: 2022/4/11 15:59
     * @description: 根据id查询机构详情（适用于修改和查看页面）
     * @param: * @param null
     * @return:
     */
    @Override
    public SysUnit findUnitDataById(String id){
        QueryWrapper<SysUnit> wrapper = new QueryWrapper<>();
        wrapper.eq("unit.id",id);
        return baseMapper.findUnitDataById(wrapper);
    }

    /**
     * @title: saveOrUpdateUnit
     * @author: SongYh
     * @date: 2022/4/11 17:43
     * @description: 新增、编辑机构
     * @param: * @param null
     * @return:
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveOrUpdateUnit(SysUnit sysUnit) {
        // 获取parentId，用于新增或更新时的parentIds赋值
        String parentId = sysUnit.getParentId();
        String parentIds = CommonConstant.DEFAULT_PARENT_VAL;
        if (StrUtil.isNotBlank(parentId)) {
            QueryWrapper<SysUnit> queryWrapper = new QueryWrapper<>();
            // QueryWrapper如果select的字段为null，则整个查询结果为null
            queryWrapper.select("id", "parent_ids").eq("id", parentId);
            SysUnit parentUnit = super.getOne(queryWrapper);
            if (null != parentUnit) {
                parentIds = parentUnit.getParentIds() + "," + sysUnit.getParentId();
            }
        }
        sysUnit.setParentIds(parentIds);
        return super.saveOrUpdate(sysUnit);
    }

    /**
     * @title: deleteUnitDataById
     * @author: SongYh
     * @date: 2022/4/11 18:03
     * @description: 根据id删除机构数据，有子节点也都删除
     * @param: * @param null
     * @return:
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteUnitDataById(String id) {
        SysUnit sysUnit = new SysUnit();
        sysUnit.setId(id);
        sysUnit.setUpdateBy(BaseContextHandler.getUserID());
        sysUnit.setUpdateDate(LocalDateTime.now());
        return baseMapper.deleteUnitDataById(sysUnit);
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
    @Override
    public List<SysUnit> getUnitListById(String id) {
        List<SysUnit> sysUnitList = this.list(new LambdaQueryWrapper<SysUnit>()
                .eq(SysUnit::getId, id));
        List<SysUnit> sysUnitList1 = this.list(new LambdaQueryWrapper<SysUnit>()
                .orderByAsc(SysUnit::getLocation));
        if (!sysUnitList.isEmpty()) {
            this.addChildList(sysUnitList, sysUnitList1);
        }
        return sysUnitList;
    }

    /**
     * 树的子集添加数据
     *
     * @param sysUnitList
     * @param sysUnitList1
     * @return void
     * @author jijinming
     * @date 16:13:25
     * @since
     */
    public void addChildList(List<SysUnit> sysUnitList, List<SysUnit> sysUnitList1) {
        for (SysUnit sysUnit : sysUnitList) {
            List<SysUnit> childrenList = new ArrayList<>();
            for (SysUnit unit : sysUnitList1) {
                if (unit.getParentId().equals(sysUnit.getId())) {
                    childrenList.add(unit);
                }
            }
            if (!CollUtil.isEmpty(childrenList)) {
                sysUnit.setChildren(childrenList);
                addChildList(childrenList, sysUnitList1);
            }
        }
    }

}
