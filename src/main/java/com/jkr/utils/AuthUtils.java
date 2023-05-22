package com.jkr.utils;

import com.jkr.common.constant.CommonConstant;
import com.jkr.common.context.BaseContextHandler;
import com.jkr.modules.sys.role.model.SysRole;
import com.jkr.modules.sys.unit.model.SysUnit;
import com.jkr.modules.sys.unit.service.ISysUnitService;
import com.jkr.modules.sys.user.model.SysUser;
import com.jkr.modules.sys.user.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName AuthUtils
 * @Description 数据权限工具类
 * @Author songyuhang
 * @Date 2020/5/22 15:38
 * @Version 1.0
 **/
@Component
public class AuthUtils {

    private static RedisTemplate<String, Object> redisTemplate2;

    @Autowired
    RedisTemplate<String, Object> redisTemplate;
    @Autowired
    ISysUserService iSysUserService;

    @PostConstruct
    public void init() {
        if (null == redisTemplate2) {
            redisTemplate2 = redisTemplate;
        }
    }

    /**
     * method_name: dataScopeFilter
     * create_user: songYh
     * create_date: 2020/5/23
     * create_time: 9:51
     * describe: 拼接数据权限（单位数据）
     * param : [unitAlias]
     * return : java.lang.String
     */
    public static String dataScopeFilter(String unitAlias) {
        //获取用户信息
        String userId = BaseContextHandler.getUserID();
        StringBuilder sqlString = new StringBuilder();
        if (!"".equals(unitAlias) && null != unitAlias) {
            sqlString.append(" AND EXISTS (SELECT 1 FROM sys_user_unit WHERE user_id = '")
                    .append(userId).append("' AND unit_id = ").append(unitAlias).append(".id)");
            return sqlString.toString();
        }
        return "";
    }

}
