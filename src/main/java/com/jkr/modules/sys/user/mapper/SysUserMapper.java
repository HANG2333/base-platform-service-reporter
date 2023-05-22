package com.jkr.modules.sys.user.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.jkr.modules.sys.menu.model.SysMenu;
import com.jkr.modules.sys.menu.model.dto.MenuBtnDTO;
import com.jkr.modules.sys.role.model.SysRole;
import com.jkr.modules.sys.user.model.SysUser;
import com.jkr.modules.sys.user.model.dto.UserDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author jikeruan
 * @since 2019-09-06
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

    IPage<UserDTO> pageList(IPage<UserDTO> page, @Param("userDto") UserDTO userDTO);

    /**
     * 根据用户id 查询已授权角色列表
     *
     * @param userId
     * @return
     */
    List<SysRole> getAuthRoleListByUserId(@Param("userId") String userId);

    /**
     * 根据用户id 查询已授权角色列表
     *
     * @param userId
     * @return
     */
    Set<String> getAuthRoleSetByUserId(@Param("userId") String userId);

    /**
     * 根据用户id 查询已授权的菜单权限
     *
     * @param userId
     * @return
     */
    Set<String> getMenuListByUserId(@Param("userId") String userId);

    /**
     * method_name: getUserRoleList
     * create_user: songYh
     * create_date: 2020/6/3
     * create_time: 14:49
     * describe:  获取用户所有角色详细信息
     * param : [userId]
     * return : java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     */
    List<Map<String, Object>> getUserRoleList(@Param("userId") String userId);

    /**
     * method_name: pageListWithUnit
     * create_user: songYh
     * create_date: 2020/6/4
     * create_time: 8:37
     * describe: 用户分页
     * param : [page, queryWrapper]
     * return : com.baomidou.mybatisplus.core.metadata.IPage<java.util.Map<java.lang.String,java.lang.Object>>
     */
    IPage<Map<String, Object>> pageListWithUnit(IPage<SysUser> page, @Param(Constants.WRAPPER) Wrapper<SysUser> queryWrapper);

    /**
    * method_name:getUserInfoByUserId
    * create_user: DaiFuyou
    * create_date:2020/9/3
    * create_time:10:51
    * describe:根据用户ID获取用户信息
    * param:[userId]
    * return:com.jkr.modules.user.model.SysUserInfo
    */
    SysUser getUserInfoByUserId(String userId);

    /**
    * method_name:getUserIdByLoginName
    * create_user: DaiFuyou
    * create_date:2020/9/3
    * create_time:10:52
    * describe: 根据登录名获取userId
    * param:[loginName]
    * return:java.lang.String
    */
    String getUserIdByLoginName(@Param("loginName") String loginName);

    /**
     * method_name: getAllMenuByUserId
     * create_user: sunhao
     * create_date: 2020/8/10
     * create_time: 10:04
     * describe: 获取所有菜单
     * param : [userId]
     * return : java.util.Set<java.lang.String>
     */
    List<SysMenu> getAllMenuByUserId(@Param("userId") String userId);



    /**
     * method_name: getAllMenuByUserId
     * create_user: yzl
     * create_date: 2021/3/2
     * create_time: 10:04
     * describe: 获取菜单下按钮权限
     * param : [userId]
     * return : java.util.Set<java.lang.String>
     */
    List<MenuBtnDTO> getAllMenuBtnByUserId(@Param("userId") String userId);

    /**
     * @title: findAllMenuBtnByUserId
     * @author: SongYh
     * @date: 2022/4/22 14:07
     * @description: 获取用户每一个菜单目录下的全部按钮权限
     * @param: * @param null
     * @return:
     */
    List<SysMenu> findAllMenuBtnByUserId(@Param("userId") String userId);

}
