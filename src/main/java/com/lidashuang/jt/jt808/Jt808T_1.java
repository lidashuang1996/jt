package com.lidashuang.jt.jt808;

import com.lidashuang.jt.JtMessage;
import com.lidashuang.jt.JtUtils;

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
public class Jt808T_1 extends JtMessage {
    /** 消息 ID：0x8100 */
    public static final int M_ID = 0x8001;

    private String phone;
    private int number;
    private int messageId;
    private int result;

    public Jt808T_1() { }

    public Jt808T_1(String phone, int number, int messageId, int result) {
        this.phone = phone;
        this.number = number;
        this.result = result;
        this.messageId = messageId;
    }

    @Override
    public int getType() {
        return M_ID;
    }

    @Override
    public JtMessage decode(byte[] bytes) {
        this.bytes = bytes;
        this.header = new Header(this.bytes);
        return this;
    }

    @Override
    public byte[] encode() {
        ByteArrayOutputStream outputStream = null;
        try {
            outputStream = new ByteArrayOutputStream();
            outputStream.write(JtUtils.integerToHigh8Low8(number));
            outputStream.write(JtUtils.integerToHigh8Low8(messageId));
            outputStream.write((byte) result);
            final byte[] content = outputStream.toByteArray();
            if (content.length <= MAX_MESSAGE_CONTENT_LENGTH) {
                final Header rHeader = new Header(M_ID, 12, content.length, 0,
                        false, phone, number, 0, 0);
                final byte[] result = new byte[content.length + rHeader.getHeadLength()];
                System.out.println(rHeader.toString());
                System.arraycopy(rHeader.toBytes(), 0, result, 0, rHeader.getHeadLength());
                System.arraycopy(content, 0, result, rHeader.getHeadLength(), content.length);
                this.header = rHeader;
                return result;
            } else {
                // 多包加密
                // int a = (int) Math.ceil((double) content.length / MAX_MESSAGE_CONTENT_LENGTH);
                throw new RuntimeException("不支持多包 ～");
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
                + "\"bytes\":"
                + Arrays.toString(bytes)
                + ",\"header\":"
                + header
                + ",\"phone\":\""
                + phone + '\"'
                + ",\"number\":"
                + number
                + ",\"result\":"
                + result
                + ",\"messageId\":\""
                + messageId + '\"'
                + "}";
    }
}
