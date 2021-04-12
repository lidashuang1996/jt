package com.lidashuang.jt.jt808;

import lombok.Getter;

/**
 * @author lidashuang
 * @version 1.0
 *
 * 数据格式
 * 数据压缩上报
 * 消息ID：0x0901
 * 表 94 数据压缩上报消息体数据格式
 * 起始字节  字段         数据类型    描述及要求
 * 0        压缩消息长度  DWORD
 * 4        压缩消息体               压缩消息体为需要压缩的消息经过 GZIP 压缩算法后的消息
 *
 */
public class Jt808T63 {
    /** 消息 ID：0x0901 */
    public static final int M_ID = 0x0901;
    public static final int M_LEN = 4;

    @Getter
    private final int len;

    @Getter
    private final byte[] content;

    public Jt808T63(byte[] bytes) {
        if (bytes != null && bytes.length >= M_LEN) {
            final byte[] contents = new byte[bytes.length - 4];
            System.arraycopy(bytes, 4, contents, 0, bytes.length - 4);
            this.len = bytes[0] + bytes[1] + bytes[2] + bytes[3];
            this.content = contents;
        } else {
            throw new RuntimeException("数据格式 --> 数据压缩上报 --> 长度出现问题！");
        }
    }

}
