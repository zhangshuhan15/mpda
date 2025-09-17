package com.dahuaboke.mpda.core.memory;

import com.dahuaboke.mpda.core.agent.scene.Scene;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MemoryMerge {

    Class<? extends Scene>[] value();
}
