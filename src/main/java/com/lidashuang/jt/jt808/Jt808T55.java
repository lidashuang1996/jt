package com.lidashuang.jt.jt808;

/**
 * @author lidashuang
 * @version 1.0
 *
 * 数据格式
 * 摄像头立即拍摄命令应答
 * 消息ID：0x0805
 * 摄像头立即拍摄命令应答消息体数据格式
 *
 * 该命令用于应答监控中心下发的摄像头立即拍摄命令0x8801。
 *
 * 起始字节  字段            数据类型    描述及要求
 * 0        应答流水号       WORD       对应平台摄像头立即拍摄命令的消息流水号 0：成功；1：失败；2：通道不支持。
 * 2        结果            BYTE       以下字段在结果=0 时才有效。
 * 3        多媒体 ID 个数   WORD       n，拍摄成功的多媒体个数
 * 4        多媒体 ID 列表   BYTE[4*n]
 */
public class Jt808T55 {
    /** 消息 ID：0x8805 */
    public static final int M_ID = 0x0805;
    public static final int M_LEN = 0;

    public Jt808T55(byte[] bytes) {
        if (bytes != null) {
            System.out.println(new String(bytes));
        } else {
            throw new RuntimeException("数据格式 --> 摄像头立即拍摄命令应答 --> 长度出现问题！");
        }
    }

}
