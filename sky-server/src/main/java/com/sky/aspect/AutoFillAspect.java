package com.sky.aspect;


import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
public class AutoFillAspect {

    //定义切入点
    // 返回值任意的，com.sky.mapper包下的任意类的任意参数的任意方法 且配置注解为AutoFill
    @Pointcut("execution(* com.sky.mapper.*.*(*)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut(){}


    //连接切入点和通知，形成切面
    @Before(value = "autoFillPointCut()")
    //定义通知
    //在通知中完成公共字段的赋值
    public void AutoFill(JoinPoint joinPoint){
        log.info("公共字段执行自动配置...");
        //实现公共字段自动填充功能
        //0.获取拦截到的具体的数据库操作方法的签名signature
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //1.获得数据库操作类型 insert、update
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);
        OperationType operationType = autoFill.value();
        //2.获取操作所传入的entity参数对象
        Object[] args = joinPoint.getArgs();
        //默认约定第一个参数是传入的entity对象
        if (args == null || args.length == 0) return ;
        Object entity = args[0];
        //3.准备entity对象需要配置的属性
        LocalDateTime now = LocalDateTime.now();
        Long id = BaseContext.getCurrentId();
        //4.根据数据库操作类型对entity不同属性进行配置
        if (operationType == OperationType.INSERT){
            //为4种属性进行配置
            try {
                //反射得到对应的set方法
                Method setCurrentUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setCurrentTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME,LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER,Long.class);
                Method setUpdateTime =entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME,LocalDateTime.class);

                //通过反射为对象属性赋值(调用方法的对象，参数)
                setCurrentUser.invoke(entity,id);
                setCurrentTime.invoke(entity,now);
                setUpdateUser.invoke(entity,id);
                setUpdateTime.invoke(entity,now);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if (operationType == OperationType.UPDATE) {
            //为2种属性进行配置
            try {
                //反射得到对应的set方法
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER,Long.class);
                Method setUpdateTime =entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME,LocalDateTime.class);

                //通过反射为对象属性赋值(调用方法的对象，参数)
                setUpdateUser.invoke(entity,id);
                setUpdateTime.invoke(entity,now);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
