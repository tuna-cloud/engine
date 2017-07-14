package com.github.io.protocol.test.utils;

import com.github.io.protocol.utils.NumberCastUtil;
import org.junit.Assert;
import org.junit.Test;

public class TestNumberCastUtil {

    @Test
    public void testByte() throws Exception {
        byte v = (byte) 0xF0;
        Byte b = (Byte) NumberCastUtil.cast(v, "byte");
        Assert.assertTrue(v == b.byteValue());

        Short b1 = (Short) NumberCastUtil.cast(v, "short");
        Assert.assertTrue(v == b1);
        Short b2 = (Short) NumberCastUtil.cast(v, "Short[]");
        Assert.assertTrue(v == b2);

        Integer b3 = (Integer) NumberCastUtil.cast(v, "int");
        Assert.assertTrue(v == b3);
        Integer b4 = (Integer) NumberCastUtil.cast(v, "int[]");
        Assert.assertTrue(v == b4);
        Integer b5 = (Integer) NumberCastUtil.cast(v, "Integer");
        Assert.assertTrue(v == b5);
        Integer b6 = (Integer) NumberCastUtil.cast(v, "Integer[]");
        Assert.assertTrue(v == b6);
        Long b7 = (Long) NumberCastUtil.cast(v, "long");
        Assert.assertTrue(v == b7);
        Long b8 = (Long) NumberCastUtil.cast(v, "Long[]");
        Assert.assertTrue(v == b8);
    }

    @Test
    public void testShort() throws Exception {
        short v = (short) 0xFF00;
        Short b1 = (Short) NumberCastUtil.cast(v, "short");
        Assert.assertTrue(v == b1);
        Short b2 = (Short) NumberCastUtil.cast(v, "Short[]");
        Assert.assertTrue(v == b2);

        Integer b3 = (Integer) NumberCastUtil.cast(v, "int");
        Assert.assertTrue(v == b3);
        Integer b4 = (Integer) NumberCastUtil.cast(v, "int[]");
        Assert.assertTrue(v == b4);
        Integer b5 = (Integer) NumberCastUtil.cast(v, "Integer");
        Assert.assertTrue(v == b5);
        Integer b6 = (Integer) NumberCastUtil.cast(v, "Integer[]");
        Assert.assertTrue(v == b6);
        Long b7 = (Long) NumberCastUtil.cast(v, "long");
        Assert.assertTrue(v == b7);
        Long b8 = (Long) NumberCastUtil.cast(v, "Long[]");
        Assert.assertTrue(v == b8);
    }

    @Test
    public void testInt() throws Exception {
        int v = 0xFF000000;

        Integer b3 = (Integer) NumberCastUtil.cast(v, "int");
        Assert.assertTrue(v == b3);
        Integer b4 = (Integer) NumberCastUtil.cast(v, "int[]");
        Assert.assertTrue(v == b4);
        Integer b5 = (Integer) NumberCastUtil.cast(v, "Integer");
        Assert.assertTrue(v == b5);
        Integer b6 = (Integer) NumberCastUtil.cast(v, "Integer[]");
        Assert.assertTrue(v == b6);
        Long b7 = (Long) NumberCastUtil.cast(v, "long");
        Assert.assertTrue(v == b7);
        Long b8 = (Long) NumberCastUtil.cast(v, "Long[]");
        Assert.assertTrue(v == b8);
    }

    @Test
    public void testLong() throws Exception {
        long v = 0xFF00000000000000L;

        Long b7 = (Long) NumberCastUtil.cast(v, "long");
        Assert.assertTrue(v == b7);
        Long b8 = (Long) NumberCastUtil.cast(v, "Long[]");
        Assert.assertTrue(v == b8);
    }
}
