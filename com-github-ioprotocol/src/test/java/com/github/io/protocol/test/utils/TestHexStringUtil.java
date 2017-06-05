package com.github.io.protocol.test.utils;

import com.github.io.protocol.utils.HexStringUtil;
import org.apache.io.protocol.utils.*;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by xsy on 2017/3/10.
 */
public class TestHexStringUtil {
    @Test
    public void test() {
        /**
         * byte
         */
        Assert.assertTrue("F3".equals(HexStringUtil.toHexString((byte)0xf3)));
        /**
         * byte array
         */
        Assert.assertTrue("F1F2F3F4".equals(HexStringUtil.toHexString(new byte[]{(byte) 0xf1, (byte) 0xf2, (byte) 0xf3, (byte) 0xf4})));
        /**
         * short
         */
        Assert.assertTrue("F1F2".equals(HexStringUtil.toHexString((short)0xf1f2)));
        /**
         * short array
         */
        Assert.assertTrue("F1F280807070".equals(HexStringUtil.toHexString(new short[]{(short) 0xf1f2, (short) 0x8080, 0x7070})));
        /**
         * 24 bit int
         */
        Assert.assertTrue("F1F2F3".equals(HexStringUtil.toHexString3bytes(0xf1f2f3)));

        /**
         * int
         */
        Assert.assertTrue("F1F2F3F4".equals(HexStringUtil.toHexString(0xf1f2f3f4)));
        /**
         * int array
         */
        Assert.assertTrue("F1F2F3F4F1F2F3F4".equals(HexStringUtil.toHexString(new int[]{0xf1f2f3f4, 0xf1f2f3f4})));
        /**
         * long
         */
        Assert.assertTrue("F1F2F3F4F1F2F3F4".equals(HexStringUtil.toHexString(0xf1f2f3f4f1f2f3f4l)));
        /**
         * long array
         */
        Assert.assertTrue("F1F2F3F4F1F2F3F4F1F2F3F4F1F2F3F4".equals(HexStringUtil.toHexString(new long[]{0xf1f2f3f4f1f2f3f4L,0xf1f2f3f4f1f2f3f4L})));
    }
}
