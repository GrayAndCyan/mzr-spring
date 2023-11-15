package com.mizore.spring.aop.aspectj;

import com.mizore.spring.aop.ClassFilter;
import com.mizore.spring.aop.MethodMatcher;
import com.mizore.spring.aop.Pointcut;
import org.aspectj.weaver.tools.PointcutExpression;
import org.aspectj.weaver.tools.PointcutParser;
import org.aspectj.weaver.tools.PointcutPrimitive;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

// AspectJPointcut表达式类
public class AspectJExpressionPointcut implements Pointcut, MethodMatcher, ClassFilter {

    private static final Set<PointcutPrimitive> SUPPORTED_PRIMITIVES = new HashSet<>();


    static {
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.EXECUTION);
    }

    private final PointcutExpression pointcutExpression;
    public AspectJExpressionPointcut(String expression) {
        PointcutParser pointcutParser = PointcutParser
                .getPointcutParserSupportingSpecifiedPrimitivesAndUsingSpecifiedClassLoaderForResolution(
                        SUPPORTED_PRIMITIVES,
                        this.getClass().getClassLoader()
                );
        pointcutExpression = pointcutParser.parsePointcutExpression(expression);
    }

    @Override
    public boolean matches(Class<?> clazz) {
        return pointcutExpression.couldMatchJoinPointsInType(clazz);
    }

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        return pointcutExpression.matchesMethodExecution(method).alwaysMatches();
    }

    @Override
    public ClassFilter getClassFilter() {
        return this;
    }

    @Override
    public MethodMatcher getMethodMather() {
        return this;
    }
}
