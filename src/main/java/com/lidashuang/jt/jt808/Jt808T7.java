package com.lidashuang.jt.jt808;

import com.lidashuang.jt.JtMessage;

import java.util.Arrays;

/**
 * @author lidashuang
 * @version 1.0
 *
 * 数据格式
 * 终端注销
 * 消息 ID：0x0003
 * 终端注销消息体为空
 */
public class Jt808T7 extends JtMessage {
    /** 消息 ID：0x0003 */
    public static final int M_ID = 0x0003;
    public static final int M_LEN = 0;

    public Jt808T7(byte[] bytes) {
        super(bytes);
        if (bytes != null) {
            System.out.println("终端注册 Jt808T7 头部 ===> " + getHeadMessage());
            System.out.println("终端注册 Jt808T7 内容 ===> " + Arrays.toString(bytes));
            System.out.println("终端注册 Jt808T7 数据 ===> " + this.toString());
        } else {
            throw new RuntimeException("数据格式 --> 终端注销 Jt808T7 --> 长度出现问题！");
        }
    }

    @Override
    public void decode() {

    }

    @Override
    public byte[] encode() {
        return new byte[0];
    }

}
