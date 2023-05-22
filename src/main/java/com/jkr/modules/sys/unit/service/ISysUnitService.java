package com.jkr.modules.sys.unit.service;

import com.jkr.core.base.service.BaseService;
import com.jkr.modules.sys.unit.model.SysUnit;

import java.util.List;

/**
 * <p>
 * 单位表 服务类
 * </p>
 *
 * @author jikeruan
 * @since 2020-05-21
 */
public interface ISysUnitService extends BaseService<SysUnit> {

    /**
     * @title: treeList
     * @author: SongYh
     * @date: 2022/4/11 11:15
     * @description: 机构列表树形结构查询
     * @param: * @param null
     * @return:
     */
    List<SysUnit> getUnitTreeTableList(SysUnit sysUnit);

    /**
     * @title: findUnitDataById
     * @author: SongYh
     * @date: 2022/4/11 15:59
     * @description: 根据id查询机构详情（适用于修改和查看页面）
     * @param: * @param null
     * @return:
     */
    SysUnit findUnitDataById(String id);

    /**
     * @title: saveOrUpdateUnit
     * @author: SongYh
     * @date: 2022/4/11 17:43
     * @description: 新增、编辑机构
     * @param: * @param null
     * @return:
     */
    boolean saveOrUpdateUnit(SysUnit sysUnit);

    /**
     * @title: deleteUnitDataById
     * @author: SongYh
     * @date: 2022/4/11 18:03
     * @description: 根据id删除机构数据，有子节点也都删除
     * @param: * @param null
     * @return:
     */
    boolean deleteUnitDataById(String id);

    /**
     * 根据id查询当前以及下级数据
     *
     * @param id
     * @return java.util.List<com.jkr.modules.sys.unit.model.SysUnit>
     * @author jijinming
     * @date 15:30:16
     * @since
     */
    List<SysUnit> getUnitListById(String id);





}

