package com.lidashuang.jt.actuator;

import com.lidashuang.jt.JttActuator;
import com.lidashuang.jt.JttContext;
import com.lidashuang.jt.JttMessage;
import com.lidashuang.jt.JttUtils;
import com.lidashuang.jt.jtt808.Jtt808T6;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 终端注册的应答
 * @author lidashuang
 * @version 1.0
 */
public class Jtt808T5Actuator implements JttActuator {

    private static final Logger LOGGER = LoggerFactory.getLogger(Jtt808T5Actuator.class);

    @Override
    public void execute(JttContext context, JttMessage message) {
        LOGGER.info("[ 接收到「注册」] ==> " + message.toString());
        final Jtt808T6 jtt808T6 = new Jtt808T6(
                message.getHeader().getNumber(), 0, JttUtils.generateUuid(12));
        context.sendMessage(jtt808T6);
        LOGGER.info("[ 应答到「注册」 ] ==> " + jtt808T6);
    }

}
