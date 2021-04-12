package com.lidashuang.jt.jt808;

import lombok.Getter;

/**
 * @author lidashuang
 * @version 1.0
 *
 * 数据格式
 * 查询终端参数
 * 消息 ID：0x8104
 * 查询终端参数消息体为空
 */
public class Jt808T10 {
    /** 消息 ID：0x8104 */
    public static final int M_ID = 0x8104;
    public static final int M_LEN = 0;

    public Jt808T10(byte[] bytes) {
        if (bytes != null) {
        } else {
            throw new RuntimeException("数据格式 --> 查询终端参数 --> 长度出现问题！");
        }
    }

}
