package com.jkr.modules.sys.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jkr.modules.sys.user.model.SysUserUnit;
import org.apache.ibatis.annotations.Param;


public interface SysUserUnitMapper extends BaseMapper<SysUserUnit> {

    int deleteUserUnits(@Param(value="userId") String userId);
}
