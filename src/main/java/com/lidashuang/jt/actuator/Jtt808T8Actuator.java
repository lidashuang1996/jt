package com.lidashuang.jt.actuator;

import com.lidashuang.jt.JttActuator;
import com.lidashuang.jt.JttContext;
import com.lidashuang.jt.JttMessage;
import com.lidashuang.jt.jtt808.Jtt808T8;
import com.lidashuang.jt.jtt808.Jt808T_1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lidashuang
 * @version 1.0
 */
public class Jtt808T8Actuator implements JttActuator {

    private static final Logger LOGGER = LoggerFactory.getLogger(Jtt808T8Actuator.class);

    @Override
    public void execute(JttContext context, JttMessage message) {
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

        Jtt808T8 jt808T8 = (Jtt808T8) message;

        final Jt808T_1 jt808T1 = new Jt808T_1(context.getAttribute("phone"), Integer.parseInt(index), Jtt808T8.M_ID, 0);
        LOGGER.info("[发送到消息] 注册 ==> " + jt808T1.toString());
        context.sendMessage(jt808T1);
    }

}
