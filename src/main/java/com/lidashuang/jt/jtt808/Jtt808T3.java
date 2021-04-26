package com.lidashuang.jt.jtt808;

import com.lidashuang.jt.JttMessage;
import java.util.Arrays;

/**
 *
 * 数据格式
 *
 * 终端心跳
 * 消息 ID:0x0002
 *
 * 终端心跳数据消息体为空。
 *
 * @author lidashuang
 * @version 1.0
 */
public class Jtt808T3 extends JttMessage {

    /** 消息 ID */
    public final static int M_ID = 0x0002;

    public Jtt808T3() {}

    public Jtt808T3(byte[] bytes) {
        this.bytes = bytes;
        if (this.bytes != null && this.bytes.length > 0) {
            LOGGER.warn("[ Jtt808T3 (0x0002) { 平台通用应答 }] 消息内容理论为空，现在存在内容, 内容为 ==> " + Arrays.toString(this.bytes));
        }
    }

    @Override
    public int getMid() {
        return M_ID;
    }

    @Override
    @SuppressWarnings("all")
    public byte[] encode() {
        if (this.bytes != null && this.bytes.length > 0) {
            return bytes;
        } else {
            return new byte[0];
        }
    }

    @Override
    public Jtt808T3 decode(byte[] bytes) {
        try {
            return new Jtt808T3(bytes);
        } catch (Exception e) {
            throw new RuntimeException("[ Jtt808T3 ] decode() ==> " + e);
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
