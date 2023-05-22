package com.jkr.modules.sys.user.controller;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jkr.common.annotation.ValidResult;
import com.jkr.common.annotation.WebLog;
import com.jkr.common.constant.CommonConstant;
import com.jkr.common.model.ResponseData;
import com.jkr.common.utils.BPwdEncoderUtil;
import com.jkr.common.utils.JwtUtil;
import com.jkr.modules.sys.menu.model.SysMenu;
import com.jkr.modules.sys.menu.service.ISysMenuService;
import com.jkr.modules.sys.user.model.SysUser;
import com.jkr.modules.sys.user.service.ISysUserService;
import com.jkr.utils.AesEncryptUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author jikeruan
 * @Date: 2019/3/22 10:17
 * @Description:
 */
@RestController
@Api(value = "LoginController", tags = {"登录操作接口"})
@Slf4j
public class LoginController {

    @Autowired
    private ISysUserService iSysUserService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private ISysMenuService sysMenuService;

    /**
     * Token过期时间
     */
    @Value("${jwt.expires}")
    private Long jwtExpires;
    /**
     * 是否允许重复登录
     */
    @Value("${jwt.singleLogin}")
    private Boolean singleLoginFlag;

    /**
     * @title: login
     * @author: 曾令文
     * @date: 2022/3/6 14:46
     * @description: PC端用户登录
     * @param: [sysUser]
     * @return: com.jkr.common.model.ResponseData<java.lang.String>
     */
    @PostMapping("/login")
    @ApiOperation(value = "登录")
    @ValidResult
    @WebLog(description = "PC端用户登录")
    public ResponseData<String> login(@RequestBody SysUser sysUser) {
        //请求头信息加密后需要先解密
        SysUser sysUserInfo = iSysUserService.getOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getLoginName, AesEncryptUtil.desEncrypt(sysUser.getLoginName())));
        if (sysUserInfo == null) {
            return ResponseData.error(500, "用户名或密码错误，请重新输入");
        }
        if (CommonConstant.ONE_NUMBER.equals(sysUserInfo.getStatus())) {
            return ResponseData.error(500, "用户已锁定");
        }
        if (!BPwdEncoderUtil.matches(AesEncryptUtil.desEncrypt(sysUser.getPassword()), sysUserInfo.getPassword().replace("{bcrypt}", ""))) {
            return ResponseData.error(500, "用户名或密码错误，请重新输入");
        }
        // 用户信息存入redis
        redisTemplate.opsForValue().set(CommonConstant.REDIS_USER_KEY + sysUserInfo.getId(), JSON.toJSONString(sysUserInfo));
        String token = JwtUtil.generateToken(JSON.toJSONString(sysUserInfo), jwtExpires);
        // Token存储到Redis中
        redisTemplate.opsForValue().set(token, token, Duration.ofMillis(jwtExpires));
        // userId、unitId存储到Redis中
        redisTemplate.opsForHash().put(CommonConstant.REDIS_USER_TOKEN_KEY + token, "userId", sysUserInfo.getId());
        redisTemplate.opsForHash().put(CommonConstant.REDIS_USER_TOKEN_KEY + token, "unitId", sysUserInfo.getUnitId());
        // 设置过期时间
        redisTemplate.expire(CommonConstant.REDIS_USER_TOKEN_KEY + token, jwtExpires, TimeUnit.MILLISECONDS);
        // 设置当前登录用户token，用户校验是否重复登录
        if (singleLoginFlag) {
            redisTemplate.opsForValue().set(CommonConstant.REDIS_USER_LOGIN_KEY + sysUserInfo.getId(), token, Duration.ofMillis(jwtExpires));
        }
        return ResponseData.success(token);
    }


    /**
     * method_name: getUserInfo
     * create_user: songYh
     * create_date: 2020/7/18
     * create_time: 10:49
     * describe: 获取用户所有相关信息（前端页面刷新时，调用）
     * param : [request]
     * return : com.jkr.common.model.ResponseData<com.jkr.modules.user.model.SysUserInfo>
     */
    @GetMapping("/getUserInfo")
    @ApiOperation(value = "获取用户信息")
    @WebLog(description = "获取用户所有相关信息（前端页面刷新时，调用）")
    public ResponseData<SysUser> getUserInfo(HttpServletRequest request) {
        String authorization = JwtUtil.getAuthToken(request);
        String userJson = JwtUtil.validateToken(authorization);
        SysUser sysUser = JSON.parseObject(userJson, SysUser.class);
        sysUser.setPassword(null);
        // 查询角色权限
        Set<String> roleSet = iSysUserService.getAuthRoleSetByUserId(sysUser.getId());
        // 查询菜单权限
        Set<String> menuSet = iSysUserService.getMenuListByUserId(sysUser.getId());
        // 查询用户菜单
        List<SysMenu> menuTree = sysMenuService.getMenuTreeListByUserId(sysUser.getId());
        // 查询用户菜单下的按钮权限
//        List<MenuBtnDTO> menuBtnList = iSysUserService.getAllMenuBtnByUserId(sysUser.getId());
//        Map<String, MenuBtnDTO> menuBtnMap = menuBtnList.stream().collect(Collectors.toMap(MenuBtnDTO::getParentCode, Function.identity(), (key1, key2) -> key2));
        Map<String,List<SysMenu>> menuBtnMap = iSysUserService.findAllMenuBtnByUserId(sysUser.getId());
        CollUtil.addAll(roleSet, menuSet);
        sysUser.setAllPermissionSet(roleSet);
        Map<String, Object> map = new HashMap<>(16);
        map.put("rolePermission", roleSet);
        map.put("menuPermission", menuSet);
        map.put("menuTree", menuTree);
        map.put("menuBtnMap", menuBtnMap);
        sysUser.setPermissionMap(map);
        return ResponseData.success(sysUser);
    }

    /**
     * @title: logout
     * @author: 曾令文
     * @date: 2022/6/16 17:29
     * @description: 退出登录
     * @param: [request]
     * @return: com.jkr.common.model.ResponseData<java.lang.Boolean>
     */
    @PostMapping("/logout")
    @ApiOperation(value = "退出登录")
    @ValidResult
    @WebLog(description = "PC端用户退出登录")
    public ResponseData<Boolean> logout(HttpServletRequest request) {
        String authorization = JwtUtil.getAuthToken(request);
        redisTemplate.delete(authorization);
        redisTemplate.delete(CommonConstant.REDIS_USER_TOKEN_KEY + authorization);
        return ResponseData.success();
    }

}
