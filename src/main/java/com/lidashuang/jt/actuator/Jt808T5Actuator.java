package com.lidashuang.jt.actuator;

import com.lidashuang.jt.JtActuator;
import com.lidashuang.jt.JtEncoder;
import com.lidashuang.jt.JtMessage;
import com.lidashuang.jt.jt808.Jt808T0;
import com.lidashuang.jt.jt808.Jt808T6;
import io.netty.channel.ChannelHandlerContext;

import java.util.UUID;

/**
 * @author lidashuang
 * @version 1.0
 */
public class Jt808T5Actuator implements JtActuator {
    @Override
    public void execute(ChannelHandlerContext context, JtMessage message) throws Exception {
        final JtMessage.HeadMessage headMessage = message.getHeadMessage();
        final int number = headMessage.getNumber();
        final int result = 0;
        final String authCode = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
        context.pipeline().writeAndFlush(new Jt808T0(new byte[] { 1, 2, 3}));
    }
}
