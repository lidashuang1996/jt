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
public class JtContext {

    /** netty 的上下文 */
    private final ChannelHandlerContext context;

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
    public void sendMessage(JtMessage message) {
        if (context != null) {
            final Channel channel = context.channel();
            if (channel != null) {
                // 得到消息编码后的内容并发送出去
                channel.writeAndFlush(message);
            }
        }
    }

    public String getAttribute(String key) {
        return attribute.get(key);
    }

    public synchronized void delAttribute(String key) {
        attribute.remove(key);
    }

    public synchronized void setAttribute(String key, String value) {
        attribute.put(key, value);
    }
}
