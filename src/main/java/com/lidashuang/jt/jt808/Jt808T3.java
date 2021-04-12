package com.lidashuang.jt.jt808;

import lombok.Getter;

/**
 * @author lidashuang
 * @version 1.0
 *
 * 数据格式
 * 终端心跳
 * 消息 ID：0x0002
 * 终端心跳数据消息体为空。
 *
 */
public class Jt808T3 {
    /** 消息 ID：0x0002 */
    public static final int M_ID = 0x0002;
    public static final int M_LEN = 0;

    public Jt808T3(byte[] bytes) {
        if (bytes != null && bytes.length == M_LEN) {

        } else {
            throw new RuntimeException("数据格式 --> 终端通用应答 --> 长度出现问题！");
        }
    }

}
