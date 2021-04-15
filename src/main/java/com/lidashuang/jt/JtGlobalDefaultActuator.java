package com.lidashuang.jt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 全局默认处理器
 * @author lidashuang
 * @version 1.0
 */
public class JtGlobalDefaultActuator implements JtActuator {

    /**
     * 注入日志对象
     */
    private static final Logger LOGGER =
            LoggerFactory.getLogger(JtGlobalDefaultActuator.class);

    @Override
    public void execute(JtContext context, JtMessage message) {
        LOGGER.info("全局默认处理器收到的消息 ==> " + message.toString());
    }

}
