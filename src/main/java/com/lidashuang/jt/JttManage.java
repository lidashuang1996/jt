package com.lidashuang.jt;

import java.util.ArrayList;
import java.util.List;

/**
 * jtt 管理对象
 * @author lidashuang
 * @version 1.0
 */
public final class JttManage {

    private final static List<JttContext> CONTEXT_LIST = new ArrayList<>();

    public static void setContextList(JttContext context) {
        CONTEXT_LIST.add(context);
    }

    public static List<JttContext> getContextList() {
        return CONTEXT_LIST;
    }

}
