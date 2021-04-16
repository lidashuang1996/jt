package com.lidashuang.jt.actuator;

import com.lidashuang.jt.JtActuator;
import com.lidashuang.jt.JtContext;
import com.lidashuang.jt.JtMessage;
import com.lidashuang.jt.JtUtils;
import com.lidashuang.jt.jt808.Jt808T6;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lidashuang
 * @version 1.0
 */
public class Jt808T5Actuator implements JtActuator {

    private static final Logger LOGGER = LoggerFactory.getLogger(Jt808T5Actuator.class);

    @Override
    public void execute(JtContext context, JtMessage message) {
        // 注册
        LOGGER.info(
                "注册 ==> " + message.getHeader()
                + ", 实际需要缓存核对操作"
        );
        final Jt808T6 jt808T6 = new Jt808T6(message.getHeader().getNumber(), 0, JtUtils.generateUUID());
        LOGGER.info("注册返回的消息为 ==> " + jt808T6.toString());
        context.sendMessage(jt808T6);
    }

}
