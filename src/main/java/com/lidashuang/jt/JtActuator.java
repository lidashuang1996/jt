package com.lidashuang.jt;

/**
 * 处理器
 * @author lidashuang
 * @version 1.0
 */
public interface JtActuator {

    /**
     * 收到消息后执行的方法
     * @param context Netty 上下文对象
     * @param message Jt 消息对象
     */
    public void execute(JtContext context, JtMessage message);

}
