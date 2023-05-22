package com.jkr.filter;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.jkr.common.constant.CommonConstant;
import com.jkr.common.context.BaseContextHandler;
import com.jkr.common.model.ResponseData;
import com.jkr.common.utils.JwtUtil;
import com.jkr.modules.sys.user.model.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * @Author：jikeruan
 * @Description:
 * @Date: 2019/11/8 8:51
 */
public class UserAuthRestInterceptor extends HandlerInterceptorAdapter {

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    private static final String ADMIN_USER_TYPE = "0";

    private static final String GET = "GET";

    private static final String POST = "POST";

    private static final String HEAD = "HEAD";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

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

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {


        String method = request.getMethod();
        if (!GET.equals(method) && !POST.equals(method) && !HEAD.equals(method)) {
            response.setContentType("text/html;charset=GBK");
            response.setCharacterEncoding("GBK");
            response.setStatus(403);
            response.getWriter().print("<font size=6 color=red>对不起，您的请求非法，系统拒绝响应!</font>");
            return false;
        }


        String requestURI = request.getRequestURI();
        if (requestURI.equals("/app/login")) {
            return true;
        }

        String header = request.getHeader("Device");
        //判断是否为APP请求
        if ("Android".equals(header)) {
        } else {
            //PC端请求
//            HttpServletRequest req = (HttpServletRequest) request;
//            HttpServletResponse resp = (HttpServletResponse) response;
//            String referer = req.getHeader("referer");
//            if(referer.contains(refererUrl)){
//            }else{
//                return false;
//            }
        }

        // 取header 的内容
        String token = JwtUtil.getAuthToken(request);
        if (StrUtil.isNotBlank(token)) {
            // 1. 验证Redis中是否有token，验证是否过期
            String redisToken = (String) redisTemplate.opsForValue().get(token);
            if (StrUtil.isBlank(redisToken)) {
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json; charset=utf-8");
                PrintWriter out = response.getWriter();
                out.append(JSON.toJSONString(ResponseData.error(401, "token已过期")));
                return false;
            } else {
                // 相同账号，只允许同时登录一个
                if(singleLoginFlag) {
                    // 从redis中取userId和unitId，放入BaseContextHandler中
                    String userId = (String) redisTemplate.opsForHash().get(CommonConstant.REDIS_USER_TOKEN_KEY + token, "userId");
                    String loginToken = (String) redisTemplate.opsForValue().get(CommonConstant.REDIS_USER_LOGIN_KEY + userId);
                    if(!redisToken.equals(loginToken)) {
                        response.setCharacterEncoding("UTF-8");
                        response.setContentType("application/json; charset=utf-8");
                        PrintWriter out = response.getWriter();
                        out.append(JSON.toJSONString(ResponseData.error(3000, "请重新登录")));
                        return false;
                    }
                }
                // 2. Redis中存在token，则表示token有效
                String newToken = redisToken;
                Long expire = redisTemplate.opsForValue().getOperations().getExpire(token, TimeUnit.MILLISECONDS);
                // token有效期小于剩余有效期的二分之一时，重新获取token
                if (expire < jwtExpires / 2) {
                    newToken = this.refreshToken(token);
                }
                // 验证token
                String userJson = JwtUtil.validateToken(newToken);
                // 转换对象
                SysUser sysUser = JSON.parseObject(userJson, SysUser.class);
                BaseContextHandler.setUserID(sysUser.getId());
                BaseContextHandler.setUnitID(sysUser.getUnitId());
            }
        } else {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            PrintWriter out = response.getWriter();
            out.append(JSON.toJSONString(ResponseData.error(401, "没有token")));
            return false;
        }
        return super.preHandle(request, response, handler);
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 删除变量
        BaseContextHandler.remove();
        super.afterCompletion(request, response, handler, ex);
    }

    /**
     * 获取请求中的url
     *
     * @param url
     * @return
     */
    private String getUrl(String url) {
        //获取当前访问url
        int firstQuestionMarkIndex = url.indexOf("?");
        if (firstQuestionMarkIndex != -1) {
            return url.substring(0, firstQuestionMarkIndex);
        }
        return url;
    }

    /**
     * @title: refreshToken
     * @author: 曾令文
     * @date: 2022/6/14 15:10
     * @description: token自动续期
     * @param: [token, userId]
     * @return: boolean
     */
    private String refreshToken(String token) {
        // 从redis中取userId和unitId，放入BaseContextHandler中
        String userId = (String) redisTemplate.opsForHash().get(CommonConstant.REDIS_USER_TOKEN_KEY + token, "userId");
        // 从redis中取用户信息
        String sysUserJson = (String) redisTemplate.opsForValue().get(CommonConstant.REDIS_USER_KEY + userId);
        // 根据用户信息重新生成token
        String newToken = JwtUtil.generateToken(sysUserJson, jwtExpires);
        // 重新设置过期时间
        redisTemplate.opsForValue().set(token, newToken, Duration.ofMillis(jwtExpires));
        // 设置过期时间
        redisTemplate.expire(CommonConstant.REDIS_USER_TOKEN_KEY + token, jwtExpires, TimeUnit.MILLISECONDS);
        // 如果不允许相同用户登录
        if (singleLoginFlag) {
            redisTemplate.opsForValue().set(CommonConstant.REDIS_USER_LOGIN_KEY + userId, newToken, Duration.ofMillis(jwtExpires));
        }
        return newToken;
    }

}
