package com.lidashuang.jt.jt808;

import lombok.Getter;

/**
 * @author lidashuang
 * @version 1.0
 *
 * 数据格式
 * 终端鉴权
 * 消息ID：0x0102
 * 终端鉴权消息体数据格式
 * 起始字节  字段     数据类型    描述及要求
 * 0        鉴权码   STRING     终端重连后上报鉴权码
 */
public class Jt808T9 {
    /** 消息 ID：0x0102 */
    public static final int M_ID = 0x0102;
    public static final int M_LEN = 0;

    @Getter
    private final String a;

    public Jt808T9(byte[] bytes) {
        if (bytes != null) {
            this.a = new String(bytes);
        } else {
            throw new RuntimeException("数据格式 --> 终端鉴权 --> 长度出现问题！");
        }
    }

}
