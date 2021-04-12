package com.lidashuang.jt.jt808;

import lombok.Getter;

/**
 * @author lidashuang
 * @version 1.0
 *
 * 数据格式
 * 单条存储多媒体数据检索上传命令
 * 消息ID：0x8805
 * 单条存储多媒体数据检索上传命令消息体数据格式
 * 起始字节  字段         数据类型    描述及要求
 * 0        多媒体 ID    DWORD      >0
 * 4        删除标志     BYTE       0：保留；1：删除；
 */
public class Jt808T60 {
    /** 消息 ID：0x8805 */
    public static final int M_ID = 0x8805;
    public static final int M_LEN = 0;

    public Jt808T60(byte[] bytes) {
        if (bytes != null) {

        } else {
            throw new RuntimeException("数据格式 --> 单条存储多媒体数据检索上传命令 --> 长度出现问题！");
        }
    }

}
