package com.lidashuang.jt.jt808;

import lombok.Getter;

/**
 * @author lidashuang
 * @version 1.0
 *
 * 数据格式
 * 查询指定终端参数
 * 消息ID：0x8106
 * 查询指定终端参数消息体数据格式
 * 起始字节  字段            数据类型     描述及要求
 * 0        参数总数        BYTE         参数总数为 n
 * 1        参数 ID 列表    BYTE[4*n]    参数顺序排列，如“参数 ID1 参数 ID2......参数IDn”
 */
public class Jt808T11 {
    /** 消息 ID：0x0102 */
    public static final int M_ID = 0x8106;
    public static final int M_LEN = 1;
    public static final int M_ITEM_LEN = 4;

    @Getter
    private final String a;

    @Getter
    private final String[] b;

    public Jt808T11(byte[] bytes) {
        if (bytes != null && bytes.length >= M_LEN
                && (bytes.length - M_LEN) % M_ITEM_LEN == 0) {
            this.a = String.valueOf(bytes[0]);
            final String[] bs = new String[(bytes.length - M_LEN) % M_ITEM_LEN];
            for (int i = 1; i < bytes.length;) {
                int index = (i - 1) / 4;
                bs[index] = String.valueOf(bytes[1 + index * 4])
                        + String.valueOf(bytes[2 + index * 4])
                        + String.valueOf(bytes[3 + index * 4])
                        + String.valueOf(bytes[4 + index * 4]);
                i = i + 4;
            }
            this.b = bs;
        } else {
            throw new RuntimeException("数据格式 --> 查询指定终端参数 --> 长度出现问题！");
        }
    }

}
