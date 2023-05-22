package com.jkr.common.aspect;

import cn.hutool.core.collection.CollUtil;
import com.jkr.common.exception.ValidException;
import com.jkr.common.model.ValidBaseInterface;
import com.jkr.common.utils.ValidationUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @Author：jikeruan
 * @Description: 校验aop
 * @Date: 2019/10/14 13:28
 */
@Aspect
@Slf4j
@Component
public class ValidResultAspect {

    /**
     * ..表示包及子包 该方法代表controller层的所有方法
     * Pointcut定义时，还可以使用&&、||、! 这三个运算
     */
    @Pointcut("@annotation(com.jkr.common.annotation.ValidResult)")
    public void controllerMethod() {

    }

    /**
     * 前置通知
     *
     * @param joinPoint
     * @throws Exception
     */
    @Before("controllerMethod()")
    public void valid(JoinPoint joinPoint) throws Exception {
        for (Object arg : joinPoint.getArgs()) {
            // 是否实现校验父接口
            if (arg instanceof ValidBaseInterface) {
                // 验证对象
                ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(arg);
                if (CollUtil.isNotEmpty(validResult.getAllErrors())) {
                    for (ValidationUtil.ErrorMessage error : validResult.getAllErrors()) {
                        throw new ValidException(error.getPropertyPath() + "：" + error.getMessage());
                    }
                }
            }
        }
    }


    /**
     * 异常通知
     *
     * @param joinPoint
     * @param exception
     */
    @AfterThrowing(value = "controllerMethod()", throwing = "exception")
    public void doAfterThrowingAdvice(JoinPoint joinPoint, Throwable exception) {

    }
}
