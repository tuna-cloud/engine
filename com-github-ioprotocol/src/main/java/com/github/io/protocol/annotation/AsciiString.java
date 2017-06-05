package com.github.io.protocol.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annocation marked on a property of a JavaBean which can
 * convert <code>String</code> field to <code>byte[]</code>
 * or <code>byte[]</code> to <code>String</code>.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AsciiString {

    /**
     * The number array length.
     * @return The number array length. default value is 1.
     */
    String length() default "1";

    /**
     * The charset for encode or decode.
     * @return The charset name.
     */
    String charsetName() default "utf-8";
}
