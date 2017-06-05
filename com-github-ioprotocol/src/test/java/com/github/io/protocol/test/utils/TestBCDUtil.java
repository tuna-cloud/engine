package com.github.io.protocol.test.utils;

import com.github.io.protocol.utils.BCDUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * Created by xsy on 2017/3/10.
 */
public class TestBCDUtil {
    private byte[] buf = new byte[]{0x12,0x34,0x44, (byte) 0x85};
    @Test
    public void test1() {
        int value = BCDUtil.bcd2value(buf, 0, 4);
        Assert.assertTrue(12344485 == value);
    }

    @Test
    public void test2() {
        int value = BCDUtil.bcd2value(buf);
        Assert.assertTrue(12344485 == value);
    }

    @Test
    public void test3() {
        byte[] temp = BCDUtil.value2bcd(12344485, 8);
        Assert.assertTrue(Arrays.equals(buf, temp));
    }
}
