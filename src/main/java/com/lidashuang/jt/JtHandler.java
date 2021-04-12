package com.lidashuang.jt;

import com.lidashuang.jt.actuator.Jt808T5Actuator;
import com.lidashuang.jt.jt808.Jt808T0;
import com.lidashuang.jt.jt808.Jt808T5;
import com.lidashuang.jt.jt808.Jt808T6;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 处理器
 * @author lidashuang
 * @version 1.0
 */
public class JtHandler extends ChannelInboundHandlerAdapter {

    private final JtActuator jt808T5Actuator = new Jt808T5Actuator();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("X --> 0");
        if (msg instanceof Jt808T5) {
            System.out.println("X --> 1");
            jt808T5Actuator.execute(ctx, (JtMessage) msg);
            System.out.println("X --> 2");
        } else {
            System.out.println("----------------------------------------");
            System.out.println("sd JtHandler ---> " + msg + "  " + msg.getClass());
            ctx.channel().write(new Jt808T6("0", 0, 0, "000"));
            System.out.println("----------------------------------------");
        }
    }

}
