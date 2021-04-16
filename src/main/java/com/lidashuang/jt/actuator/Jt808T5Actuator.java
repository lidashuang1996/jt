package com.lidashuang.jt.actuator;

import com.lidashuang.jt.JtActuator;
import com.lidashuang.jt.JtContext;
import com.lidashuang.jt.JtMessage;
import com.lidashuang.jt.jt808.Jt808T0;

/**
 * @author lidashuang
 * @version 1.0
 */
public class Jt808T5Actuator implements JtActuator {

    @Override
    public void execute(JtContext context, JtMessage message) {
        System.out.println("execute ==> " + message);
        context.sendMessage(new Jt808T0(new byte[] {
                0, 1, 2, 3, 4
        }));
    }

}
