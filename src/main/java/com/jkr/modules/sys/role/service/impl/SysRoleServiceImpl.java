package com.jkr.modules.sys.role.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jkr.core.base.service.impl.BaseServiceImpl;
import com.jkr.modules.sys.role.mapper.SysRoleMapper;
import com.jkr.modules.sys.role.model.SysRole;
import com.jkr.modules.sys.role.model.dto.SysRoleMenuDTO;
import com.jkr.modules.sys.role.service.ISysRoleMenuService;
import com.jkr.modules.sys.role.service.ISysRoleService;
import com.jkr.modules.sys.user.model.SysUserRole;
import com.jkr.modules.sys.user.service.ISysUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author jikeruan
 * @since 2019-09-06
 */
@Service
public class SysRoleServiceImpl extends BaseServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Autowired
    private ISysUserRoleService iSysUserRoleService;
    @Autowired
    private ISysRoleMenuService iSysRoleMenuService;

    @Override
    public IPage<SysRole> pageList(SysRole sysRole) {
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_date");
        if (StrUtil.isNotBlank(sysRole.getName())) {
            queryWrapper.like("name", sysRole.getName());
        }
        if (StrUtil.isNotBlank(sysRole.getCode())) {
            queryWrapper.like("code", sysRole.getCode());
        }
        return baseMapper.selectPage(getPagePlus(sysRole), queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveOrUpdate(SysRole sysRole) {
        return super.saveOrUpdate(sysRole);
    }

    @Override
    public boolean checkColumnUnique(SysRole sysRole) {
        LambdaQueryWrapper<SysRole> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(SysRole::getId)
                .last("limit 1");
        if (StrUtil.isNotBlank(sysRole.getName())) {
            lambdaQueryWrapper.eq(SysRole::getName, sysRole.getName());
        } else if (StrUtil.isNotBlank(sysRole.getCode())) {
            lambdaQueryWrapper.eq(SysRole::getCode, sysRole.getCode());
        }
        if (StrUtil.isNotBlank(sysRole.getId())) {
            lambdaQueryWrapper.ne(SysRole::getId, sysRole.getId());
        }
        return super.getOne(lambdaQueryWrapper) == null;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteRoleById(String roleId) {
        // 判断当角色是否有人使用
        List<SysUserRole> userRoleList = iSysUserRoleService.list(new LambdaQueryWrapper<SysUserRole>()
                .eq(SysUserRole::getRoleId, roleId));
        if (CollectionUtil.isNotEmpty(userRoleList)) {
            return false;
        } else {
            // 删除用户关联角色
            iSysUserRoleService.deleteUserRolesByRoleId(roleId);
            // 删除角色
            baseMapper.delete(new LambdaQueryWrapper<SysRole>()
                    .eq(SysRole::getId, roleId));
            return true;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveAuthMenu(SysRoleMenuDTO sysRoleMenuDTO) {
        // 刪除原有角色菜单对应关系
        iSysRoleMenuService.deleteByRoleId(sysRoleMenuDTO.getRoleId());
        if (sysRoleMenuDTO.getMenuIds().length > 0) {
            iSysRoleMenuService.saveBatch(sysRoleMenuDTO);
        }
        return true;
    }
}
