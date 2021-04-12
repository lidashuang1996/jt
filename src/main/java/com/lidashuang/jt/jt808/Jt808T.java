package com.lidashuang.jt.jt808;

import com.lidashuang.jt.JtMessage;

/**
 * @author lidashuang
 * @version 1.0
 */
public class Jt808T extends JtMessage {

    public Jt808T(byte[] bytes) {
        super(bytes);
    }

    @Override
    public void decode() {
    }

    @Override
    public byte[] encode() {
        return bytes;
    }

}
