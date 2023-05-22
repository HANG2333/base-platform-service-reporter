package com.jkr.modules.sys.user.service;

import com.jkr.core.base.service.BaseService;
import com.jkr.modules.sys.user.model.SysUserRole;
import com.jkr.modules.sys.user.model.SysUserUnit;

/**
 * <p>
 * 用户角色关联表 服务类
 * </p>
 *
 * @author jikeruan
 * @since 2019-09-06
 */
public interface ISysUserUnitService extends BaseService<SysUserUnit> {

    /**
     * @title: deleteUserUnits
     * @author: SongYh
     * @date: 2022/4/10 8:59
     * @description: 删除用户绑定单位权限（物理删除）
     * @param: * @param null
     * @return:
     */
    void deleteUserUnits(String userId);

}
