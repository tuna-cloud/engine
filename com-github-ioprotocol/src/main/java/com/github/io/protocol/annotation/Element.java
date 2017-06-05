package com.github.io.protocol.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annocation which can use a java struct JavaBean as a property of another
 * JavaBean.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Element {

    /**
     * The number array length. user define themself.
     * @return The number array length. default value is 1.
     */
    String length() default "1";
}
