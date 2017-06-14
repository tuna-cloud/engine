package com.github.io.protocol.test.example.simpleexample;

import com.github.io.protocol.annotation.AsciiString;
import com.github.io.protocol.annotation.ByteOrder;
import com.github.io.protocol.annotation.Number;
import com.github.io.protocol.core.ProtocolEngine;
import com.github.io.protocol.utils.HexStringUtil;
import org.junit.Test;

public class ProtocolTest {
    @Number(width = 16, order = ByteOrder.BigEndian)
    private int header;
    @Number(width = 8)
    private int version;
    @Number(width = 16, order = ByteOrder.BigEndian)
    private int contentLength;
    @AsciiString(length = "getContentLength")
    private String asccString;

    @Test
    public void test() throws Exception {
        ProtocolEngine engine = new ProtocolEngine();

        /**
         * First we test the encode case
         */

        ProtocolTest test = new ProtocolTest();
        test.setHeader(0x2882);
        test.setVersion(1);
        test.setContentLength(12);
        test.setAsccString("HELLO, WORLD");

        // we encode the ProtocolTest object to byte buffer.
        byte[] buf = engine.encode(test);
        System.out.println(HexStringUtil.toHexString(buf));
        // 2882010C0048454C4C4F2C20574F524C44

        // ok, now we decode the ProtocolTest object from byte[] array.
        ProtocolTest testDecode = engine.decode(buf, ProtocolTest.class);

        System.out.println(testDecode.toString());
        // ProtocolTest{header=10370, version=1, contentLength=12, asccString='HELLO, WORLD'}
        // wow... is it work?
    }

    public int getHeader() {
        return header;
    }

    public void setHeader(int header) {
        this.header = header;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public String getAsccString() {
        return asccString;
    }

    public void setAsccString(String asccString) {
        this.asccString = asccString;
    }

    @Override
    public String toString() {
        return "ProtocolTest{" +
                "header=" + header +
                ", version=" + version +
                ", contentLength=" + contentLength +
                ", asccString='" + asccString + '\'' +
                '}';
    }
}
