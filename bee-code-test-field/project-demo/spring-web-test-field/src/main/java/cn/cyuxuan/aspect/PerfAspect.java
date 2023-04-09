package cn.cyuxuan.aspect;

import cn.cyuxuan.util.ExecuteInfoUtils;
import cn.cyuxuan.util.PerfThreadUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * 函数执行时间分析
 * 1. 利用切面获取对应的函数执行，在方法执行 before 和 after 的时候执行统计
 * 2. before拦截到的时候，使用线程本地变量来记录当前线程执行的方法栈信息
 * 2.1
 * 3.
 *
 * @author 陈玉轩
 * @since 23.03.26
 */
//@Aspect
//@Component
public class PerfAspect {


    @Pointcut(value = "execution(* cn.cyuxuan..*.*(..))")
    public void pointCut() {

    }


    /**
     * 前置通知
     *
     * @param joinPoint 切点
     */
    @Before(value = "pointCut()")
    public void before(JoinPoint joinPoint) {
        // 获取当前方法签名
        Signature signature = joinPoint.getSignature();
        // 生成函数执行的运行数据签名
        ExecuteInfoUtils.getExeMethodSignature(joinPoint);
        // 获取入参数据 + 出参数据 + 方法名称做一个签名
        MethodSignature methodSignature = (MethodSignature) signature;
        // 获取方法名称
        String methodName = methodSignature.getMethod().getName();
        // 通过 method name 生成一组执行数据并记录下来
        this.handleBeforeData(methodName);
    }

    /**
     * 后置执行函数
     *
     * @param joinPoint
     */
    @After(value = "pointCut()")
    public void after(JoinPoint joinPoint) {
        // 处理方法执行后的数据
        this.handleAfterData();
    }

    /**
     * 处理方法执行前的数据处理
     *
     * @param methodName 方法名称
     */
    private void handleBeforeData(String methodName) {
        PerfThreadUtils.handleBeforeData(methodName);
    }


    /**
     * 方法执行后的书处理
     */
    private void handleAfterData() {
        PerfThreadUtils.handleAfterData();
    }
}
