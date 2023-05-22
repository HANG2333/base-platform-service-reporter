package com.jkr.modules.sys.role.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jkr.modules.sys.role.model.SysRole;
import com.jkr.modules.sys.role.model.SysRoleMenu;
import com.jkr.modules.sys.role.model.dto.SysRoleMenuDTO;
import org.apache.ibatis.annotations.Param;

public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {

    boolean saveBatch(@Param("sysRoleMenuDTO")SysRoleMenuDTO sysRoleMenuDTO);
}
