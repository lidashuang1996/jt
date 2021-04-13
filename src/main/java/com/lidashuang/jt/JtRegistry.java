package com.lidashuang.jt;

import com.lidashuang.jt.jt808.Jt808T0;

import java.util.Hashtable;
import java.util.Map;

/**
 * @author lidashuang
 * @version 1.0
 */
public final class JtRegistry {

    /** 消息核心 */
    private static final Map<Integer, JtMessage> MESSAGE_CORE = new Hashtable<>();


    private static final Map<String, String> aa2 = new Hashtable<>();
    private static final Map<String, String> aa3 = new Hashtable<>();

    static {
        MESSAGE_CORE.put(null, new Jt808T0());
    }

    public static Map<Integer, JtMessage> getMessageCore() {
        return MESSAGE_CORE;
    }

    public static JtMessage getMessageCore(Integer key) {
        JtMessage message = MESSAGE_CORE.get(key);
        if (message == null) {
            message = MESSAGE_CORE.get(null);
        }
        return message;
    }



}
