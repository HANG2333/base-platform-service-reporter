package com.jkr.modules.sys.role.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jkr.core.base.service.BaseService;
import com.jkr.modules.sys.role.model.SysRole;
import com.jkr.modules.sys.role.model.dto.SysRoleMenuDTO;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author jikeruan
 * @since 2019-09-06
 */
public interface ISysRoleService extends BaseService<SysRole> {

    @Override
    IPage<SysRole> pageList(SysRole entity);

    @Override
    boolean saveOrUpdate(SysRole sysRole);

    boolean checkColumnUnique(SysRole sysRole);

    boolean deleteRoleById(String roleId);

    boolean saveAuthMenu(SysRoleMenuDTO sysRoleDTO);

}
