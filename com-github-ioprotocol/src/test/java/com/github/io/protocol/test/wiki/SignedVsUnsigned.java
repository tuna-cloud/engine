package com.github.io.protocol.test.wiki;

import com.github.io.protocol.annotation.Number;
import com.github.io.protocol.annotation.Sign;
import com.github.io.protocol.core.ProtocolEngine;
import com.github.io.protocol.utils.HexStringUtil;

public class SignedVsUnsigned {
    @Number(width = 8, sign = Sign.Signed)
    private Integer signedValue;
    @Number(width = 8, sign = Sign.Unsigned)
    private Integer unsignedValue;

    public static void main(String[] args) throws Exception {
        ProtocolEngine engine = new ProtocolEngine();

        SignedVsUnsigned bean = new SignedVsUnsigned();
        bean.setSignedValue(0xF0);
        bean.setUnsignedValue(0xF0);

        byte[] buf= engine.encode(bean);
        System.out.println(HexStringUtil.toHexString(buf));

        SignedVsUnsigned dbean = engine.decode(buf, SignedVsUnsigned.class);
        System.out.println(dbean.toString());
    }

    public Integer getSignedValue() {
        return signedValue;
    }

    public void setSignedValue(Integer signedValue) {
        this.signedValue = signedValue;
    }

    public Integer getUnsignedValue() {
        return unsignedValue;
    }

    public void setUnsignedValue(Integer unsignedValue) {
        this.unsignedValue = unsignedValue;
    }

    @Override
    public String toString() {
        return "SignedVsUnsigned{" +
                "signedValue=" + signedValue +
                ", unsignedValue=" + unsignedValue +
                '}';
    }
}
