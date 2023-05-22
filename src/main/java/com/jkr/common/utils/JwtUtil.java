package com.jkr.common.utils;

import cn.hutool.extra.servlet.ServletUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author：jikeruan
 * @Description:
 * @Date: 2019/11/8 8:40
 */
@Component
public class JwtUtil {

    public static final String SECRET = "XX#$%()(#*!()!KL<><MQLMNQNQJQK sdfkjsdrow32234545fdf>?N<:{LWPW";
    public static final String TOKEN_PREFIX = "Bearer";
    public static final String HEADER_STRING = "authorization";

    public static String generateToken(String userJson, long jwtExpires) {
        HashMap<String, Object> map = new HashMap<>();
        //you can put any data in the map
        map.put("userJson", userJson);
        LocalDateTime localDateTime = LocalDateTime.now();
        Date date = Date.from(localDateTime.plusMinutes(jwtExpires).atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .setClaims(map)
                //过期时间
                .setExpiration(date)
                //SECRET是加密算法对应的密钥，这里使用额是HS256加密算法
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    public static String validateToken(String token) {
        if (token != null) {
            // parse the token.
            try {
                Map<String, Object> body = Jwts.parser()
                        .setSigningKey(SECRET)
                        .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                        .getBody();
                String userJson = (String) (body.get("userJson"));
                if (userJson == null || userJson.isEmpty()) {
                    throw new TokenValidationException("Wrong token without username");
                } else {
                    return userJson;
                }
            } catch (ExpiredJwtException expiredJwtException) {
                throw new ExpiredJwtException(expiredJwtException.getHeader(), expiredJwtException.getClaims(), expiredJwtException.getMessage());
            }
        } else {
            throw new TokenValidationException("Missing token");
        }
    }

    /**
     * 获取token
     *
     * @param request
     * @return
     */
    public static String getAuthToken(HttpServletRequest request) {
        Cookie cookie = ServletUtil.getCookie(request, JwtUtil.HEADER_STRING);
        if (cookie != null) {
            return cookie.getValue();
        }
        return null;
    }

    static class TokenValidationException extends RuntimeException {
        public TokenValidationException(String msg) {
            super(msg);
        }
    }
}
