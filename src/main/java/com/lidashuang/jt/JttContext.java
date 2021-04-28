package com.lidashuang.jt;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.Map;

/**
 * 上下文
 * @author lidashuang
 * @version 1.0
 */
public class JttContext {

    /** 消息流水号最大长度 */
    private final static int MAX_INDEX_SIZE = 1024;

    /** 消息序列号 */
    private int index = 0;

    /** 上下文 */
    private final ChannelHandlerContext context;

    /** 属性集合 */
    private final Map<String, Object> attribute = new HashMap<>();

    /**
     * 构造输入 netty 的上下文
     * @param context netty 上下文参数对象
     */
    public JttContext(ChannelHandlerContext context) {
        this.context = context;
    }

    /**
     * 推送消息对象
     * 不能同时推送消息，只能一条条发送消息
     * @param message 消息对象
     */
    public synchronized void sendMessage(JttMessage message) {
        if (context != null) {
            final Channel channel = context.channel();
            if (channel != null) {
                // 得到消息编码后的内容并发送出去
                final Object phone = getAttribute("__phone__");
                final Object encryption = getAttribute("__encryption__");
                message.setHeader(new JttMessageHeader(
                        encryption == null ? null : (int) encryption,
                        phone == null ? null : (String) phone,
                        getIncrIndex())
                );
                channel.writeAndFlush(message);
            }
        }
    }

    /**
     * 获取索引
     * @return 索引
     */
    public int getIndex() {
        return index;
    }

    /**
     * 获取自增后的索引
     * @return 索引
     */
    public synchronized int getIncrIndex() {
        index = index + 1;
        index = index >= MAX_INDEX_SIZE ? 0 : index;
        return index;
    }

    /**
     * 获取属性
     * @param key 属性的 KEY
     * @return 属性的 VALUE
     */
    public Object getAttribute(String key) {
        return attribute.get(key);
    }

    /**
     * 获取全部属性
     * @return 全部属性
     */
    public Map<String, Object> getAttribute() {
        return attribute;
    }

    /**
     * 删除属性
     * @param key 属性的 KEY
     */
    public synchronized void delAttribute(String key) {
        attribute.remove(key);
    }

    /**
     * 设置属性
     * @param key 属性的 KEY
     * @param value 属性的 VALUE
     */
    public synchronized void setAttribute(String key, Object value) {
        attribute.put(key, value);
    }
}
