package com.github.io.protocol.coder.impl;

import net.sf.cglib.beans.BeanMap;
import com.github.io.protocol.annotation.AsciiString;
import com.github.io.protocol.coder.ICoder;
import com.github.io.protocol.core.BitBuffer;
import com.github.io.protocol.core.CoderHelper;
import com.github.io.protocol.utils.HexStringUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * @Project:org-apache-io-protocol
 * @Package org.apache.io.protocol.coder.impl
 * @Description:
 * @author: xsy
 * @date： 2016/11/25
 * @version： V1.0
 */
public class AsciiStringCoder implements ICoder {

    public void decode(BitBuffer bitBuffer, BeanMap beanMap, Field field, Annotation annotation) throws Exception {
        AsciiString anno = (AsciiString) annotation;
        int length = CoderHelper.caculateArrayLength(beanMap.getBean(), anno.length());
        if (length > 0) {
            byte[] buf = bitBuffer.readBytes(new byte[length]);
            beanMap.put(field.getName(), new String(buf, anno.charsetName()));
        }
    }

    public void encode(BitBuffer bitBuffer, BeanMap beanMap, Field field, Annotation annotation) throws Exception {
        AsciiString arrayAnnocation = (AsciiString) annotation;
        int length = CoderHelper.caculateArrayLength(beanMap.getBean(), arrayAnnocation.length());

        String value = (String) beanMap.get(field.getName());
        byte[] buf = value.getBytes(arrayAnnocation.charsetName());

        if(length != buf.length)
            throw new Exception(field.getName() + " length is not equal to " + length);

        if (buf != null && buf.length > 0) {
            bitBuffer.write(buf);
        }
    }

    @Override
    public String toPrettyHexString(BitBuffer bitBuffer, BeanMap beanMap, Field field, Annotation annotation) throws Exception {
        AsciiString arrayAnnocation = (AsciiString) annotation;
        String value = (String) beanMap.get(field.getName());
        return String.format(FIELD_TEMPLATE, HexStringUtil.toHexString(value, arrayAnnocation.charsetName()), field.getName(), value);
    }
}
