package com.dahuaboke.mpda.rag.entity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Desc: 字段与问题绑定
 * @Author：zhh
 * @Date：2025/8/7 14:11
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FieldComment {

    String question();

    String keyWord() default "";

}
