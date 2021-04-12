package com.lidashuang.jt.jt808;

/**
 * @author lidashuang
 * @version 1.0
 *
 * 数据格式
 * 存储多媒体数据检索
 * 消息ID：0x8802
 * 注：不按时间范围则将起始时间/结束时间都设为00-00-00-00-00-00。
 * 起始字节  字段         数据类型    描述及要求
 * 0        多媒体类型     BYTE      0：图像；1：音频；2：视频；
 * 1        通道 ID       BYTE      0 表示检索该媒体类型的所有通道；
 * 2        事件项编码     BYTE      0：平台下发指令；1：定时动作；2：抢劫报警触发；3：碰撞侧翻报警触发；其他保留
 * 3        起始时间       BCD[6]    YY-MM-DD-hh-mm-ss
 * 9        结束时间       BCD[6]    YY-MM-DD-hh-mm-ss
 */
public class Jt808T57 {
    /** 消息 ID：0x8802 */
    public static final int M_ID = 0x8802;
    public static final int M_LEN = 0;

    public Jt808T57(byte[] bytes) {
        if (bytes != null) {
            System.out.println(new String(bytes));
        } else {
            throw new RuntimeException("数据格式 --> 存储多媒体数据检索 --> 长度出现问题！");
        }
    }

}
