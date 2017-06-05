package com.github.io.protocol.test.number;

import com.github.io.protocol.annotation.ByteOrder;
import com.github.io.protocol.annotation.Number;
import com.github.io.protocol.test.TestEngine;
import com.github.io.protocol.utils.HexStringUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by xsy on 2017/3/22.
 */
public class TestNumberCase {
    @Number(width = 8)
    private int case1;
    @Number(width = 16)
    private int case2;
    @Number(width = 32)
    private long case3;
    @Number(width = 64, order = ByteOrder.BigEndian)
    private long case4;

    @Test
    public void test1() throws Exception {
        TestNumberCase testNumberCase = new TestNumberCase();
        testNumberCase.setCase1(255);
        testNumberCase.setCase2(0x1233);
        testNumberCase.setCase3(0x01020304);
        testNumberCase.setCase4(0x0807060504030201L);
        byte[] buf = TestEngine.encode(testNumberCase);
        System.out.println(HexStringUtil.toHexString(buf));
        Assert.assertTrue(buf.length == 15);
        Assert.assertTrue(buf[0] == (byte)255);
        Assert.assertTrue(buf[1] == (byte)0x33);
        Assert.assertTrue(buf[2] == (byte)0x12);
        Assert.assertTrue(buf[3] == (byte)0x04);
        Assert.assertTrue(buf[4] == (byte)0x03);
        Assert.assertTrue(buf[5] == (byte)0x02);
        Assert.assertTrue(buf[6] == (byte)0x01);
        System.out.println(TestEngine.toPreetyString(testNumberCase));
    }

    public int getCase1() {
        return case1;
    }

    public void setCase1(int case1) {
        this.case1 = case1;
    }

    public int getCase2() {
        return case2;
    }

    public void setCase2(int case2) {
        this.case2 = case2;
    }

    public long getCase3() {
        return case3;
    }

    public void setCase3(long case3) {
        this.case3 = case3;
    }

    public long getCase4() {
        return case4;
    }

    public void setCase4(long case4) {
        this.case4 = case4;
    }
}
