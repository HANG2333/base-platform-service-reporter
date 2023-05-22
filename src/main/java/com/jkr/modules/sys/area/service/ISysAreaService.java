package com.jkr.modules.sys.area.service;

import com.jkr.core.base.service.BaseService;
import com.jkr.modules.sys.area.model.SysArea;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 行政区划管理-服务类
 *
 * @author zenglingwen
 * @since 2022-04-11
 */
public interface ISysAreaService extends BaseService<SysArea> {

    /**
     * @title: getAreaTreeTableList
     * @author: zenglingwen
     * @date: 2022年04月11日 16:20:23
     * @description: 行政区划列表树形查询
     * @param: sysArea
     * @return: java.util.List<com.jkr.modules.sys.area.model.SysArea>
     */
    List<SysArea> getAreaTreeTableList(SysArea sysArea);

    /**
     * @title: searchList
     * @author: zenglingwen
     * @date: 2022年04月11日 16:20:32
     * @description: 查询行政区划列表，用于上级行政区划树形结构
     * @param: sysArea
     * @return: java.util.List<com.jkr.modules.sys.area.model.SysArea>
     */
    List<SysArea> searchList(SysArea sysArea);

    /**
     * @title: saveOrUpdate
     * @author: zenglingwen
     * @date: 2022年04月11日 16:20:41
     * @description: 保存或更新行政区划信息
     * @param: sysArea
     * @return: boolean
     */
    @Override
    boolean saveOrUpdate(SysArea sysArea);

    /**
     * @title: deleteSelfAndChildren
     * @author: zenglingwen
     * @date: 2022年04月11日 16:20:53
     * @description: 删除自己及下级
     * @param: id
     * @return: boolean
     */
    boolean deleteSelfAndChildren(String id);

    /**
     * @title: checkNameUnique
     * @author: zenglingwen
     * @date: 2022年04月11日 16:21:05
     * @description: 验证行政区划名称唯一
     * @param: id
     * @param: name
     * @return: boolean
     */
    boolean checkNameUnique(String id, String name);

    /**
     * @title: checkCodeUnique
     * @author: zenglingwen
     * @date: 2022年04月11日 16:21:18
     * @description: 验证行政区划编码唯一
     * @param: id
     * @param: code
     * @return: boolean
     */
    boolean checkCodeUnique(String id, String name);


    /**
     * @title: getAreaByType
     * @author: py
     * @date: 2022/4/29 18:57
     * @description: 根据type parentId 获取行政区划
     * @param: [type, parentId]
     * @return: java.util.List<com.jkr.modules.sys.area.model.SysArea>
     */
    List<SysArea> getAreaByType(@Param("type") String type, @Param("parentId") String parentId);


    /**
     * @title: areaList
     * @author: py
     * @date: 2022/5/6 10:04
     * @description: 查询行政区划
     * @param: []
     * @return: java.util.List<com.jkr.modules.sys.area.model.SysArea>
     */
    List<SysArea> areaList();

    /**
     * id
     *
     * @param id
     * @return java.util.List<com.jkr.modules.sys.area.model.SysArea>
     * @author jijinming
     * @date 16:15:02
     * @since
     */
    List<SysArea> searchListByParentId(String id);

}
