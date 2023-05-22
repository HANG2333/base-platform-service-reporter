package com.jkr.modules.sys.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jkr.modules.sys.user.model.SysUserRole;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 用户角色关联表 Mapper 接口
 * </p>
 *
 * @author jikeruan
 * @since 2019-09-06
 */
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    int deleteUserRolesByUserId(@Param(value="userId") String userId);

    int deleteUserRolesByRoleId(@Param(value="roleId") String roleId);
}
