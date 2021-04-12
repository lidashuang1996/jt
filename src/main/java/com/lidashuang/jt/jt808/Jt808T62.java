package com.lidashuang.jt.jt808;

/**
 * @author lidashuang
 * @version 1.0
 *
 * 数据格式
 * 数据上行透传
 *
 * 消息ID： 0x0900
 * 数据上行透传消息体数据格式
 * 起始字节  字段         数据类型     描述及要求
 * 0        透传消息类型  BYTE        透传消息类型定义见 表 93
 * 1        透传消息内容
 *
 * 透传消息类型               定义                  描述及要求
 * GNSS模块详细定位数据       0x00                  GNSS 模块详细定位数据
 * 道路运输证 IC 卡信息       0x0B                  道路运输证 IC 卡信息上传消息为 64Byte，下传消息为24Byte。道路运输证 IC 卡认证透传超时时间为 30s。超时后，不重发。
 * 串口 1 透传               0x41                  串口 1 透传消息
 * 串口 2 透传               0x42                  串口 2 透传消息
 * 用户自定义透传             0xF0-0xFF             用户自定义透传消息
 *
 */
public class Jt808T62 {
    /** 消息 ID：0x0900 */
    public static final int M_ID = 0x0900;
    public static final int M_LEN = 0;

    public Jt808T62(byte[] bytes) {
        if (bytes != null) {

        } else {
            throw new RuntimeException("数据格式 --> 数据上行透传 --> 长度出现问题！");
        }
    }

}
