package com.lidashuang.jt.jt808;

import com.lidashuang.jt.JtMessage;
import com.lidashuang.jt.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author lidashuang
 * @version 1.0
 *
 * 数据格式
 * 终端注册应答
 * 消息ID：0x8100。
 * 终端注册应答消息体数据格式
 * 起始字节  字段         数据类型    描述及要求
 * 0        应答流水号    WORD       对应的终端注册消息的流水号
 * 2        结果         BYTE       0：成功；1：车辆已被注册；2：数据库中无该车辆；3：终端已被注册；4：数据库中无该终端
 * 3        鉴权码       STRING     只有在成功后才有该字段
 */
public class Jt808T6 extends JtMessage {
    /** 消息 ID：0x8100 */
    public static final int M_ID = 0x8100;

    private final String phone;

    private final int number;

    private final int result;

    private final String authCode;

    public Jt808T6(String phone, int number, int result, String authCode) {
        super();
        this.phone = phone;
        this.number = number;
        this.result = result;
        this.authCode = authCode;
        System.out.println(this.toString());
    }

    @Override
    public void decode() {
    }

    @Override
    public byte[] encode() {
        ByteArrayOutputStream outputStream = null;
        try {
            outputStream = new ByteArrayOutputStream();
            outputStream.write(Utils.integerToHigh8Low8(number));
            outputStream.write(result);
            outputStream.write(authCode.getBytes("GBK"));
            final byte[] content = outputStream.toByteArray();
            if (content.length < 1024) {
                final HeadMessage headMessage = new HeadMessage(
                        M_ID,
                        12,
                        content.length,
                        0,
                        false,
                        phone,
                        number,
                        0,
                        0
                );
                final byte[] result = new byte[content.length + headMessage.getHeadLength()];
                System.arraycopy(headMessage.toBytes(), 0, result, 0, headMessage.getHeadLength());
                System.arraycopy(content, headMessage.getHeadLength(), result, headMessage.getHeadLength(), content.length);
                System.out.println("推送的数据为： " + Arrays.toString(result));
                return result;
            } else {
                throw new RuntimeException("不支持多包  下次开放");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
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
    public String toString() {
        return "{"
                + "\"phone\":\""
                + phone + '\"'
                + ",\"number\":"
                + number
                + ",\"result\":"
                + result
                + ",\"authCode\":\""
                + authCode + '\"'
                + "}";
    }
}
