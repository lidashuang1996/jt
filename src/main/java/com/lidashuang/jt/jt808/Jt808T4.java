package com.lidashuang.jt.jt808;

import lombok.Getter;

/**
 * @author lidashuang
 * @version 1.0
 *
 * 数据格式
 * 终端通用应答
 * 消息 ID：0x0001。
 * 起始字节     字段       数据类型   描述及要求
 * 0           应答流水号  WORD      对应的平台消息的流水号
 * 2           应答 ID    WORD      对应的平台消息的 ID
 * 4           结果       BYTE      0：成功/确认；1：失败；2：消息有误；3：不支持
 *
 *
 */
public class Jt808T4 {
    /** 消息 ID：0x0001 */
    public static final byte M_ID = 0x0001;
    public static final int M_LEN = 5;

    @Getter
    private final String a;

    @Getter
    private final String b;

    @Getter
    private final String c;

    public Jt808T4(byte[] bytes) {
        if (bytes != null && bytes.length == M_LEN) {
            a = String.valueOf(bytes[0]) + String.valueOf(bytes[1]);
            b = String.valueOf(bytes[2]) + String.valueOf(bytes[3]);
            c = String.valueOf(bytes[4]);
        } else {
            throw new RuntimeException("数据格式 --> 终端通用应答 --> 长度出现问题！");
        }
    }

}
