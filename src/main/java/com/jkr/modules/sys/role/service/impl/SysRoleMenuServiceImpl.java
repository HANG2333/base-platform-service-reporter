package com.jkr.modules.sys.role.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jkr.core.base.service.impl.BaseServiceImpl;
import com.jkr.modules.sys.role.mapper.SysRoleMenuMapper;
import com.jkr.modules.sys.role.model.SysRoleMenu;
import com.jkr.modules.sys.role.model.dto.SysRoleMenuDTO;
import com.jkr.modules.sys.role.service.ISysRoleMenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysRoleMenuServiceImpl extends BaseServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements ISysRoleMenuService {

    @Override
    public List<String> getAuthMenuIdsById(String roleId) {
        List<SysRoleMenu> sysRoleMenus = super.list(new QueryWrapper<SysRoleMenu>().eq("role_id", roleId));
        return sysRoleMenus.stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteByRoleId(String roleId) {
        return super.remove(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, roleId));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveBatch(SysRoleMenuDTO sysRoleMenuDTO) {
        return baseMapper.saveBatch(sysRoleMenuDTO);
    }
}
