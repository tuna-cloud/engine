package com.github.io.protocol.coder;

import com.github.io.protocol.core.BitBuffer;
import net.sf.cglib.beans.BeanMap;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * @Project:net-top-framwork-protocol
 * @Package net.top.framwork.protocolpool.core
 * @Description:
 * @author: xsy
 * @date： 2016/11/23
 * @version： V1.0
 */
public interface ICoder {
    public static String FIELD_TEMPLATE = "%s(%s=%s),";


    /**
     *
     * @param bitBuffer
     * @param beanMap
     * @param field
     * @param annotation
     */
    void decode(BitBuffer bitBuffer, BeanMap beanMap, Field field, Annotation annotation) throws Exception;

    /**
     *
     * @param bitBuffer
     * @param beanMap
     * @param field
     * @param annotation
     */
    void encode(BitBuffer bitBuffer, BeanMap beanMap, Field field, Annotation annotation) throws Exception;

    /**
     *
     * @param bitBuffer
     * @param beanMap
     * @param field
     * @param annotation
     * @return
     * @throws Exception
     */
    String toPrettyHexString(BitBuffer bitBuffer, BeanMap beanMap, Field field, Annotation annotation) throws Exception;
}
