package com.lidashuang.jt.jt808;

import com.lidashuang.jt.JtMessage;
import lombok.Getter;

/**
 * @author lidashuang
 * @version 1.0
 *
 * 数据格式
 * 平台通用应答
 * 消息 ID：0x8001
 * 平台通用应答消息体数据格式
 * 起始字节  字段         数据类型    描述及要求
 * 0        应答流水号    WORD       对应的终端消息的流水号
 * 2        应答 ID      WORD       对应的终端消息的 ID
 * 4        结果         BYTE       0：成功/确认；1：失败；2：消息有误；3：不支持；4：报警
 * 处理确认；
 *
 *
 */
public class Jt808T2 extends JtMessage {
    /** 消息 ID：0x8001 */
    public static final int M_ID = 0x8001;
    public static final int M_LEN = 5;

    /**
     * 构造输入消息源字节码数据
     *
     * @param bytes 消息源字节码数据
     */
    public Jt808T2(byte[] bytes) {
        super(bytes);
    }


    @Override
    public void decode() {

    }

    @Override
    public byte[] encode() {
        return new byte[0];
    }

}
