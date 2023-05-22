package com.jkr.modules.sys.role.service;

import com.jkr.core.base.service.BaseService;
import com.jkr.modules.sys.role.model.SysRoleMenu;
import com.jkr.modules.sys.role.model.dto.SysRoleMenuDTO;

import java.util.List;

public interface ISysRoleMenuService extends BaseService<SysRoleMenu> {

    List<String> getAuthMenuIdsById(String roleId);

    boolean deleteByRoleId(String roleId);

    boolean saveBatch(SysRoleMenuDTO sysRoleMenuDTO);
}
