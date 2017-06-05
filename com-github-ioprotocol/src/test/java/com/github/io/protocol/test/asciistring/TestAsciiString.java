package com.github.io.protocol.test.asciistring;

import com.github.io.protocol.annotation.AsciiString;
import com.github.io.protocol.test.TestEngine;
import com.github.io.protocol.utils.HexStringUtil;
import org.junit.Test;

/**
 * Created by xsy on 2017/3/21.
 */
public class TestAsciiString {
    @AsciiString(length = "4")
    private String case1;
    @AsciiString(length = "6")
    private String case2;
    @AsciiString(length = "case3length", charsetName = "gbk")
    private String case3;

    @Test
    public void test1() throws Exception {
        TestAsciiString t = new TestAsciiString();
        t.setCase1("1234");
        t.setCase2("hellow");
        t.setCase3("我是你");
        byte[] buffer = TestEngine.encode(t);
        System.out.println(HexStringUtil.toHexString(buffer));
        System.out.println(TestEngine.toPreetyString(t));
    }

    public int case3length() {
        return 6;
    }

    public String getCase1() {
        return case1;
    }

    public void setCase1(String case1) {
        this.case1 = case1;
    }

    public String getCase2() {
        return case2;
    }

    public void setCase2(String case2) {
        this.case2 = case2;
    }

    public String getCase3() {
        return case3;
    }

    public void setCase3(String case3) {
        this.case3 = case3;
    }
}
