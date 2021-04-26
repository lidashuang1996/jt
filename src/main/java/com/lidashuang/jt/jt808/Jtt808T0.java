package com.lidashuang.jt.jt808;

import com.lidashuang.jt.message.JttMessage;
import java.util.Arrays;

/**
 * 默认「字节码」的消息处理
 *
 * @author lidashuang
 * @version 1.0
 */
public class Jtt808T0 extends JttMessage {

    /** 消息 ID */
    public final static int M_ID = 0x0000;

    public Jtt808T0() {}

    public Jtt808T0(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public int getMid() {
        return M_ID;
    }

    @Override
    @SuppressWarnings("all")
    public byte[] encode() {
        try {
            return this.bytes;
        } catch (Exception e) {
            throw new RuntimeException("[ Jtt808T0 ] encode() ==> " + e);
        }
    }

    @Override
    public Jtt808T0 decode(byte[] bytes) {
        try {
            return new Jtt808T0(bytes);
        } catch (Exception e) {
            throw new RuntimeException("[ Jtt808T0 ] decode() ==> " + e);
        }
    }

    @Override
    public String toString() {
        return "{"
                + "\"bytes\":"
                + Arrays.toString(bytes)
                + "}";
    }


}
