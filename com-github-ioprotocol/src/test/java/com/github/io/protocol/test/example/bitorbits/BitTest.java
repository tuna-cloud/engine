package com.github.io.protocol.test.example.bitorbits;

import com.github.io.protocol.annotation.Number;
import com.github.io.protocol.core.ProtocolEngine;
import com.github.io.protocol.utils.HexStringUtil;
import org.junit.Test;

/*
 *+------------------------------------+--------------------------+-------------+-------------------+
 *|                Byte1               |              Byte2       |     Byte3   |         Byte4     |
 *| Bit0 | Bit1~Bit2 | Bit3~Bit5 | Bit6~Bit9 | Bit10~Bit14 | Bit15~Bit20 | Bit21~Bit27 | Bit28~Bit31|
 *| Col1 |   Col2    |   Col3    |  Col4     |    Col5     |    Col6     |   Col7      |  Col8      |
 *+------+-----------+-----------+-----------+-------------+-------------+-------------+------------+
 */

public class BitTest {
    @Number(width = 1)
    private int col1;
    @Number(width = 2)
    private int col2;
    @Number(width = 3)
    private int col3;
    @Number(width = 4)
    private int col4;
    @Number(width = 5)
    private int col5;
    @Number(width = 6)
    private int col6;
    @Number(width = 7)
    private int col7;
    /**
     * Below total has 28 bit, we must keep it byte align, so I add a col8 to fill the last 4 bit.
     */
    @Number(width = 4)
    private int col8;

    @Test
    public void test() throws Exception {
        ProtocolEngine engine = new ProtocolEngine();

        BitTest test = new BitTest();

        test.setCol1(0B1);
        test.setCol2(0B10);
        test.setCol3(0B101);
        test.setCol4(0B1001);
        test.setCol5(0B10011);
        test.setCol6(0B100110);
        test.setCol7(0B1001101);
        test.setCol8(0B1101);

        byte[] buf = engine.encode(test);
        System.out.println(HexStringUtil.toHexString(buf));

        BitTest decodeObj = engine.decode(buf, BitTest.class);
        System.out.println(decodeObj.toString());
    }

    public int getCol1() {
        return col1;
    }

    public void setCol1(int col1) {
        this.col1 = col1;
    }

    public int getCol2() {
        return col2;
    }

    public void setCol2(int col2) {
        this.col2 = col2;
    }

    public int getCol3() {
        return col3;
    }

    public void setCol3(int col3) {
        this.col3 = col3;
    }

    public int getCol4() {
        return col4;
    }

    public void setCol4(int col4) {
        this.col4 = col4;
    }

    public int getCol5() {
        return col5;
    }

    public void setCol5(int col5) {
        this.col5 = col5;
    }

    public int getCol6() {
        return col6;
    }

    public void setCol6(int col6) {
        this.col6 = col6;
    }

    public int getCol7() {
        return col7;
    }

    public void setCol7(int col7) {
        this.col7 = col7;
    }

    public int getCol8() {
        return col8;
    }

    public void setCol8(int col8) {
        this.col8 = col8;
    }

    @Override
    public String toString() {
        return "BitTest{" +
                "col1=" + col1 +
                ", col2=" + col2 +
                ", col3=" + col3 +
                ", col4=" + col4 +
                ", col5=" + col5 +
                ", col6=" + col6 +
                ", col7=" + col7 +
                ", col8=" + col8 +
                '}';
    }
}
