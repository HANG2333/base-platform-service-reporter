package com.jkr.modules.sys.user.service.impl;

import com.jkr.core.base.service.impl.BaseServiceImpl;
import com.jkr.modules.sys.user.mapper.SysUserUnitMapper;
import com.jkr.modules.sys.user.model.SysUserUnit;
import com.jkr.modules.sys.user.service.ISysUserUnitService;
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
public class SysUserUnitServiceImpl extends BaseServiceImpl<SysUserUnitMapper, SysUserUnit> implements ISysUserUnitService {

    /**
     * @title: deleteUserUnits
     * @author: SongYh
     * @date: 2022/4/10 8:59
     * @description: 删除用户绑定数据权限（物理删除）
     * @param: * @param null
     * @return:
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteUserUnits(String userId) {
        baseMapper.deleteUserUnits(userId);
    }
}
