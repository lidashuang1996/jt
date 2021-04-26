package com.lidashuang.jt;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 处理器
 * @author lidashuang
 * @version 1.0
 */
public class JttHandler extends ChannelInboundHandlerAdapter {

    /**
     * 一个连接只有一个上下文对象
     */
    private JttContext jttContext;

    /**
     * 每次收到消息处理的勾子函数
     * @param context 上下文对象
     * @param message 收到的消息内容
     */
    @Override
    public void channelRead(ChannelHandlerContext context, Object message) {
        if (message instanceof JttMessage) {
            // 转换消息对象
            final JttMessage jttMessage = (JttMessage) message;
            // 读取处理这条消息的处理器
            final JttActuator jttActuator = JttRegistry.getActuatorCore(jttMessage.getMid());
            if (jttActuator != null) {
                // 如果不存在上下文对象，就创建一个上下文对象
                if (jttContext == null) {
                    // 创建并初始化手机号码
                    jttContext = new JttContext(context);
                    jttContext.setAttribute("__phone__", jttMessage.getHeader().getPhone());
                    jttContext.setAttribute("__encryption__", jttMessage.getHeader().getEncryption());
                }
                // 两种执行，执行器的方法
                if (JttRegistry.ASYNC.equals(JttRegistry.getMode())) {
                    // 线程池中执行
                    JttRegistry.executeThreadPool(() -> jttActuator.execute(jttContext, jttMessage));
                } else {
                    // 立即执行
                    jttActuator.execute(jttContext, jttMessage);
                }
            }
        }
    }
}
