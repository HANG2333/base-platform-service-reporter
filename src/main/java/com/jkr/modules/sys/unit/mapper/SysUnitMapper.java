package com.jkr.modules.sys.unit.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.jkr.modules.sys.unit.model.SysUnit;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 单位表 Mapper 接口
 * </p>
 *
 * @author jikeruan
 * @since 2020-05-21
 */
public interface SysUnitMapper extends BaseMapper<SysUnit> {

    /**
     * @title: findUnitDataById
     * @author: SongYh
     * @date: 2022/4/11 16:05
     * @description: 根据id查询机构详情（适用于修改和查看页面）
     * @param: * @param null
     * @return:
     */
    SysUnit findUnitDataById(@Param(Constants.WRAPPER) Wrapper<SysUnit> queryWrapper);

    /**
     * @title: deleteUnitDataById
     * @author: SongYh
     * @date: 2022/4/11 18:03
     * @description: 根据id删除机构数据，有子节点也都删除
     * @param: * @param null
     * @return:
     */
    boolean deleteUnitDataById(SysUnit sysUnit);




}
