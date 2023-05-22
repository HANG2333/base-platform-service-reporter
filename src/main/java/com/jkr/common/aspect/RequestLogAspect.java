package com.jkr.common.aspect;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.jkr.common.annotation.WebLog;
import com.jkr.modules.sys.log.model.SysLog;
import com.jkr.modules.sys.log.service.ISysLogService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zenglingwen
 */
@Slf4j
@Component
@Aspect
public class RequestLogAspect {

    @Autowired
    private ISysLogService iSysLogService;

    /**
     * ..表示包及子包 该方法代表controller层的所有方法
     * Pointcut定义时，还可以使用&&、||、! 这三个运算
     */
    @Pointcut("execution(public * com.jkr.modules..controller..*(..))")
    public void requestServer() {
    }

    @Around("requestServer()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        TimeInterval timer = DateUtil.timer();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Object result = proceedingJoinPoint.proceed();
        String url = request.getRequestURL().toString();
        String method = request.getMethod();
        // 获取连接点的方法签名对象
        Signature signature = proceedingJoinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        // 通过接口方法获取该方法上的 @WebLog 注解对象
        WebLog webLog = methodSignature.getMethod().getAnnotation(WebLog.class);
        String description = "";
        if(null != webLog){
            description = webLog.description();
        }
        String declaringTypeName = proceedingJoinPoint.getSignature().getDeclaringTypeName();
        String name = proceedingJoinPoint.getSignature().getName();
        String remoteAddr = getRemoteAddr(request);
        String params = JSONUtil.toJsonStr(getRequestParamsByProceedingJoinPoint(proceedingJoinPoint));
        String intervalPretty = timer.intervalPretty();
        // 请求开始（前置通知）
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 请求开始 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        // 请求链接
        log.info("请求链接：{}", url);
        // 接口方法描述信息
        log.info("接口描述：{}", description);
        // 请求类型
        log.info("请求类型：{}", method);
        // 请求方法
        log.info("请求方法：{}.{}", declaringTypeName, name);
        // 请求远程地址
        log.info("请求远程地址：{}", remoteAddr);
        // 请求入参
        log.info("请求入参：{}", params);
        // 请求耗时
        log.info("请求耗时：{}", intervalPretty);
        // 请求返回
        log.info("请求返回：{}", result);
        // 请求结束（后置通知）
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 请求结束 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<" + System.lineSeparator());
        SysLog sysLog = new SysLog();
        sysLog.setIp(remoteAddr);
        sysLog.setUrl(url);
        sysLog.setModal(description);
        sysLog.setHttpMethod(method);
        sysLog.setClassMethod(String.format("%s.%s", declaringTypeName, name));
        sysLog.setRequestParams(params);
        sysLog.setResult(JSONUtil.toJsonStr(result));
        sysLog.setTimeCost(intervalPretty);
        sysLog.setRequestStatus(0);
        iSysLogService.save(sysLog);
        return result;
    }


    @AfterThrowing(pointcut = "requestServer()", throwing = "e")
    public void doAfterThrow(JoinPoint joinPoint, RuntimeException e) {
        TimeInterval timer = DateUtil.timer();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 获取连接点的方法签名对象
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        // 通过接口方法获取该方法上的 @WebLog 注解对象
        WebLog webLog = methodSignature.getMethod().getAnnotation(WebLog.class);
        String description = "";
        if(null != webLog){
            description = webLog.description();
        }
        String url = request.getRequestURL().toString();
        String method = request.getMethod();
        String declaringTypeName = joinPoint.getSignature().getDeclaringTypeName();
        String name = joinPoint.getSignature().getName();
        String remoteAddr = getRemoteAddr(request);
        String params = JSONUtil.toJsonStr(getRequestParamsByJoinPoint(joinPoint));
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter,true));
        String exception = JSON.toJSONString(stringWriter.toString());
        String intervalPretty = timer.intervalPretty();
        // 请求开始（前置通知）
        log.error(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 请求开始 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        // 请求链接
        log.error("请求链接：{}", url);
        // 接口方法描述信息
        log.error("接口描述：{}", description);
        // 请求类型
        log.error("请求类型：{}", method);
        // 请求方法
        log.error("请求方法：{}.{}", declaringTypeName, name);
        // 请求远程地址
        log.error("请求远程地址：{}", remoteAddr);
        // 请求入参
        log.error("请求入参：{}", params);
        // 异常信息
        log.error("异常信息：{}", exception);
        // 请求耗时
        log.error("请求耗时：{}", intervalPretty);
        // 请求结束（后置通知）
        log.error(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 请求结束 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<" + System.lineSeparator());
        SysLog sysLog = new SysLog();
        sysLog.setIp(remoteAddr);
        sysLog.setUrl(url);
        sysLog.setModal(description);
        sysLog.setHttpMethod(method);
        sysLog.setClassMethod(String.format("%s.%s", declaringTypeName, name));
        sysLog.setRequestParams(params);
        sysLog.setException(exception);
        sysLog.setTimeCost(intervalPretty);
        sysLog.setRequestStatus(1);
        iSysLogService.save(sysLog);
    }

    /**
     * 获取入参
     *
     * @param proceedingJoinPoint
     * @return
     */
    private Map<String, Object> getRequestParamsByProceedingJoinPoint(ProceedingJoinPoint proceedingJoinPoint) {
        //参数名
        String[] paramNames = ((MethodSignature) proceedingJoinPoint.getSignature()).getParameterNames();
        //参数值
        Object[] paramValues = proceedingJoinPoint.getArgs();
        return buildRequestParam(paramNames, paramValues);
    }

    private Map<String, Object> getRequestParamsByJoinPoint(JoinPoint joinPoint) {
        //参数名
        String[] paramNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        //参数值
        Object[] paramValues = joinPoint.getArgs();
        return buildRequestParam(paramNames, paramValues);
    }

    private Map<String, Object> buildRequestParam(String[] paramNames, Object[] paramValues) {
        Map<String, Object> requestParams = new HashMap<>();
        for (int i = 0; i < paramNames.length; i++) {
            Object value = paramValues[i];
            //如果是文件对象
            if (value instanceof MultipartFile) {
                MultipartFile file = (MultipartFile) value;
                //获取文件名
                value = file.getOriginalFilename();
            }
            //如果是批量文件上传
            if (value instanceof List) {
                System.out.println("Yes...");
                try {
                    List<MultipartFile> multipartFiles = castList(value, MultipartFile.class);
                    if (multipartFiles != null) {
                        List<String> fileNames = new ArrayList<>();
                        for (MultipartFile file : multipartFiles) {
                            fileNames.add(file.getOriginalFilename());
                        }
                        requestParams.put(paramNames[i], fileNames);
                        break;
                    }
                } catch (ClassCastException e) {
                    //忽略不是文件类型的List
                }
            }
            requestParams.put(paramNames[i], value);
        }
        return requestParams;
    }

    public static <T> List<T> castList(Object obj, Class<T> clazz) {
        List<T> result = new ArrayList<T>();
        if (obj instanceof List<?>) {
            for (Object o : (List<?>) obj) {
                result.add(clazz.cast(o));
            }
            return result;
        }
        return null;
    }

    /**
     * @title: getRemoteAddr
     * @author: 曾令文
     * @date: 2022/6/15 11:33
     * @description: 获得用户远程地址
     * @param: [request]
     * @return: java.lang.String
     */
    public static String getRemoteAddr(HttpServletRequest request) {
        String remoteAddr = request.getHeader("X-Real-IP");
        if (StrUtil.isBlank(remoteAddr)) {
            remoteAddr = request.getHeader("X-Forwarded-For");
        } else if (StrUtil.isBlank(remoteAddr)) {
            remoteAddr = request.getHeader("Proxy-Client-IP");
        } else if (StrUtil.isBlank(remoteAddr)) {
            remoteAddr = request.getHeader("WL-Proxy-Client-IP");
        }
        return remoteAddr != null ? remoteAddr : request.getRemoteAddr();
    }

}