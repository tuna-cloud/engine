package com.github.io.protocol.test.datetime;

import com.github.io.protocol.annotation.DateTime;
import com.github.io.protocol.test.ICoderTest;
import com.github.io.protocol.test.TestEngine;
import org.junit.Assert;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by xsy on 2017/3/4.
 */
public class TestDatetime extends TestEngine implements ICoderTest {
    @DateTime(isBCDCode = true)
    private long time1;
    @DateTime
    private long time2;

    public long getTime1() {
        return time1;
    }

    public void setTime1(long time1) {
        this.time1 = time1;
    }

    public long getTime2() {
        return time2;
    }

    public void setTime2(long time2) {
        this.time2 = time2;
    }

    @Test
    @Override
    public void testEncode() throws Exception {
        TestDatetime t = new TestDatetime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");

        Date d = sdf.parse("170101121212");

        long time = d.getTime();
        t.setTime1(time);
        t.setTime2(time);

        byte[] buf = encode(t);

        Assert.assertTrue(buf[0] == (byte)0x17);
        Assert.assertTrue(buf[1] == (byte)0x01);
        Assert.assertTrue(buf[2] == (byte)0x01);
        Assert.assertTrue(buf[3] == (byte)0x12);
        Assert.assertTrue(buf[4] == (byte)0x12);
        Assert.assertTrue(buf[5] == (byte)0x12);

        Assert.assertTrue(buf[6] == (byte)17);
        Assert.assertTrue(buf[7] == (byte)1);
        Assert.assertTrue(buf[8] == (byte)1);
        Assert.assertTrue(buf[9] == (byte)12);
        Assert.assertTrue(buf[10] == (byte)12);
        Assert.assertTrue(buf[11] == (byte)12);
    }

    @Test
    @Override
    public void testDecode() throws Exception {
        byte[] buf = new byte[]{0x17,0x01,0x01,0x12,0x12,0x12, 17, 1, 1, 12, 12, 12};
        TestDatetime t = decode(buf, TestDatetime.class);
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
        long time = sdf.parse("170101121212").getTime();
        Assert.assertTrue(time == t.getTime1());
        Assert.assertTrue(time == t.getTime2());
    }

    @Test
    @Override
    public void testPretty() throws Exception {
        TestDatetime t = new TestDatetime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");

        Date d = sdf.parse("170101121212");

        long time = d.getTime();
        t.setTime1(time);
        t.setTime2(time);

        System.out.println(toPreetyString(t));
    }
}
