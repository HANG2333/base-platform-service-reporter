package com.jkr.modules.sys.area.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jkr.modules.sys.area.model.SysArea;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 行政区划管理-Mapper 接口
 *
 * @author zenglingwen
 * @since 2022-04-11
 */
public interface SysAreaMapper extends BaseMapper<SysArea> {

    /**
     * @title: deleteSelfAndChildren
     * @author: zenglingwen
     * @date: 2022年04月11日 16:20:53
     * @description: 删除自己及下级
     * @param: sysArea
     * @return: boolean
     */
    boolean deleteSelfAndChildren(SysArea sysArea);

    /**
     * @title: getAreaByType
     * @author: wanghe
     * @date: 2022/4/30 9:51
     * @description: 通过type查询区域
     * @param type
     * @param parentId
     * @return: java.util.List<com.jkr.modules.sys.area.model.SysArea>
     */
    List<SysArea> getAreaByType(@Param("type") String type, @Param("parentId") String parentId);
}
