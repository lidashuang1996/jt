package com.lidashuang.jt.jt808;

import com.lidashuang.jt.JtMessage;

import java.util.Arrays;

/**
 * JT 808 --> 默认处理
 * @author lidashuang
 * @version 1.0
 */
public class Jt808T0 extends JtMessage {

    public Jt808T0() { }

    public Jt808T0(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public int getType() {
        if (this.header == null) {
            return -1;
        } else {
            return this.header.getId();
        }
    }

    @Override
    public byte[] encode() {
        return this.bytes;
    }

    @Override
    public JtMessage decode(byte[] bytes) {
        this.bytes = bytes;
        this.header = new Header(this.bytes);
        return this;
    }

    @Override
    public String toString() {
        return "{"
                + "\"bytes\":"
                + Arrays.toString(this.bytes)
                + ",\"header\":"
                + this.header
                + "}";
    }
}
