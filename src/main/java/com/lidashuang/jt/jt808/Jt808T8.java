package com.lidashuang.jt.jt808;

import com.lidashuang.jt.JtMessage;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

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
public class Jt808T8 extends JtMessage {
    /** 消息 ID：0x0102 */
    public static final int M_ID = 0x0102;

    private String authCode;

    public Jt808T8(byte[] bytes) {
        super(bytes);
        if (bytes != null) {
            System.out.println("终端注册 Jt808T8 头部 ===> " + getHeadMessage());
            System.out.println("终端注册 Jt808T8 内容 ===> " + Arrays.toString(bytes));
            System.out.println("终端注册 Jt808T8 数据 ===> " + this.toString());
        } else {
            throw new RuntimeException("数据格式 --> 终端鉴权 Jt808T8 --> 长度出现问题！");
        }
    }

    @Override
    public void decode() {
        final int contentLength = headMessage.getContentLength();
        final byte[] content = new byte[contentLength];
        System.arraycopy(bytes, headMessage.getHeadLength(), content, 0, contentLength);
        try {
            this.authCode = new String(content, "GBK");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] encode() {
        return new byte[0];
    }

    @Override
    public String toString() {
        return "{"
                + "\"authCode\":\""
                + authCode + '\"'
                + "}";
    }
}
