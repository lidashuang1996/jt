package com.lidashuang.jt.actuator;

import com.lidashuang.jt.JttActuator;
import com.lidashuang.jt.JttContext;
import com.lidashuang.jt.JttMessage;
import com.lidashuang.jt.jtt808.Jtt808T2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 全局默认处理器
 * @author lidashuang
 * @version 1.0
 */
public class JttGlobalActuator implements JttActuator {

    /**
     * 注入日志对象
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(JttGlobalActuator.class);

    @Override
    public void execute(JttContext context, JttMessage message) {
        LOGGER.info("[ 全局默认处理器收到的消息 ] ==> " + message.toString());
        final Jtt808T2 jtt808T2 = new Jtt808T2(
                message.getHeader().getNumber(), message.getMid(), 0);
        context.sendMessage(jtt808T2);
        LOGGER.info("[ 全局默认处理器返回通用应答的消息 ] ==> " + jtt808T2);
    }

}
