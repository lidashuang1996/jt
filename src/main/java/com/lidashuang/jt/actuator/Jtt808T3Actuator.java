package com.lidashuang.jt.actuator;

import com.lidashuang.jt.JttActuator;
import com.lidashuang.jt.JttContext;
import com.lidashuang.jt.JttMessage;
import com.lidashuang.jt.JttUtils;
import com.lidashuang.jt.jtt808.Jtt808T6;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lidashuang
 * @version 1.0
 */
public class Jtt808T3Actuator implements JttActuator {

    private static final Logger LOGGER = LoggerFactory.getLogger(Jtt808T3Actuator.class);

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

        String uuid = JttUtils.generateUUID();
        uuid = uuid.length() >= 16 ? uuid.substring(0, 16) : (uuid + "0000000000000000").substring(0, 16);
        final Jtt808T6 jt808T6 = new Jtt808T6(context.getAttribute("phone"), Integer.parseInt(index), 0, uuid);
        LOGGER.info("[发送到消息] 注册 ==> " + jt808T6.toString());
        context.sendMessage(jt808T6);
    }

}
