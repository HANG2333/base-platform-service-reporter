package com.jkr.modules.sys.area.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jkr.common.constant.CommonConstant;
import com.jkr.common.context.BaseContextHandler;
import com.jkr.core.base.service.impl.BaseServiceImpl;
import com.jkr.modules.sys.area.mapper.SysAreaMapper;
import com.jkr.modules.sys.area.model.SysArea;
import com.jkr.modules.sys.area.service.ISysAreaService;
import com.jkr.utils.EscapeUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 行政区划管理-服务实现类
 *
 * @author zenglingwen
 * @since 2022-04-11
 */
@Service
public class SysAreaServiceImpl extends BaseServiceImpl<SysAreaMapper, SysArea> implements ISysAreaService {

    /**
     * @title: getAreaTreeTableList
     * @author: zenglingwen
     * @date: 2022年04月11日 16:20:23
     * @description: 行政区划列表树形查询
     * @param: sysArea
     * @return: java.util.List<com.jkr.modules.sys.area.model.SysArea>
     */
    @Override
    public List<SysArea> getAreaTreeTableList(SysArea sysArea) {
        List<SysArea> sysAreaList;
        QueryWrapper<SysArea> wrapper = new QueryWrapper<>();
        // 树形结构，如果输入了查询条件，就按列表展示
        if (StrUtil.isNotBlank(sysArea.getName())) {
            wrapper.like("name", EscapeUtil.escapeChar(sysArea.getName()));
        }
        if (StrUtil.isNotBlank(sysArea.getCode())) {
            wrapper.like("code", EscapeUtil.escapeChar(sysArea.getCode()));
        }
        if (StrUtil.isNotBlank(sysArea.getName()) || StrUtil.isNotBlank(sysArea.getCode())) {
            sysAreaList = baseMapper.selectList(wrapper);
            return sysAreaList;
        } else {
            wrapper.orderByAsc("location");
            wrapper.eq("parent_id","000000000000");
            sysAreaList = baseMapper.selectList(wrapper);
            /*List<SysArea> treeDataList = new ArrayList<>();
            sysAreaList.forEach(result -> {
                if (CommonConstant.DEFAULT_PARENT_VAL.equalsIgnoreCase(result.getParentId())) {
                    treeDataList.add(result);
                }
            });
            recursionTreeTableChildren(treeDataList, sysAreaList);*/
            return lazyList(sysAreaList);
        }
    }

    /**
     * 根据id查询子集
     *
     * @param id
     * @return java.util.List<com.jkr.modules.sys.area.model.SysArea>
     * @author jijinming
     * @date 16:40:22
     * @since
     */
    @Override
    public List<SysArea> searchListByParentId(String id) {
        List<SysArea> childList = this.list(new LambdaQueryWrapper<SysArea>().eq(SysArea::getParentId, id));
        return lazyList(childList);
    }

    /**
     * 判断是否有子集
     *
     * @param sysAreaList
     * @return java.util.List<com.jkr.modules.sys.area.model.SysArea>
     * @author jijinming
     * @date 16:40:44
     * @since
     */
    public List<SysArea> lazyList(List<SysArea> sysAreaList) {
        List<SysArea> sysAreas = this.list(new LambdaQueryWrapper<SysArea>().ne(SysArea::getParentId, "000000000000"));
        for (SysArea sysArea : sysAreaList) {
            for (SysArea area : sysAreas) {
                if (sysArea.getId().equals(area.getParentId())) {
                    sysArea.setHasChildren(true);
                }
            }
        }
        return sysAreaList;
    }

    /**
     * @title: searchList
     * @author: zenglingwen
     * @date: 2022年04月11日 16:20:32
     * @description: 查询行政区划列表，用于上级行政区划树形结构
     * @param: sysArea
     * @return: java.util.List<com.jkr.modules.sys.area.model.SysArea>
     */
    @Override
    public List<SysArea> searchList(SysArea sysArea) {
        QueryWrapper<SysArea> queryWrapper = new QueryWrapper<>();
        return baseMapper.selectList(queryWrapper);
    }

    /**
     * @title: saveOrUpdate
     * @author: zenglingwen
     * @date: 2022年04月11日 16:20:41
     * @description: 保存或更新行政区划信息
     * @param: sysArea
     * @return: boolean
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveOrUpdate(SysArea sysArea) {
        // 获取parentId，用于新增或更新时的parentIds赋值
        String parentId = sysArea.getParentId();
        String parentIds = "0";
        if (StrUtil.isNotBlank(parentId)) {
            QueryWrapper<SysArea> queryWrapper = new QueryWrapper<>();
            // QueryWrapper如果select的字段为null，则整个查询结果为null
            queryWrapper.select("id", "parent_ids").eq("id", parentId);
            SysArea parentArea = super.getOne(queryWrapper);
            if (null != parentArea) {
                parentIds = parentArea.getParentIds() + "," + sysArea.getParentId();
            }
        } else {
            parentId = "0";
        }
        sysArea.setParentId(parentId);
        sysArea.setParentIds(parentIds);
        return super.saveOrUpdate(sysArea);
    }

    /**
     * @title: deleteSelfAndChildren
     * @author: zenglingwen
     * @date: 2022年04月11日 16:20:53
     * @description: 删除自己及下级
     * @param: id
     * @return: boolean
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteSelfAndChildren(String id) {
        SysArea sysArea = new SysArea();
        sysArea.setId(id);
        sysArea.setUpdateBy(BaseContextHandler.getUserID());
        sysArea.setUpdateDate(LocalDateTime.now());
        return baseMapper.deleteSelfAndChildren(sysArea);
    }

    /**
     * @title: checkNameUnique
     * @author: zenglingwen
     * @date: 2022年04月11日 16:21:05
     * @description: 验证行政区划名称唯一
     * @param: id
     * @param: name
     * @return: boolean
     */
    @Override
    public boolean checkNameUnique(String id, String name) {
        QueryWrapper<SysArea> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id")
                .eq("name", name)
                .last("limit 1");
        if (StrUtil.isNotBlank(id)) {
            queryWrapper.ne("id", id);
        }
        SysArea sysArea = super.getOne(queryWrapper);
        return sysArea == null;
    }

    /**
     * @title: checkCodeUnique
     * @author: zenglingwen
     * @date: 2022年04月11日 16:21:18
     * @description: 验证行政区划编码唯一
     * @param: id
     * @param: code
     * @return: boolean
     */
    @Override
    public boolean checkCodeUnique(String id, String code) {
        QueryWrapper<SysArea> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id")
                .eq("code", code)
                .last("limit 1");
        if (StrUtil.isNotBlank(id)) {
            queryWrapper.ne("id", id);
        }
        SysArea sysArea = super.getOne(queryWrapper);
        return sysArea == null;
    }


    /**
     * @title: recursionTreeTableChildren
     * @author: zenglingwen
     * @date: 2022年04月11日 16:18:32
     * @description: 递归构造行政区划树形结构
     * @param: treeDataList
     * @param: sysAreaList
     * @return: void
     */
    private void recursionTreeTableChildren(List<SysArea> treeDataList, List<SysArea> sysAreaList) {
        for (SysArea treeData : treeDataList) {
            List<SysArea> childrenList = new ArrayList<>();
            for (SysArea sysArea : sysAreaList) {
                if (sysArea.getParentId().equals(treeData.getId())) {
                    childrenList.add(sysArea);
                }
            }
            if (!CollUtil.isEmpty(childrenList)) {
                treeData.setChildren(childrenList);
                recursionTreeTableChildren(childrenList, sysAreaList);
            }
        }
    }


    /**
     * @title: getAreaByType
     * @author: wanghe
     * @date: 2022/4/30 9:51
     * @description: 通过type查询区域
     * @param type
     * @param parentId
     * @return: java.util.List<com.jkr.modules.sys.area.model.SysArea>
     */
    @Override
    public List<SysArea> getAreaByType(String type,String parentId) {
        return baseMapper.getAreaByType(type,parentId);
    }


    /**
     * @title: areaList
     * @author: py
     * @date: 2022/5/6 10:03
     * @description: 查询行政区划
     * @param: []
     * @return: java.util.List<com.jkr.modules.sys.area.model.SysArea>
     */
    @Override
    public List<SysArea> areaList() {
        List<SysArea> provinceList = this.findListForAreaTopThree("2");
        List<SysArea> cityList = this.findListForAreaTopThree("3");
        List<SysArea> countyList = this.findListForAreaTopThree("4");

        return this.dealWithAreaList(provinceList, cityList, countyList);
    }



    /**
     * @title: findListForAreaTopThree
     * @author: py
     * @date: 2022/5/6 10:01
     * @description: 根据type查询行政区划
     * @param: [type]
     * @return: java.util.List<com.jkr.modules.sys.area.model.SysArea>
     */
    public List<SysArea> findListForAreaTopThree(String type) {
        LambdaQueryWrapper<SysArea> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysArea::getType,type);
        List<SysArea> areaList =  this.list(queryWrapper);
        return areaList;
    }

    /**
     * @title: dealWithAreaList
     * @author: py
     * @date: 2022/5/6 9:58
     * @description:  处理省市县行政区划为指定格式
     * @param: [provinceList, cityList, countyList]
     * @return: java.util.List<com.jkr.modules.sys.area.model.SysArea>
     */
    public List<SysArea> dealWithAreaList(List<SysArea> provinceList, List<SysArea> cityList, List<SysArea> countyList){
        List<SysArea> resultList = new ArrayList<>(16);
        if(CollectionUtils.isNotEmpty(cityList)){
            for (SysArea secondRank : cityList) {
                List<SysArea> thirdNodeList = new ArrayList<SysArea>();
                if(CollectionUtils.isNotEmpty(countyList)){
                    for (SysArea thirdRank : countyList) {
                        if (thirdRank.getParentId().equals(secondRank.getId())) {
                            thirdNodeList.add(thirdRank);
                        }
                        secondRank.setChildren(thirdNodeList);
                    }
                }
            }
        }
        if(CollectionUtils.isNotEmpty(provinceList)){
            for (SysArea firstRank : provinceList) {
                List<SysArea> secondNodeList = new ArrayList<SysArea>();
                if(CollectionUtils.isNotEmpty(cityList)){
                    for (SysArea secondRank : cityList) {
                        if (firstRank.getId().equals(secondRank.getParentId())) {
                            secondNodeList.add(secondRank);
                        }
                    }
                }
                firstRank.setChildren(secondNodeList);
            }
            resultList.addAll(provinceList);
        }else{
            resultList.addAll(cityList);
        }

        return resultList;
    }
}
