package com.lidashuang.jt.jt808;

import com.lidashuang.jt.JtMessage;

/**
 * @author lidashuang
 * @version 1.0
 *
 * 数据格式
 * 终端通用应答
 * 消息 ID：0x0001。
 * 起始字节     字段       数据类型   描述及要求
 * 0           应答流水号  WORD      对应的平台消息的流水号
 * 2           应答 ID    WORD      对应的平台消息的 ID
 * 4           结果       BYTE      0：成功/确认；1：失败；2：消息有误；3：不支持
 *
 *
 */
public class Jt808T1  {
    /** 消息 ID：0x0001 */
    public static final int M_ID = 0x0001;
    public static final int M_LEN = 5;


}
