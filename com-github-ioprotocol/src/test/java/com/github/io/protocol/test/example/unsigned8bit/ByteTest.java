package com.github.io.protocol.test.example.unsigned8bit;

import com.github.io.protocol.annotation.Number;
import com.github.io.protocol.core.ProtocolEngine;
import com.github.io.protocol.utils.HexStringUtil;
import org.junit.Assert;
import org.junit.Test;

public class ByteTest {
    /**
     * While we want to represent a 8-bit unsigned value in java, even though we can use byte or Byte,
     * But while the value greater than 128, the value in java will be a negative number.
     * So we use short/Short/int/Integer/long/Long to represent a 8-bit unsigned value.
     */
    @Number(width = 8)
    private short col1;

    @Number(width = 8)
    private Short col2;

    @Number(width = 8)
    private int col3;

    @Number(width = 8)
    private Integer col4;

    @Number(width = 8)
    private long col5;

    @Number(width = 8)
    private Long col6;

    @Test
    public void test() throws Exception {
        ProtocolEngine engine = new ProtocolEngine();

        ByteTest test = new ByteTest();

        test.setCol1((short) 200);
        test.setCol2((short) 200);
        test.setCol3(200);
        test.setCol4(200);
        test.setCol5(200l);
        test.setCol6(200l);

        byte[] buf = engine.encode(test);

        for(int i = 0; i < 6; i++) {
            Assert.assertTrue(buf[i] == (byte)200);
        }

        System.out.println(HexStringUtil.toHexString(buf));

        ByteTest decodeTest = engine.decode(buf, ByteTest.class);
        System.out.println(decodeTest.toString());

        Assert.assertTrue(decodeTest.getCol1() == 200);
        Assert.assertTrue(decodeTest.getCol2() == 200);
        Assert.assertTrue(decodeTest.getCol3() == 200);
        Assert.assertTrue(decodeTest.getCol4() == 200);
        Assert.assertTrue(decodeTest.getCol5() == 200);
        Assert.assertTrue(decodeTest.getCol6() == 200);
    }


    @Override
    public String toString() {
        return "ByteTest{" +
                "col1=" + col1 +
                ", col2=" + col2 +
                ", col3=" + col3 +
                ", col4=" + col4 +
                ", col5=" + col5 +
                ", col6=" + col6 +
                '}';
    }

    public short getCol1() {
        return col1;
    }

    public void setCol1(short col1) {
        this.col1 = col1;
    }

    public Short getCol2() {
        return col2;
    }

    public void setCol2(Short col2) {
        this.col2 = col2;
    }

    public int getCol3() {
        return col3;
    }

    public void setCol3(int col3) {
        this.col3 = col3;
    }

    public Integer getCol4() {
        return col4;
    }

    public void setCol4(Integer col4) {
        this.col4 = col4;
    }

    public long getCol5() {
        return col5;
    }

    public void setCol5(long col5) {
        this.col5 = col5;
    }

    public Long getCol6() {
        return col6;
    }

    public void setCol6(Long col6) {
        this.col6 = col6;
    }
}
