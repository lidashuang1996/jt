package com.lidashuang.jt.jt808;

import lombok.Getter;

/**
 * @author lidashuang
 * @version 1.0
 *
 * 数据格式
 * 查询终端参数应答
 * 消息ID：0x0104。
 * 查询终端参数应答消息体数据格式
 * 起始字节  字段         数据类型    描述及要求
 * 0        应答流水号    WORD       对应的终端参数查询消息的流水号
 * 2        应答参数个数  BYTE
 * 3        参数项列表               参数项格式
 *
 * ======= 参数项格式 =======
 * 起始字节  字段       数据类型 描述及要求
 * 0        参数总数    BYTE
 * 1        参数项列表    参数项格式见 表 11
 */
public class Jt808T12 {
    /** 消息 ID：0x0104 */
    public static final int M_ID = 0x0104;
    public static final int M_LEN = 1;
    public static final int M_ITEM_LEN = 4;

    @Getter
    private final String a;

    @Getter
    private final String[] b;

    public Jt808T12(byte[] bytes) {
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
            throw new RuntimeException("数据格式 --> 查询终端参数应答 --> 长度出现问题！");
        }
    }

}
