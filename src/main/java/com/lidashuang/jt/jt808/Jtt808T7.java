package com.lidashuang.jt.jt808;

import com.lidashuang.jt.message.JttMessage;

import java.util.Arrays;

/**
 * 数据格式
 *
 * 终端注销
 * 消息 ID：0x0003
 *
 * 终端注销消息体为空
 *
 * @author lidashuang
 * @version 1.0
 */
public class Jtt808T7 extends JttMessage {

    /** 消息 ID：0x0003 */
    public static final int M_ID = 0x0003;

    public Jtt808T7() {}

    public Jtt808T7(byte[] bytes) {
        this.bytes = bytes;
        if (this.bytes != null && this.bytes.length > 0) {
            LOGGER.warn("[ Jtt808T7 (0x0003) { 终端注销消息体为空 }] 消息内容理论为空，现在存在内容, 内容为 ==> " + Arrays.toString(this.bytes));
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
    public Jtt808T7 decode(byte[] bytes) {
        try {
            return new Jtt808T7(bytes);
        } catch (Exception e) {
            throw new RuntimeException("[ Jtt808T7 ] decode() ==> " + e);
        }
    }
}
