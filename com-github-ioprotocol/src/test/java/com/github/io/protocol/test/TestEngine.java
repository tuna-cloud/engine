package com.github.io.protocol.test;

import com.github.io.protocol.core.ProtocolEngine;

/**
 * @Project:net-top-framwork-protocol
 * @Package net.top.framwork.protocolpool.test
 * @Description:
 * @author: xsy
 * @date： 2016/11/25
 * @version： V1.0
 */
public class TestEngine {
    public static ProtocolEngine engin = new ProtocolEngine();
    public static <T> T decode(byte[] buf, Class<T> cls) throws Exception {
        return engin.decode(buf, cls);
    }

    public static byte[] encode(Object obj) throws Exception {
        return engin.encode(obj);
    }

    public static String toPreetyString(Object obj) throws Exception {
        return engin.toPrettyHexString(obj);
    }
}
