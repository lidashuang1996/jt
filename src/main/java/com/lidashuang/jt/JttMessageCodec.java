package com.lidashuang.jt;

/**
 * JTT 消息的编码和解码
 * @author lidashuang
 * @version 1.0
 */
public interface JttMessageCodec {

    /**
     * 编码
     * @return 编码后内容的字节码
     */
    public byte[] encode();

    /**
     * 解码
     * @param bytes 解码的参数
     * @return 解码后的消息对象
     */
    public JttMessage decode(byte[] bytes);

}
