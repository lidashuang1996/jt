package com.lidashuang.jt;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author lidashuang
 * @version 1.0
 */
public interface JtActuator {

    public void execute(ChannelHandlerContext context, JtMessage message) throws Exception;

}
