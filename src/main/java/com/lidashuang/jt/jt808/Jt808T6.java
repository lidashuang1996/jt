package com.lidashuang.jt.jt808;

import com.lidashuang.jt.JtMessage;
import com.lidashuang.jt.JtUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

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

    private int number;
    private int result;
    private String authCode;

    public Jt808T6(byte[] bytes) {
        this.bytes = bytes;
        this.header = new Header(this.bytes);
    }

    public Jt808T6(int number, int result, String authCode) {
        this.number = number;
        this.result = result;
        this.authCode = authCode;
    }

    @Override
    public int getType() {
        return M_ID;
    }

    @Override
    public JtMessage decode() {
        return this;
    }

    @Override
    public byte[] encode() {
        ByteArrayOutputStream outputStream = null;
        try {
            outputStream = new ByteArrayOutputStream();
            outputStream.write(JtUtils.integerToHigh8Low8(number));
            outputStream.write(result);
            outputStream.write(authCode.getBytes("GBK"));
            final byte[] content = outputStream.toByteArray();
            if (content.length <= MAX_MESSAGE_CONTENT_LENGTH) {
                final Header rHeader = new Header(M_ID, 12, content.length, 0,
                        false, DEFAULT_MESSAGE_PHONE, number, 0, 0);
                final byte[] result = new byte[content.length + rHeader.getHeadLength()];
                System.arraycopy(rHeader.toBytes(), 0, result, 0, rHeader.getHeadLength());
                System.arraycopy(content, 0, result, rHeader.getHeadLength(), content.length);
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
}
