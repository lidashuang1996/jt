package com.lidashuang.jt;

import com.lidashuang.jt.message.JttMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.Map;

/**
 * 上下文
 * @author lidashuang
 * @version 1.0
 */
public class JtContext {

    /** 上下文 */
    private final ChannelHandlerContext context;

    /** 属性集合 */
    private final Map<String, String> attribute = new HashMap<>();

    /**
     * 构造输入 netty 的上下文
     * @param context netty 上下文参数对象
     */
    public JtContext(ChannelHandlerContext context) {
        this.context = context;
    }

    /**
     * 推送消息对象
     * @param message 消息对象
     */
    public void sendMessage(JttMessage message) {
        if (context != null) {
            final Channel channel = context.channel();
            if (channel != null) {
                // 得到消息编码后的内容并发送出去
                channel.writeAndFlush(message);
            }
        }
    }

    /**
     * 获取属性
     * @param key 属性的 KEY
     * @return 属性的 VALUE
     */
    public String getAttribute(String key) {
        return attribute.get(key);
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
    public synchronized void setAttribute(String key, String value) {
        attribute.put(key, value);
    }
}
