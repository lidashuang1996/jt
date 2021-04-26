package com.lidashuang.jt.jtt808;

import com.lidashuang.jt.JttUtils;
import com.lidashuang.jt.JttMessage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * 数据格式
 *
 * 位置信息汇报
 * 消息ID：0x0200
 *
 * 位置信息汇报消息体由位置基本信息和位置附加信息项列表组成
 * +--------------------+--------------------+
 * |     位置基本信      | 位置附加信息项列表   |
 * |--------------------+--------------------+
 *
 * 位置附加信息项列表由各位置附加信息项组合，也可没有，根据消息头中的长度字段确定。
 *
 *
 * 位置基本信息数据格式
 * 起始字节  字段      数据类型      描述及要求
 * 0        报警标志   DWORD        报警标志位定义见 [报警标志位定义]
 * 4        状态      DWORD        状态位定义见 [状态位定义见]
 * 8        纬度      DWORD        以度为单位的纬度值乘以10的6次方，精确到百万分之一度
 * 12       经度      DWORD        以度为单位的经度值乘以10的6次方，精确到百万分之一度
 * 16       高程      WORD         海拔高度，单位为米(m)
 * 18       速度      WORD         1/10km/h
 * 20       方向      WORD         0-359，正北为 0，顺时针
 * 22       时间      BCD[6]       YY-MM-DD-hh-mm-ss(GMT+8 时间，本标准中之后涉及的时间均采用此时区)
 *
 *
 * 报警标志位定义
 * 位        定义                              处理说明
 * 0        1:紧急报警，触动报警开关后触发        收到应答后清零
 * 1        1:超速报警                          标志维持至报警条件解除
 * 2        1:疲劳驾驶                          标志维持至报警条件解除
 * 3        1:危险预警                          收到应答后清零
 * 4        1:GNSS 模块发生故障                 标志维持至报警条件解除
 * 5        1:GNSS 天线未接或被剪断              标志维持至报警条件解除
 * 6        1:GNSS 天线短路                     标志维持至报警条件解除
 * 7        1:终端主电源欠压                     标志维持至报警条件解除
 * 8        1:终端主电源掉电                     标志维持至报警条件解除
 * 9        1:终端 LCD 或显示器故障              标志维持至报警条件解除
 * 10       1:TTS 模块故障                      标志维持至报警条件解除
 * 11       1:摄像头故障                        标志维持至报警条件解除
 * 12       1:道路运输证 IC 卡模块故障           标志维持至报警条件解除
 * 13       1:超速预警                          标志维持至报警条件解除
 * 14       1:疲劳驾驶预警                       标志维持至报警条件解除
 * 15-17    保留
 * 18       1:当天累计驾驶超时                    标志维持至报警条件解除
 * 19       1:超时停车                           标志维持至报警条件解除
 * 20       1:进出区域                           收到应答后清零
 * 21       1:进出路线                           收到应答后清零
 * 22       1:路段行驶时间不足/过长                收到应答后清零
 * 23       1:路线偏离报警                        标志维持至报警条件解除
 * 24       1:车辆 VSS 故障                      标志维持至报警条件解除
 * 25       1:车辆油量异常                        标志维持至报警条件解除
 * 26       1:车辆被盗(通过车辆防盗器)             标志维持至报警条件解除
 * 27       1:车辆非法点火                        收到应答后清零
 * 28       1:车辆非法位移                        收到应答后清零
 * 29       1:碰撞预警                            标志维持至报警条件解除
 * 30       1:侧翻预警                            标志维持至报警条件解除
 * 31       1:非法开门报警(终端未设置区域时，不判断非法开门)  收到应答后清零
 *
 *
 * 状态位定义
 * 位              状态
 * 0               0:ACC 关;1: ACC 开
 * 1               0:未定位;1:定位
 * 2               0:北纬;1:南纬
 * 3               0:东经;1:西经
 * 4               0:运营状态;1:停运状态
 * 5               0:经纬度未经保密插件加密;1:经纬度已经保密插件加密
 * 6-7             保留
 * 8-9             00:空车;01:半载;10:保留;11:满载 (可用于客车的空、重车及货车的空载、满载状态表示，人工输入或传感器获取)
 * 10              0:车辆油路正常;1:车辆油路断开
 * 11              0:车辆电路正常;1:车辆电路断开
 * 12              0:车门解锁;1:车门加锁
 * 13              0:门 1 关;1:门 1 开(前门)
 * 14              0:门 2 关;1:门 2 开(中门)
 * 15              0:门 3 关;1:门 3 开(后门)
 * 16              0:门 4 关;1:门 4 开(驾驶席门)
 * 17              0:门 5 关;1:门 5 开(自定义)
 * 18              0:未使用 GPS 卫星进行定位;1:使用 GPS 卫星进行定位
 * 19              0:未使用北斗卫星进行定位;1:使用北斗卫星进行定位
 * 20              0:未使用 GLONASS 卫星进行定位;1:使用 GLONASS 卫星进行定位
 * 21              0:未使用 Galileo 卫星进行定位;1:使用 Galileo 卫星进行定位
 * 22-31           保留
 * 注:状态发生改变需立即上报位置信息
 *
 *
 * 位置附加信息项格式
 * 字段                   数据类型            描述及要求
 * 附加信息 ID             BYTE               1-255
 * 附加信息长度 BYTE        BYTE
 * 附加信息                                   附加信息定义见 附加信息定义
 *
 *
 * 附加信息定义
 * 附加信息 ID           附加信息长度           描述及要求
 * 0x01                 4                     里程，DWORD，1/10km，对应车上里程表读数
 * 0x02                 2                     油量，WORD，1/10L，对应车上油量表读数
 * 0x03                 2                     行驶记录功能获取的速度，WORD，1/10km/h
 * 0x04                 2                     需要人工确认报警事件的 ID，WORD，从 1 开始计数
 * 0x05-0x10                                  保留
 * 0x11                 1 或 5                超速报警附加信息 [超速报警附加信息]
 * 0x12                 6                     进出区域/路线报警 [进出区域/路线报警]
 * 0x13                 7                     路段行驶时间不足/过长报警附加信息 [路段行驶时间不足/过长报警附加信息]
 * 0x14-0x24                                  保留
 * 0x25                 4                     扩展车辆信号状态位 [扩展车辆信号状态位]
 * 0x2A                 2                     IO 状态位 [IO 状态位]
 * 0x2B                 4                     模拟量，bit0-15，AD0;bit16-31，AD1。
 * 0x30                 1                     BYTE，无线通信网络信号强度
 * 0x31                 1                     BYTE，GNSS 定位卫星数
 * 0xE0                 后续信息长度           后续自定义信息长度
 * 0xE1-0xFF                                  自定义区域
 *
 *
 * 超速报警附加信息
 * 起始字节  字段           数据类型        描述及要求
 * 0        位置类型        BYTE           0:无特定位置; 1:圆形区域; 2:矩形区域; 3:多边形区域; 4:路段
 * 1        区域或路段 ID   DWORD          若位置类型为 0，无该字段
 *
 *
 * 进出区域/路线报警附加信息消息体数据格式
 * 起始字节  字段           数据类型        描述及要求
 * 0        位置类型        BYTE           0:无特定位置; 1:圆形区域; 2:矩形区域; 3:多边形区域; 4:路段
 * 1        区域或路段 ID   DWORD          若位置类型为 0，无该字段
 * 5        方向           BYTE           0:进; 1:出
 *
 *
 * 路线行驶时间不足/过长报警附加信息消息体数据格式
 * 起始字节  字段           数据类型              描述及要求
 * 0        路段ID         DWORD
 * 4        路段行驶时间    WORD 单位为秒(s)      单位为秒(s)
 * 6        结果           BYTE 0:不足;1:过长    0:不足;1:过长
 *
 *
 * 扩展车辆信号状态位
 * 位        定义
 * 0         1:近光灯信号
 * 1         1:远光灯信号
 * 2         1:右转向灯信号
 * 3         1:左转向灯信号
 * 4         1:制动信号
 * 5         1:倒档信号
 * 6         1:雾灯信号
 * 7         1:示廓灯
 * 8         1:喇叭信号
 * 9         1:空调状态
 * 10        1:空挡信号
 * 11        1:缓速器工作
 * 12        1:ABS 工作
 * 13        1:加热器工作
 * 14        1:离合器状态
 * 15-31     保留
 *
 *
 *
 * IO 状态位
 * 位        定义
 * 0         1:深度休眠状态
 * 1         1:休眠状态
 * 2-15      保留
 *
 *
 *
 * @author lidashuang
 * @version 1.0
 */
public class Jtt808T18 extends JttMessage {

    /** 消息 ID */
    public final static int M_ID = 0x0200;

    /** 报警标记 */
    private byte[] alarmMark;
    /** 状态 */
    private byte[] status;
    /** 纬度 */
    private int latitude;
    /** 经度 */
    private int longitude;
    /** 高度 */
    private int height;
    /** 速度 */
    private int speed;
    /** 方向 */
    private int direction;
    /** 时间 */
    private String datetime;
    /** 扩展信息 */
    private byte[] extend;

    public Jtt808T18() {}

    public Jtt808T18(byte[] alarmMark, byte[] status, int latitude, int longitude,
                     int height, int speed, int direction, String datetime, byte[] extend) {
        this.alarmMark = alarmMark;
        this.status = status;
        this.latitude = latitude;
        this.longitude = longitude;
        this.height = height;
        this.speed = speed;
        this.direction = direction;
        this.datetime = datetime;
        this.extend = extend;
    }

    public Jtt808T18(byte[] alarmMark, byte[] status, int latitude, int longitude,
                     int height, int speed, int direction, String datetime, byte[] extend, byte[] bytes) {
        this.alarmMark = alarmMark;
        this.status = status;
        this.latitude = latitude;
        this.longitude = longitude;
        this.height = height;
        this.speed = speed;
        this.direction = direction;
        this.datetime = datetime;
        this.extend = extend;
        this.bytes = bytes;
    }

    @Override
    public int getMid() {
        return M_ID;
    }

    /**
     *  * 起始字节  字段      数据类型      描述及要求
     *  * 0        报警标志   DWORD        报警标志位定义见 [报警标志位定义]
     *  * 4        状态      DWORD        状态位定义见 [状态位定义见]
     *  * 8        纬度      DWORD        以度为单位的纬度值乘以10的6次方，精确到百万分之一度
     *  * 12       经度      DWORD        以度为单位的经度值乘以10的6次方，精确到百万分之一度
     *  * 16       高程      WORD         海拔高度，单位为米(m)
     *  * 18       速度      WORD         1/10km/h
     *  * 20       方向      WORD         0-359，正北为 0，顺时针
     *  * 22       时间      BCD[6]       YY-MM-DD-hh-mm-ss(GMT+8 时间，本标准中之后涉及的时间均采用此时区)
     * @param bytes 解码的参数
     * @return
     */
    @Override
    @SuppressWarnings("all")
    public byte[] encode() {
        ByteArrayOutputStream outputStream = null;
        try {
            outputStream = new ByteArrayOutputStream();
            outputStream.write(alarmMark);
            outputStream.write(status);
            outputStream.write(JttUtils.intToBytesBig(latitude));
            outputStream.write(JttUtils.intToBytesBig(longitude));
            outputStream.write(JttUtils.integerToHigh8Low8(height));
            outputStream.write(JttUtils.integerToHigh8Low8(speed));
            outputStream.write(JttUtils.integerToHigh8Low8(direction));
            outputStream.write(JttUtils.codeTo8421Bytes(datetime));
            if (extend != null && extend.length > 0) {
                outputStream.write(extend);
            }
            this.bytes = outputStream.toByteArray();
            return this.bytes;
        } catch (Exception e) {
            throw new RuntimeException("[ Jtt808T0 ] encode() ==> " + e);
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
    public Jtt808T18 decode(byte[] bytes) {
        try {
            return new Jtt808T18(
                    JttUtils.bytesArrayIntercept(bytes, 0, 4),
                    JttUtils.bytesArrayIntercept(bytes, 4, 4),
                    JttUtils.bytesToIntBig(JttUtils.bytesArrayIntercept(bytes, 8, 4)),
                    JttUtils.bytesToIntBig(JttUtils.bytesArrayIntercept(bytes, 12, 4)),
                    JttUtils.bytesToHigh8Low8(JttUtils.bytesArrayIntercept(bytes, 16, 2)),
                    JttUtils.bytesToHigh8Low8(JttUtils.bytesArrayIntercept(bytes, 18, 2)),
                    JttUtils.bytesToHigh8Low8(JttUtils.bytesArrayIntercept(bytes, 20, 2)),
                    JttUtils.bytesTo8421Code(JttUtils.bytesArrayIntercept(bytes, 22, 6)),
                    JttUtils.bytesArrayIntercept(bytes, 28, bytes.length - 28),
                    bytes
            );
        } catch (Exception e) {
            throw new RuntimeException("[ Jtt808T0 ] decode() ==> " + e);
        }
    }

    @Override
    public String toString() {
        return "{"
                + "\"alarmMark\":"
                + Arrays.toString(alarmMark)
                + ",\"status\":"
                + Arrays.toString(status)
                + ",\"latitude\":"
                + latitude
                + ",\"longitude\":"
                + longitude
                + ",\"height\":"
                + height
                + ",\"speed\":"
                + speed
                + ",\"direction\":"
                + direction
                + ",\"datetime\":\""
                + datetime + '\"'
                + ",\"extend\":"
                + Arrays.toString(extend)
                + ",\"bytes\":"
                + Arrays.toString(bytes)
                + "}";
    }

    public byte[] getAlarmMark() {
        return alarmMark;
    }

    public byte[] getStatus() {
        return status;
    }

    public int getLatitude() {
        return latitude;
    }

    public int getLongitude() {
        return longitude;
    }

    public int getHeight() {
        return height;
    }

    public int getSpeed() {
        return speed;
    }

    public int getDirection() {
        return direction;
    }

    public String getDatetime() {
        return datetime;
    }

    public byte[] getExtend() {
        return extend;
    }
}
