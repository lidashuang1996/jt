package com.lidashuang.jt.jtt808;

import com.lidashuang.jt.JttMessage;
import java.util.Arrays;

/**
 * 数据格式
 *
 * 终端鉴权
 * 消息ID：0x0102
 *
 * 终端鉴权消息体数据格式
 * 起始字节  字段     数据类型    描述及要求
 * 0        鉴权码   STRING     终端重连后上报鉴权码
 *
 * @author lidashuang
 * @version 1.0
 */
public class Jtt808T8 extends JttMessage {

    /** 消息 ID：0x0102 */
    public static final int M_ID = 0x0102;

    /** 鉴权码 */
    private String authCode;

    public Jtt808T8() { }

    public Jtt808T8(String authCode) {
        this.authCode = authCode;
    }

    public Jtt808T8(String authCode, byte[] bytes) {
        this.authCode = authCode;
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
            this.bytes = authCode.getBytes("GBK");
            return this.bytes;
        } catch (Exception e) {
            throw new RuntimeException("[ Jtt808T8 ] encode() ==> " + e);
        }
    }

    @Override
    public Jtt808T8 decode(byte[] bytes) {
        try {
            return new Jtt808T8(new String(bytes, "GBK"), bytes);
        } catch (Exception e) {
            throw new RuntimeException("[ Jtt808T8 ] decode() ==> " + e);
        }
    }

    @Override
    public String toString() {
        return "{"
                + "\"authCode\":\""
                + authCode + '\"'
                + ",\"bytes\":"
                + Arrays.toString(bytes)
                + "}";
    }

    public String getAuthCode() {
        return authCode;
    }
}
