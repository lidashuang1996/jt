package com.lidashuang.jt.actuator;

import com.lidashuang.jt.JtActuator;
import com.lidashuang.jt.JtContext;
import com.lidashuang.jt.JtMessage;
import com.lidashuang.jt.JtUtils;
import com.lidashuang.jt.jt808.Jt808T6;
import com.lidashuang.jt.jt808.Jt808T8;
import com.lidashuang.jt.jt808.Jt808T_1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lidashuang
 * @version 1.0
 */
public class Jt808T8Actuator implements JtActuator {

    private static final Logger LOGGER = LoggerFactory.getLogger(Jt808T8Actuator.class);

    @Override
    public void execute(JtContext context, JtMessage message) {
        // 注册
        LOGGER.info(
                "[接受到消息] 注册 ==> " + message.toString()
                + ", 实际需要缓存核对操作"
        );
        String index = context.getAttribute("index");
        if (index == null) {
            index = "0";
        }
        context.setAttribute("index", String.valueOf(Integer.parseInt(index) + 1));

        Jt808T8 jt808T8 = (Jt808T8) message;

        final Jt808T_1 jt808T1 = new Jt808T_1(context.getAttribute("phone"), Integer.parseInt(index), Jt808T8.M_ID, 0);
        LOGGER.info("[发送到消息] 注册 ==> " + jt808T1.toString());
        context.sendMessage(jt808T1);
    }

}
