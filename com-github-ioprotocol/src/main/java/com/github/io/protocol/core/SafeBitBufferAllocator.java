package com.github.io.protocol.core;

public class SafeBitBufferAllocator {

    /**
     * The {@link com.github.io.protocol.core.BitBuffer} BitBuffer has a global byte buffer
     * to keep it, the default size is 512 bytes, if you need to transport much big data, you
     * need to give a buffer size bigger one.
     * @return The BitBuffer
     */
    public static ThreadLocal<BitBuffer> allocate() {
        return new ThreadLocal<>();
    }

    /**
     * The {@link com.github.io.protocol.core.BitBuffer} BitBuffer has a global byte buffer
     * to keep it, the default size is 512 bytes, if you need to transport much big data, you
     * need to give a buffer size bigger one.
     * @return The BitBuffer
     */
    public static ThreadLocal<BitBuffer> allocate(int bufferSize) {
        return new ThreadLocal<BitBuffer>() {
            @Override
            protected BitBuffer initialValue() {
                return new BitBuffer(bufferSize);
            }
        };
    }
}
