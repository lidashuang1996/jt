package com.lidashuang.jt.jt808;

import com.lidashuang.jt.JtMessage;
import com.lidashuang.jt.Utils;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * @author lidashuang
 * @version 1.0
 *
 * 数据格式
 *
 * 终端注册
 * 消息ID：0x0100
 *
 * 终端注册消息体数据格式
 * 起始字节     字段      数据类型        描述及要求
 * 0           省域 ID   WORD           标示终端安装车辆所在的省域，0 保留，由平台取默认值。省域 ID 采用 GB/T 2260 中规定的行政区划代码六位中前两位。
 * 2           市县域 ID WORD           标示终端安装车辆所在的市域和县域，0 保留，由平台取默认值。市县域 ID 采用 GB/T 2260 中规定的行政区划代码六位中后四位。
 * 4           制造商 ID BYTE[5]        5 个字节，终端制造商编码
 * 9           终端型号  BYTE[20]       20 个字节，此终端型号由制造商自行定义，位数不足时，后补“0X00”。
 * 29          终端 ID   BYTE[7]        7 个字节，由大写字母和数字组成，此终端 ID 由制造商自行定义，位数不足时，后补“0X00”。
 * 36          车牌颜色  BYTE           车牌颜色，按照 JT/T415-2006 的 5.4.12。未上牌时，取值为 0。
 * 37          车辆标识  STRING         车牌颜色为 0 时，表示车辆 VIN；否则，表示公安交通管理部门颁发的机动车号牌。
 *
 */
public class Jt808T5 extends JtMessage {
    /** 消息 ID：0x0100 */
    public static final int M_ID = 0x0100;
    public static final int M_LEN = 37;

    private int province;
    private int city;

    private String manufacturer;
    private String terminalModel;
    private String terminalId;

    private int licensePlateColor;
    private String vehicleMark;


    public Jt808T5(byte[] bytes) {
        super(bytes);
        if (bytes != null) {
            System.out.println("终端注册 Jt808T5 头部 ===> " + getHeadMessage());
            System.out.println("终端注册 Jt808T5 内容 ===> " + Arrays.toString(bytes));
            System.out.println("终端注册 Jt808T5 数据 ===> " + this.toString());
        } else {
            throw new RuntimeException("数据格式 --> 终端注册 Jt808T5 --> 长度出现问题！");
        }
    }

    @Override
    public void decode() {
        final int contentLength = headMessage.getContentLength();
        final byte[] content = new byte[contentLength];
        System.arraycopy(bytes, headMessage.getHeadLength(), content, 0, contentLength);
        try {
            this.province = Utils.bytesToHigh8Low8(Utils.bytesArrayIntercept(content, 0, 2));
            this.city = Utils.bytesToHigh8Low8(Utils.bytesArrayIntercept(content, 2, 2));
            this.manufacturer = Utils.bytesToHex(Utils.bytesArrayIntercept(content, 4, 5));
            this.terminalModel = Utils.bytesToHex(Utils.bytesArrayIntercept(content, 9, 20));
            this.terminalId = new String(Utils.bytesArrayIntercept(content, 29, 7));
            this.licensePlateColor = content[36];
            this.vehicleMark = new String(Utils.bytesArrayIntercept(content, 37, content.length - 37), "GBK");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] encode() {
        return new byte[0];
    }

    @Override
    public String toString() {
        return "{"
                + "\"province\":"
                + province
                + ",\"city\":"
                + city
                + ",\"manufacturer\":\""
                + manufacturer + '\"'
                + ",\"terminalModel\":\""
                + terminalModel + '\"'
                + ",\"terminalId\":\""
                + terminalId + '\"'
                + ",\"licensePlateColor\":"
                + licensePlateColor
                + ",\"vehicleMark\":\""
                + vehicleMark + '\"'
                + "}";
    }
}
