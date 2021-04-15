package com.lidashuang.jt;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 处理器
 * @author lidashuang
 * @version 1.0
 */
public class JtHandler extends ChannelInboundHandlerAdapter {

    /**
     * 每次收到消息处理的勾子函数
     * @param context 上下文对象
     * @param message 收到的消息内容
     */
    @Override
    public void channelRead(ChannelHandlerContext context, Object message) {
        if (message instanceof JtMessage) {
            final JtMessage jtMessage = (JtMessage) message;
            final JtActuator jtActuator = JtRegistry.getActuatorCore(jtMessage.getType());
            if (jtActuator != null) {
                if (JtRegistry.ASYNC.equals(JtRegistry.getMode())) {
                    // 线程池中执行
                    JtRegistry.executeThreadPool(() -> jtActuator.execute(context, jtMessage));
                } else {
                    // 立即执行
                    jtActuator.execute(context, jtMessage);
                }
            }
        }

    }

}
