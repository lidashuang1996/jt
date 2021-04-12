package com.lidashuang.jt.jt808;

import lombok.Getter;

/**
 * @author lidashuang
 * @version 1.0
 *
 * 数据格式
 * 终端 RSA 公钥
 * 消息ID：0x0A00
 * 终端RSA公钥消息体数据格式
 * 起始字节  字段     数据类型     描述及要求
 * 0        e       DWORD(4 * 8)    终端 RSA 公钥{e,n}中的 e
 * 4        n       BYTE[128]    RSA 公钥{e,n}中的 n
 *
 */
public class Jt808T65 {
    /** 消息 ID：0x0A00 */
    public static final int M_ID = 0x0A00;
    public static final int M_LEN = 4 + 128;

    @Getter
    private final String e;

    @Getter
    private final byte[] n;

    public Jt808T65(byte[] bytes) {
        if (bytes != null && bytes.length == M_LEN) {
            final byte[] es = new byte[4];
            final byte[] ns = new byte[128];
            System.arraycopy(bytes, 0, es, 0, 4);
            System.arraycopy(bytes, 4, ns, 0, 128);
            this.e = new String(es);
            this.n = ns;
        } else {
            throw new RuntimeException("数据格式 --> 终端 RSA 公钥 --> 长度出现问题！");
        }
    }

}
