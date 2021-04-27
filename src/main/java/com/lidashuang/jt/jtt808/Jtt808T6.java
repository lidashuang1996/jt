package com.lidashuang.jt.jtt808;

import com.lidashuang.jt.JttMessage;
import com.lidashuang.jt.JttUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * 数据格式
 *
 * 终端注册应答
 * 消息ID：0x8100。
 *
 * 终端注册应答消息体数据格式
 * 起始字节  字段         数据类型    描述及要求
 * 0        应答流水号    WORD       对应的终端注册消息的流水号
 * 2        结果         BYTE       0：成功；1：车辆已被注册；2：数据库中无该车辆；3：终端已被注册；4：数据库中无该终端
 * 3        鉴权码       STRING     只有在成功后才有该字段
 *
 * @author lidashuang
 * @version 1.0
 */
public class Jtt808T6 extends JttMessage {
    /** 消息 ID：0x8100 */
    public static final int M_ID = 0x8100;

    /** 应答流水号 */
    private int answerNumber;
    /** 结果 */
    private int result;
    /** 鉴权码 */
    private String authCode;

    public Jtt808T6() { }

    public Jtt808T6(int answerNumber, int result, String authCode) {
        this.answerNumber = answerNumber;
        this.result = result;
        this.authCode = authCode;
    }

    public Jtt808T6(int answerNumber, int result, String authCode, byte[] bytes) {
        this.answerNumber = answerNumber;
        this.result = result;
        this.authCode = authCode;
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
            outputStream.write(JttUtils.integerToHigh8Low8(answerNumber));
            outputStream.write((byte) result);
            outputStream.write(authCode.getBytes("UTF-8"));
            this.bytes = outputStream.toByteArray();
            return this.bytes;
        } catch (Exception e) {
            throw new RuntimeException("[ Jtt808T6 ] encode() ==> " + e);
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
    public Jtt808T6 decode(byte[] bytes) {
        try {
            return new Jtt808T6(
                    JttUtils.bytesToHigh8Low8(JttUtils.bytesArrayIntercept(bytes, 0, 2)),
                    bytes[2],
                    new String(JttUtils.bytesArrayIntercept(bytes, 3, bytes.length - 3), StandardCharsets.UTF_8),
                    bytes
            );
        } catch (Exception e) {
            throw new RuntimeException("[ Jtt808T6 ] decode() ==> " + e);
        }
    }

    @Override
    public String toString() {
        return "{"
                + "\"answerNumber\":"
                + answerNumber
                + ",\"result\":"
                + result
                + ",\"authCode\":\""
                + authCode + '\"'
                + ",\"bytes\":"
                + Arrays.toString(bytes)
                + "}";
    }

    public int getAnswerNumber() {
        return answerNumber;
    }

    public int getResult() {
        return result;
    }

    public String getAuthCode() {
        return authCode;
    }
}
