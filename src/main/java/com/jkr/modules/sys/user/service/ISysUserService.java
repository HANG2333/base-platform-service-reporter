package com.jkr.modules.sys.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jkr.core.base.service.BaseService;
import com.jkr.modules.sys.menu.model.SysMenu;
import com.jkr.modules.sys.menu.model.dto.MenuBtnDTO;
import com.jkr.modules.sys.role.model.SysRole;
import com.jkr.modules.sys.user.model.SysUser;
import com.jkr.modules.sys.user.model.dto.UpdPasswordDTO;
import com.jkr.modules.sys.user.model.dto.SysUserUnitDTO;
import com.jkr.modules.sys.user.model.dto.SysUserInfoRoleDTO;
import com.jkr.modules.sys.user.model.dto.UserDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author jikeruan
 * @since 2019-09-06
 */
public interface ISysUserService extends BaseService<SysUser> {

    IPage<UserDTO> pageList(UserDTO userDTO);

    /**
     * 根据用户id 查询已授权角色列表
     *
     * @param userId
     * @return
     */
    List<SysRole> getAuthRoleListByUserId(String userId);


    /**
     * 保存授权角色
     *
     * @param sysUserInfoRoleDTO
     * @return
     */
    boolean saveAuthRole(SysUserInfoRoleDTO sysUserInfoRoleDTO);


    /**
     * 根据用户id 查询已授权角色列表
     *
     * @param userId
     * @return
     */
    Set<String> getAuthRoleSetByUserId(String userId);

    /**
     * 根据用户id 查询已授权的菜单
     *
     * @param userId
     * @return
     */
    Set<String> getMenuListByUserId(String userId);

    /**
     * 验证密码正确
     *
     * @param updPasswordDTO
     * @return
     */
    boolean validateUserPassword(UpdPasswordDTO updPasswordDTO);

    /**
     * 修改密码
     *
     * @param updPasswordDTO
     * @return
     */
    boolean updatePassword(UpdPasswordDTO updPasswordDTO);

    /**
     * 保存授权单位
     *
     * @param sysUserUnitDTO
     * @return
     */
    Boolean saveAuthUnit(SysUserUnitDTO sysUserUnitDTO);

    /**
     * method_name: getAllMenuByUserId
     * create_user: sunhao
     * create_date: 2020/8/10
     * create_time: 10:04
     * describe: 获取所有菜单
     * param : [userId]
     * return : java.util.Set<java.lang.String>
     */
    List<SysMenu> getAllMenuByUserId(String userId);


   /**
    * @title: saveAndRoleAndUnit
    * @author: SongYh
    * @date: 2022/4/8 16:23
    * @description: 用户保存和修改
    * @param: * @param null
    * @return:
    */
    boolean saveAndRoleAndUnit(SysUser sysUser);

    /**
     * 级联删除用户信息及分配的角色关联信息
     * @param userId 用户id
     * @return
     */
    boolean deleteUserCascade(String userId);

    List<MenuBtnDTO> getAllMenuBtnByUserId(String userId);

    /**
     * @title: findAllMenuBtnByUserId
     * @author: SongYh
     * @date: 2022/4/22 14:07
     * @description: 获取用户每一个菜单目录下的全部按钮权限
     * @param: * @param null
     * @return:
     */
    Map<String,List<SysMenu>> findAllMenuBtnByUserId(String userId);


    /**
     * @title: findUserById
     * @author: SongYh
     * @date: 2022/4/7 16:43
     * @description: 根据id获取用户信息
     * @param: * @param null
     * @return:
     */
    SysUser findUserById(String id);
}
