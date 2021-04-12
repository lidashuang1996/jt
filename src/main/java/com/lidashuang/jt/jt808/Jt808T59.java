package com.lidashuang.jt.jt808;

import lombok.Getter;

/**
 * @author lidashuang
 * @version 1.0
 *
 * 数据格式
 * 录音开始命令
 * 消息ID：0x8804
 * 录音开始命令消息体数据格式
 * 起始字节  字段       数据类型      描述及要求
 * 0        录音命令    BYTE        0：停止录音；0x01：开始录音；
 * 1        录音时间    WORD        单位为秒（s），0 表示一直录音
 * 3        保存标志    BYTE        0：实时上传；1：保存
 * 4        音频采样率  BYTE        0：8K；1：11K；2：23K；3：32K；其他保留
 *
 */
public class Jt808T59 {
    /** 消息 ID：0x8804 */
    public static final int M_ID = 0x8804;
    public static final int M_LEN = 5;

    @Getter
    private final String a;
    @Getter
    private final String b;
    @Getter
    private final String c;
    @Getter
    private final String d;

    public Jt808T59(byte[] bytes) {
        if (bytes != null && bytes.length == M_LEN) {
            this.a = String.valueOf(bytes[0]);
            this.b = String.valueOf(bytes[1]) + String.valueOf(bytes[2]);
            this.c = String.valueOf(bytes[3]);
            this.d = String.valueOf(bytes[4]);
        } else {
            throw new RuntimeException("数据格式 --> 录音开始命令 --> 长度出现问题！");
        }
    }

}
