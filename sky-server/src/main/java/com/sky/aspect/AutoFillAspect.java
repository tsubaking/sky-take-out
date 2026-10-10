package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import com.sky.exception.AspectException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 自定义切面，实现公共字段自动填充处理逻辑
 */
@Aspect
@Component
@Slf4j
public class AutoFillAspect {

    //切入点
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void pointCut(){};

    //前置通知
    @Before("pointCut()")
    public void autoFill(JoinPoint joinPoint) {
        log.info("开始进行公共字段自动填充...");

        //从当前被拦截的方法上获取注解信息
        //方法签名对象
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //方法签名上的注解
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);
        OperationType type = autoFill.value(); //数据库操作类型

        //当前拦截方法的参数：实体对象entity
        Object[] args = joinPoint.getArgs();
        if(args == null || args.length == 0)
            return;


        //准备赋值的数据
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();

        Object entity = args[0];

        //根据数据库操作类型，进行不同的公共字段填充
        //insert操作
        if(OperationType.INSERT.equals(type)) {
            try {
                Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                //通过反射为对象属性赋值
                setCreateTime.invoke(entity,now);
                setCreateUser.invoke(entity,currentId);
                setUpdateTime.invoke(entity,now);
                setUpdateUser.invoke(entity,currentId);

            } catch (NoSuchMethodException e) {
                throw new AspectException("AutoFillAspect发生异常");
            } catch (InvocationTargetException e) {
                throw new AspectException("AutoFillAspect发生异常");

            } catch (IllegalAccessException e) {
                throw new AspectException("AutoFillAspect发生异常");

            }
        }
        //update操作
        else {
            try {
                Method  setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                //通过反射为对象属性赋值
                setUpdateTime.invoke(entity,now);
                setUpdateUser.invoke(entity,currentId);
            } catch (NoSuchMethodException e) {
                throw new AspectException("AutoFillAspect发生异常");

            } catch (InvocationTargetException e) {
                throw new AspectException("AutoFillAspect发生异常");

            } catch (IllegalAccessException e) {
                throw new AspectException("AutoFillAspect发生异常");

            }
        }
    }
}
