package com.lidashuang.jt;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 处理器
 * @author lidashuang
 * @version 1.0
 */
public class JtHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object message) throws Exception {
        if (message instanceof JtMessage) {
            final JtMessage jtMessage = (JtMessage) message;
            final JtActuator jtActuator = JtRegistry.getActuatorCore(jtMessage.getType());
            if (jtActuator != null) {
                if (JtRegistry.ASYNC.equals(JtRegistry.getMode())) {
                    // 线程池中执行
                    JtRegistry.executeThreadPool(() -> jtActuator.execute(ctx, jtMessage));
                } else {
                    // 立即执行
                    jtActuator.execute(ctx, jtMessage);
                }
            }
        }

    }

}
