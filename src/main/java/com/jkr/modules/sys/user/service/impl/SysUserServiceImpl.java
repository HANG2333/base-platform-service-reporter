package com.jkr.modules.sys.user.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jkr.common.constant.CommonConstant;
import com.jkr.common.utils.BPwdEncoderUtil;
import com.jkr.core.base.model.PageModel;
import com.jkr.core.base.service.impl.BaseServiceImpl;
import com.jkr.modules.sys.menu.model.SysMenu;
import com.jkr.modules.sys.menu.model.dto.MenuBtnDTO;
import com.jkr.modules.sys.role.model.SysRole;
import com.jkr.modules.sys.user.mapper.SysUserMapper;
import com.jkr.modules.sys.user.model.SysUser;
import com.jkr.modules.sys.user.model.SysUserRole;
import com.jkr.modules.sys.user.model.SysUserUnit;
import com.jkr.modules.sys.user.model.dto.SysUserInfoRoleDTO;
import com.jkr.modules.sys.user.model.dto.SysUserUnitDTO;
import com.jkr.modules.sys.user.model.dto.UpdPasswordDTO;
import com.jkr.modules.sys.user.model.dto.UserDTO;
import com.jkr.modules.sys.user.service.ISysUserRoleService;
import com.jkr.modules.sys.user.service.ISysUserService;
import com.jkr.modules.sys.user.service.ISysUserUnitService;
import com.jkr.utils.AuthUtils;
import com.jkr.utils.EscapeUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author jikeruan
 * @since 2019-09-06
 */
@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
public class SysUserServiceImpl extends BaseServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Autowired
    private ISysUserRoleService iSysUserRoleService;
    @Autowired
    private ISysUserUnitService iSysUserUnitService;

    /**
     * @title: pageList
     * @author: 曾令文
     * @date: 2022/3/7 14:51
     * @description: TODO 这里是描述信息
     * @param: [userDTO]
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jkr.modules.sys.user.model.dto.UserDTO>
     */
    @Override
    public IPage<UserDTO> pageList(UserDTO userDTO) {
        Page<UserDTO> page = new Page<>();
        page.setCurrent(userDTO.getCurrent());
        page.setSize(userDTO.getSize());
        return baseMapper.pageList(page, userDTO);
    }

    @Override
    public List<SysRole> getAuthRoleListByUserId(String userId) {
        return baseMapper.getAuthRoleListByUserId(userId);
    }

    /**
     * 删除缓存
     *
     * @param sysUserInfoRoleDTO
     * @return
     */
    @Override
    public boolean saveAuthRole(SysUserInfoRoleDTO sysUserInfoRoleDTO) {
        boolean success = false;
        if (StrUtil.isNotBlank(sysUserInfoRoleDTO.getUserId()) && sysUserInfoRoleDTO.getRoleIds() != null) {
            // 删除关联表
            iSysUserRoleService.remove(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, sysUserInfoRoleDTO.getUserId()));
            // 添加关联表
            sysUserInfoRoleDTO.getRoleIds().forEach(roleId -> {
                iSysUserRoleService.save(new SysUserRole().setUserId(sysUserInfoRoleDTO.getUserId()).setRoleId(roleId));
            });
            success = true;
        }
        return success;
    }

    @Override
    public Set<String> getAuthRoleSetByUserId(String userId) {
        return baseMapper.getAuthRoleSetByUserId(userId);
    }

    @Override
    public Set<String> getMenuListByUserId(String userId) {
        return baseMapper.getMenuListByUserId(userId);
    }

    @Override
    public boolean validateUserPassword(UpdPasswordDTO updPasswordDTO) {
        SysUser sysUser = baseMapper.selectById(updPasswordDTO.getUserId());
        return BPwdEncoderUtil.matches(updPasswordDTO.getOldPass(), sysUser.getPassword().replace("{bcrypt}", ""));
    }

    @Override
    public boolean updatePassword(UpdPasswordDTO updPasswordDTO) {
        boolean bool = false;
        SysUser sysUser = baseMapper.selectById(updPasswordDTO.getUserId());
        if (sysUser != null) {
            if (!BPwdEncoderUtil.matches(updPasswordDTO.getOldPass(), sysUser.getPassword().replace("{bcrypt}", ""))) {
                return false;
            }
            if (updPasswordDTO.getNewPass().equalsIgnoreCase(updPasswordDTO.getConPass())) {
                sysUser.setPassword("{bcrypt}" + BPwdEncoderUtil.BCryptPassword(updPasswordDTO.getNewPass()));
                bool = super.updateById(sysUser);
            }
        }
        return bool;
    }

    /**
     * 保存授权单位
     *
     * @param sysUserUnitDTO
     * @return
     */
    @Override
    public Boolean saveAuthUnit(SysUserUnitDTO sysUserUnitDTO) {
        boolean success = false;
        if (StrUtil.isNotBlank(sysUserUnitDTO.getUserId()) && sysUserUnitDTO.getUnitIds() != null) {
            // 删除关联表
            iSysUserUnitService.remove(new LambdaQueryWrapper<SysUserUnit>().eq(SysUserUnit::getUserId, sysUserUnitDTO.getUserId()));
            // 添加关联表
            sysUserUnitDTO.getUnitIds().forEach(unitId -> {
                iSysUserUnitService.save(new SysUserUnit().setUserId(sysUserUnitDTO.getUserId()).setUnitId(unitId));
            });
            success = true;
        }
        return success;
    }


    /**
     * @title: saveAndRoleAndUnit
     * @author: SongYh
     * @date: 2022/4/8 16:23
     * @description: 用户保存和修改
     * @param: * @param null
     * @return:
     */
    @Override
    public boolean saveAndRoleAndUnit(SysUser sysUser) {
        saveOrUpdate(sysUser);
        // 角色修改
        SysUserInfoRoleDTO sysUserInfoRoleDTO = new SysUserInfoRoleDTO();
        sysUserInfoRoleDTO.setUserId(sysUser.getId());
        sysUserInfoRoleDTO.setRoleIds(sysUser.getRoleIds());
        saveAuthRole(sysUserInfoRoleDTO);
        // 单位权限修改
        SysUserUnitDTO sysUserUnitDTO = new SysUserUnitDTO();
        sysUserUnitDTO.setUserId(sysUser.getId());
        sysUserUnitDTO.setUnitIds(sysUser.getUnitIds());
        saveAuthUnit(sysUserUnitDTO);
        return true;
    }

    /**
     * 级联删除用户信息及分配的角色关联信息
     *
     * @param userId 用户id
     * @return
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public boolean deleteUserCascade(String userId) {
        //删除用户
        this.removeById(userId);
        //删除用户角色
        iSysUserRoleService.deleteUserRolesByUserId(userId);
        // 删除用户数据权限
        iSysUserUnitService.deleteUserUnits(userId);
        return true;
    }

    /**
     * method_name: getAllMenuByUserId
     * create_user: yzl
     * create_date: 2021/3/2
     * create_time: 10:04
     * describe: 获取菜单下按钮权限
     * param : [userId]
     * return : java.util.Set<java.lang.String>
     */
    @Override
    public List<MenuBtnDTO> getAllMenuBtnByUserId(String userId) {
        return baseMapper.getAllMenuBtnByUserId(userId);
    }


    /**
     * @title: findAllMenuBtnByUserId
     * @author: SongYh
     * @date: 2022/4/22 14:07
     * @description: 获取用户每一个菜单目录下的全部按钮权限
     * @param: * @param null
     * @return:
     */
    @Override
    public Map<String,List<SysMenu>> findAllMenuBtnByUserId(String userId){
        List<SysMenu> btnList = baseMapper.findAllMenuBtnByUserId(userId);
        Map<String, List<SysMenu>> map = new HashMap<>();
        // 数据类型转化
        if (btnList.size() > 0){
            map = btnList.stream().collect(Collectors.groupingBy(SysMenu::getParentCode));
        }
        return map;
    }





    @Override
    public List<SysMenu> getAllMenuByUserId(String userId) {
        return baseMapper.getAllMenuByUserId(userId);
    }





    /**
     * @title: findUserById
     * @author: SongYh
     * @date: 2022/4/7 16:43
     * @description: 根据id获取用户信息
     * @param: * @param null
     * @return:
     */
    @Override
    public SysUser findUserById(String id){
        SysUser user = getById(id);
        // 查询用户数据权限
        List<SysUserUnit> unitList = iSysUserUnitService.list(new LambdaQueryWrapper<SysUserUnit>().eq(SysUserUnit::getUserId, id));
        if (unitList.size() > CommonConstant.ZERO_NUMBER){
            user.setUnitIds(unitList.stream().map(re -> re.getUnitId()).collect(Collectors.toList())) ;
        }
        // 查询用户角色集合
        List<SysUserRole> roleList = iSysUserRoleService.list(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, id));
        if (roleList.size() > CommonConstant.ZERO_NUMBER){
            user.setRoleIds(roleList.stream().map(re -> re.getRoleId()).collect(Collectors.toList())) ;
        }
        return user;
    }

}
