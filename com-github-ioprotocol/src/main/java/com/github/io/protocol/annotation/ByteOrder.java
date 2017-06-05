package com.github.io.protocol.annotation;

/**
 * A typesafe enumeration for byte orders.
 * The internet comunication is relative two case,
 * big endian: 0x3211 convert to buffer is new byte[]{0x32, 0x11}
 * small endian: 0x3211 convert to buffer is new byte[]{0x11,0x32}
 */
public enum ByteOrder {
    /**
     * Constant denoting big-endian byte order.  In this order, the bytes of a
     * multibyte value are ordered from most significant to least significant.
     */
    BigEndian,
    /**
     * Constant denoting little-endian byte order.  In this order, the bytes of
     * a multibyte value are ordered from least significant to most
     * significant.
     */
    SmallEndian
}
