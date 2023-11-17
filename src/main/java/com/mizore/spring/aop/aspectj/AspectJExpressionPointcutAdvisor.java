package com.mizore.spring.aop.aspectj;

import com.mizore.spring.aop.Pointcut;
import com.mizore.spring.aop.PointcutAdvisor;
import org.aopalliance.aop.Advice;

public class AspectJExpressionPointcutAdvisor implements PointcutAdvisor {


    // 切入点
    private AspectJExpressionPointcut pointcut;

    // 具体的拦截方法
    private Advice advice;

    // 表达式
    private String expression;

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public void setAdvice(Advice advice) {
        this.advice = advice;
    }

    @Override
    public Advice getAdvice() {
        return advice;
    }

    @Override
    public Pointcut getPointCut() {
        if (pointcut == null) {
            pointcut = new AspectJExpressionPointcut(expression);
        }
        return pointcut;
    }
}
