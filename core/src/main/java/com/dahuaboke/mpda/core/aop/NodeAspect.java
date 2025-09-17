package com.dahuaboke.mpda.core.aop;


import com.dahuaboke.mpda.core.context.trace.TraceManager;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * auth: dahua
 * time: 2025/9/17 10:14
 */
@Aspect
@Component
public class NodeAspect {

    @Autowired
    private TraceManager traceManager;

    @Pointcut("within(com.dahuaboke.mpda.core.node..*) && within(com.alibaba.cloud.ai.graph.action.NodeAction+) && execution(* apply(..))")
    public void nodePointcut() {
    }

    @Before("sceneWrapperPointcut()")
    public void beforeExecute() {

    }
}
