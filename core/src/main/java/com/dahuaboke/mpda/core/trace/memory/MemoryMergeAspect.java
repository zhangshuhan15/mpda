package com.dahuaboke.mpda.core.trace.memory;

import com.dahuaboke.mpda.core.agent.scene.Scene;
import com.dahuaboke.mpda.core.consts.Constants;
import com.dahuaboke.mpda.core.trace.TraceManager;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

@Aspect
@Component
public class MemoryMergeAspect {

    @Autowired
    private TraceManager traceManager;

    @Pointcut("@annotation(com.dahuaboke.mpda.core.trace.memory.MemoryMerge)")
    public void pointcut() {
    }

    @Before("pointcut()")
    public void before(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        MemoryMerge memoryMerge = method.getAnnotation(MemoryMerge.class);
        Class<? extends Scene>[] value = memoryMerge.value();
        if (!ArrayUtils.isEmpty(value)) {
            List<String> sceneIds = Arrays.stream(value).map(clz -> traceManager.getSceneId(clz)).toList();
            traceManager.getAttribute().put(Constants.SCENE_MERGE, sceneIds);
        }
    }
}
