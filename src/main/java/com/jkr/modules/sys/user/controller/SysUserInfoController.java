package com.jkr.modules.sys.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jkr.common.annotation.WebLog;
import com.jkr.common.model.ResponseData;
import com.jkr.common.utils.BPwdEncoderUtil;
import com.jkr.config.Global;
import com.jkr.core.base.controller.BaseController;
import com.jkr.modules.sys.role.model.SysRole;
import com.jkr.modules.sys.role.service.ISysRoleService;
import com.jkr.modules.sys.user.model.SysUser;
import com.jkr.modules.sys.user.model.dto.SysUserUnitDTO;
import com.jkr.modules.sys.user.model.dto.SysUserInfoRoleDTO;
import com.jkr.modules.sys.user.model.dto.UpdPasswordDTO;
import com.jkr.modules.sys.user.model.dto.UserDTO;
import com.jkr.modules.sys.user.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author jikeruan
 * @since 2019-09-06
 */
@RestController
@RequestMapping("/user")
@Api(value = "SysUserInfoController", tags = {"用户操作接口"})
public class SysUserInfoController extends BaseController<ISysUserService, SysUser> {

    @Autowired
    private ISysUserService iSysUserService;
    @Autowired
    private ISysRoleService iSysRoleService;

    /**
     * @title: pageList
     * @author: 曾令文
     * @date: 2022/3/7 14:41
     * @description: 用户管理列表（分页）
     * @param: [userDTO]
     * @return: com.jkr.common.model.ResponseData<com.baomidou.mybatisplus.core.metadata.IPage < com.jkr.modules.sys.user.model.dto.UserDTO>>
     */
    @WebLog(description = "用户管理列表（分页）")
    @GetMapping(value = "/pageList")
    public ResponseData<IPage<UserDTO>> pageList(UserDTO userDTO) {
        return ResponseData.success(baseService.pageList(userDTO));
    }

    /**
     * @title: 用户保存和修改
     * @author: SongYh
     * @date: 2022/4/8 16:00
     * @description: 
     * @param: * @param null
     * @return: 
     */
    @WebLog(description = "用户保存和修改")
    @PostMapping("/saveOrUpdateUser")
    public ResponseData<Boolean> saveOrUpdateUser(@RequestBody SysUser sysUser) {
        // 赋值加密密码
        sysUser.setPassword(BPwdEncoderUtil.BCryptPassword(sysUser.getPassword()));
        return ResponseData.success(baseService.saveAndRoleAndUnit(sysUser));
    }

    /**
     * @title: deleteUserCascade
     * @author: SongYh
     * @date: 2022/4/10 8:56
     * @description: 行内删除用户
     * @param: * @param null
     * @return:
     */
    @ApiOperation(value = "根据用户id删除用户信息及分配的角色信息", notes = "根据用户id删除用户信息及分配的角色信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id", dataType = "String", required = true)
    })
    @PostMapping("/deleteUserCascade")
    @WebLog(description = "行内删除用户")
    public ResponseData<Boolean> deleteUserCascade(@RequestParam String id) {
        return ResponseData.success(iSysUserService.deleteUserCascade(id));
    }

    /**
     * @title: getUserInfoByUsername
     * @author: SongYh
     * @date: 2022/4/7 15:48
     * @description: 验证登录名重复
     * @param: * @param username
     * @return: com.jkr.common.model.ResponseData<com.jkr.modules.sys.user.model.SysUser>
     */
    @ApiOperation(value = "根据用户名获取用户信息", notes = "根据用户名获取用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", dataType = "String", paramType = "query", required = true)
    })
    @GetMapping(value = "/getUserInfoByUsername")
    @WebLog(description = "验证登录名重复")
    public ResponseData<SysUser> getUserInfoByUsername(@RequestParam("userName") String userName,@RequestParam("userId") String userId) {
        return ResponseData.success(baseService.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getLoginName, userName).ne(SysUser::getId,userId)));
    }

    /**
     * @title: resetPassword
     * @author: SongYh
     * @date: 2022/4/11 17:30
     * @description: 重置密码
     * @param: * @param null
     * @return:
     */
    @ApiOperation(value = "重置密码", notes = "重置密码")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "SysUserInfo", name = "sysUserInfo", value = "对象参数", required = true)
    })
    @PostMapping(value = "/resetPassword")
    @WebLog(description = "重置密码")
    public ResponseData<Boolean> resetPassword(@RequestBody SysUser sysUser) {
        sysUser.setPassword("{bcrypt}" + BPwdEncoderUtil.BCryptPassword(sysUser.getPassword()));
        return ResponseData.success(baseService.updateById(sysUser));
    }

    /**
     * @title: getAuthRoleListByUserId
     * @author: 曾令文
     * @date: 2022/3/9 15:43
     * @description: TODO 根据用户id查询已授权的角色列表
     * @param: [userId]
     * @return: com.jkr.common.model.ResponseData<java.util.List<com.jkr.modules.sys.role.model.SysRole>>
     */
    @ApiOperation(value = "获取已授权的角色列表", notes = "根据用户id查询已授权的角色列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户Id", dataType = "String", paramType = "query", required = true)
    })
    @GetMapping(value = "/getAuthRoleListByUserId")
    @WebLog(description = "根据用户id查询已授权的角色列表")
    public ResponseData<List<SysRole>> getAuthRoleListByUserId(String userId) {
        return ResponseData.success(baseService.getAuthRoleListByUserId(userId));
    }

    /**
     * @title: 用户保存角色
     * @author: SongYh
     * @date: 2022/4/8 15:58
     * @description: 
     * @param: * @param null
     * @return: 
     */
    @ApiOperation(value = "保存授权角色", notes = "保存授权角色")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "SysUserInfoRoleDTO", name = "sysUserInfoRoleDTO", value = "对象参数", required = true)
    })
    @PostMapping(value = "/saveAuthRole")
    @WebLog(description = "用户保存角色")
    public ResponseData<Boolean> saveAuthRole(@RequestBody SysUserInfoRoleDTO sysUserInfoRoleDTO) {
        return ResponseData.success(baseService.saveAuthRole(sysUserInfoRoleDTO));
    }

    /**
     * @title: validateUserPassword
     * @author: SongYh
     * @date: 2022/4/8 16:22
     * @description: 验证密码
     * @param: * @param null
     * @return:
     */
    @ApiOperation(value = "验证密码", notes = "验证密码")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "UpdPasswordDTO", name = "updPasswordDTO", value = "对象参数", required = true)
    })
    @PostMapping(value = "/validateUserPassword")
    @WebLog(description = "验证密码")
    public ResponseData<Boolean> validateUserPassword(@RequestBody UpdPasswordDTO updPasswordDTO) {
        return ResponseData.success(baseService.validateUserPassword(updPasswordDTO));
    }

    /**
     * @title: updatePassword
     * @author: SongYh
     * @date: 2022/4/11 17:30
     * @description: 修改密码
     * @param: * @param null
     * @return:
     */
    @ApiOperation(value = "修改密码", notes = "修改密码")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "UpdPasswordDTO", name = "updPasswordDTO", value = "对象参数", required = true)
    })
    @PostMapping(value = "/updatePassword")
    @WebLog(description = "修改密码")
    public ResponseData<Boolean> resetPassword(@RequestBody UpdPasswordDTO updPasswordDTO) {
        return ResponseData.success(baseService.updatePassword(updPasswordDTO));
    }

    /**
     * method_name: saveAuthUnit
     * create_user: songYh
     * create_date: 2020/5/21
     * create_time: 19:45
     * describe: 保存单位授权
     * param : [sysUserUnitDTO]
     * return : com.jkr.common.model.ResponseData<java.lang.Boolean>
     */
    @ApiOperation(value = "保存单位授权", notes = "保存单位授权")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "SysUserUnitDTO", name = "sysUserUnitDTO", value = "对象参数", required = true)
    })
    @PostMapping(value = "/saveAuthUnit")
    @WebLog(description = "保存单位授权")
    public ResponseData<Boolean> saveAuthUnit(@RequestBody SysUserUnitDTO sysUserUnitDTO) {
        return ResponseData.success(baseService.saveAuthUnit(sysUserUnitDTO));
    }

    /**
     * method_name: getByPhoneNumber
     * create_user: jikeruan
     * create_date: 2020/8/26
     * create_time: 14:14
     * describe: 根据手机号获取用户信息
     * param : [phoneNumber]
     * return : com.jkr.common.model.ResponseData<com.jkr.modules.user.model.SysUserInfo>
     */
    @WebLog(description = "根据手机号获取用户信息")
    @GetMapping(value = "/getByPhoneNumber")
    public ResponseData<SysUser> getByPhoneNumber(String phoneNumber) {
        return ResponseData.success(baseService.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getPhone, phoneNumber)));
    }

    /**
     * @title: findUserById
     * @author: SongYh
     * @date: 2022/4/7 16:43
     * @description: 根据id获取用户信息
     * @param: * @param null
     * @return:
     */
    @WebLog(description = "根据id获取用户信息")
    @RequestMapping("findUserById")
    public ResponseData<SysUser> findUserById(String id) {
        return ResponseData.success(baseService.findUserById(id));
    }
}
