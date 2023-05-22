package com.jkr.modules.sys.user.service.impl;

import com.jkr.core.base.service.impl.BaseServiceImpl;
import com.jkr.modules.sys.user.mapper.SysUserRoleMapper;
import com.jkr.modules.sys.user.model.SysUserRole;
import com.jkr.modules.sys.user.service.ISysUserRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 用户角色关联表 服务实现类
 * </p>
 *
 * @author jikeruan
 * @since 2019-09-06
 */
@Service
public class SysUserRoleServiceImpl extends BaseServiceImpl<SysUserRoleMapper, SysUserRole> implements ISysUserRoleService {

    /**
     * @title: deleteUserRoles
     * @author: SongYh
     * @date: 2022/4/10 8:59
     * @description: 删除用户绑定角色（物理删除）
     * @param: * @param null
     * @return:
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteUserRolesByUserId(String userId) {
        baseMapper.deleteUserRolesByUserId(userId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteUserRolesByRoleId(String userId) {
        baseMapper.deleteUserRolesByRoleId(userId);
    }
    
}
