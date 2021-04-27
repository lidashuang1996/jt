package com.lidashuang.jt.actuator;

import com.lidashuang.jt.JttActuator;
import com.lidashuang.jt.JttContext;
import com.lidashuang.jt.JttMessage;
import com.lidashuang.jt.jtt808.Jtt808T2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 位置信息汇报应答
 * @author lidashuang
 * @version 1.0
 */
public class Jtt808T18Actuator implements JttActuator {

    private static final Logger LOGGER = LoggerFactory.getLogger(Jtt808T18Actuator.class);

    @Override
    public void execute(JttContext context, JttMessage message) {
        LOGGER.info("[ 接收到「位置信息汇报」] ==> " + message.toString());
        final Jtt808T2 jtt808T2 = new Jtt808T2(
                message.getHeader().getNumber(), message.getMid(), 0);
        context.sendMessage(jtt808T2);
        LOGGER.info("[ 应答到「位置信息汇报」 ] ==> " + jtt808T2);
    }

}
