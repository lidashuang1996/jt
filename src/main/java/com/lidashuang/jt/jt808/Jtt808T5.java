package com.lidashuang.jt.jt808;

import com.lidashuang.jt.message.JttMessage;
import com.lidashuang.jt.JttUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
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
 * @author lidashuang
 * @version 1.0
 */
public class Jtt808T5 extends JttMessage {

    /** 消息 ID */
    public static final int M_ID = 0x0100;

    private int province;
    private int city;
    private byte[] manufacturer;
    private byte[] terminalModel;
    private byte[] terminalId;
    private int licensePlateColor;
    private String vehicleMark;

    public Jtt808T5() { }

    public Jtt808T5(int province, int city, byte[] manufacturer, byte[] terminalModel,
                    byte[] terminalId, int licensePlateColor, String vehicleMark) {
        this.province = province;
        this.city = city;
        this.manufacturer = manufacturer;
        this.terminalModel = terminalModel;
        this.terminalId = terminalId;
        this.licensePlateColor = licensePlateColor;
        this.vehicleMark = vehicleMark;
    }

    public Jtt808T5(int province, int city, byte[] manufacturer, byte[] terminalModel,
                    byte[] terminalId, int licensePlateColor, String vehicleMark, byte[] bytes) {
        this.province = province;
        this.city = city;
        this.manufacturer = manufacturer;
        this.terminalModel = terminalModel;
        this.terminalId = terminalId;
        this.licensePlateColor = licensePlateColor;
        this.vehicleMark = vehicleMark;
        this.bytes = bytes;
    }

    @Override
    public int getMid() {
        return M_ID;
    }

    @Override
    @SuppressWarnings("all")
    public byte[] encode() {
        ByteArrayOutputStream outputStream = null;
        try {
            outputStream = new ByteArrayOutputStream();
            outputStream.write(JttUtils.integerToHigh8Low8(province));
            outputStream.write(JttUtils.integerToHigh8Low8(city));
            outputStream.write(manufacturer);
            outputStream.write(terminalModel);
            outputStream.write(terminalId);
            outputStream.write((byte) licensePlateColor);
            outputStream.write(vehicleMark.getBytes("GBK"));
            this.bytes = outputStream.toByteArray();
            return this.bytes;
        } catch (Exception e) {
            throw new RuntimeException("[ Jtt808T5 ] encode() ==> " + e);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public Jtt808T5 decode(byte[] bytes) {
        try {
            return new Jtt808T5(
                    JttUtils.bytesToHigh8Low8(JttUtils.bytesArrayIntercept(bytes, 0, 2)),
                    JttUtils.bytesToHigh8Low8(JttUtils.bytesArrayIntercept(bytes, 2, 2)),
                    JttUtils.bytesArrayIntercept(bytes, 4, 5),
                    JttUtils.bytesArrayIntercept(bytes, 9, 20),
                    JttUtils.bytesArrayIntercept(bytes, 29, 7),
                    bytes[36],
                    new String(JttUtils.bytesArrayIntercept(bytes, 37, bytes.length - 37), "GBK"),
                    bytes
            );
        } catch (Exception e) {
            // 抛出异常，终止执行
            throw new RuntimeException("[ Jtt808T5 ] decode() ==> " + e);
        }
    }

    @Override
    public String toString() {
        return "{"
                + "\"province\":"
                + province
                + ",\"city\":"
                + city
                + ",\"manufacturer\":\""
                + Arrays.toString(manufacturer) + '\"'
                + ",\"terminalModel\":\""
                + Arrays.toString(terminalModel) + '\"'
                + ",\"terminalId\":\""
                + Arrays.toString(terminalId) + '\"'
                + ",\"licensePlateColor\":"
                + licensePlateColor
                + ",\"vehicleMark\":\""
                + vehicleMark + '\"'
                + ",\"bytes\":"
                + Arrays.toString(bytes)
                + "}";
    }

    public int getProvince() {
        return province;
    }

    public int getCity() {
        return city;
    }

    public byte[] getManufacturer() {
        return manufacturer;
    }

    public byte[] getTerminalModel() {
        return terminalModel;
    }

    public byte[] getTerminalId() {
        return terminalId;
    }

    public int getLicensePlateColor() {
        return licensePlateColor;
    }

    public String getVehicleMark() {
        return vehicleMark;
    }
}
