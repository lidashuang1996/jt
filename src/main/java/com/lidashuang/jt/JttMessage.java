package com.lidashuang.jt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JTT 消息的对象
 * @author lidashuang
 * @version 1.0
 */
public abstract class JttMessage implements JttMessageCodec {

    /**
     * 消息的勾子函数
     */
    public interface Hook {

        /**
         * 勾子的回调执行
         * @param encryption 加密方式
         * @param content 内容
         * @return 内容处理后的结果
         */
        public byte[] execute(int encryption, byte[] content);
    }

    /** 解密之前的勾子函数 */
    public static Hook beforeDecoderHook = (encryption, content) -> content;

    /** 编码之后的勾子函数 */
    public static Hook afterEncoderHook = (encryption, content) -> content;

    /** 日志对象 */
    protected static final Logger LOGGER = LoggerFactory.getLogger(JttMessage.class);

    /** 消息源字节码数据 */
    protected byte[] bytes;

    /** 消息头部信息 */
    protected JttMessageHeader header;

    /**
     * 获取消息内容
     * @return 消息内容的字节码
     */
    public byte[] getBytes() {
        return bytes;
    }

    /**
     * 读取消息头部对象
     * @return 消息头部对象
     */
    public JttMessageHeader getHeader() {
        return header;
    }

    /**
     * 写入消息头部对象
     * @param header 消息头部对象
     */
    public void setHeader(JttMessageHeader header) {
        this.header = header;
    }

    /**
     * 获取消息类型
     * @return 消息类型
     */
    public abstract int getMid();
}
