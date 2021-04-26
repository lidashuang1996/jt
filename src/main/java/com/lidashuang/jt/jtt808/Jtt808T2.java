package com.lidashuang.jt.jtt808;

import com.lidashuang.jt.JttUtils;
import com.lidashuang.jt.JttMessage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 *
 * 数据格式
 *
 * 平台通用应答
 * 消息 ID:0x8001
 *
 * 平台通用应答消息体数据格式
 *
 * 起始字节   字段        数据类型    描述及要求
 * 0         应答流水号   WORD       对应的终端消息的流水号
 * 2         应答 ID     WORD       对应的终端消息的 ID
 * 4         结果        BYTE       0:成功/确认;1:失败;2:消息有误;3:不支持;4:报警 处理确认;
 *
 * @author lidashuang
 * @version 1.0
 */
public class Jtt808T2 extends JttMessage {

    /** 消息 ID */
    public final static int M_ID = 0x8001;

    /** 应答流水号 */
    private int answerNumber;
    /** 应答 ID */
    private int answerId;
    /** 结果 */
    private int result;

    public Jtt808T2() {}

    public Jtt808T2(int answerNumber, int answerId, int result) {
        this.answerNumber = answerNumber;
        this.answerId = answerId;
        this.result = result;
    }

    public Jtt808T2(int answerNumber, int answerId, int result, byte[] bytes) {
        this.answerNumber = answerNumber;
        this.answerId = answerId;
        this.result = result;
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
            outputStream.write(JttUtils.integerToHigh8Low8(answerId));
            outputStream.write((byte) result);
            outputStream.flush();
            this.bytes = outputStream.toByteArray();
            return this.bytes;
        } catch (Exception e) {
            throw new RuntimeException("[ Jtt808T2 ] encode() ==> " + e);
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
    public Jtt808T2 decode(byte[] bytes) {
        try {
            return new Jtt808T2(
                    JttUtils.bytesToHigh8Low8(JttUtils.bytesArrayIntercept(bytes, 0, 2)),
                    JttUtils.bytesToHigh8Low8(JttUtils.bytesArrayIntercept(bytes, 2, 2)),
                    bytes[4],
                    bytes
            );
        } catch (Exception e) {
            throw new RuntimeException("[ Jtt808T2 ] decode() ==> " + e);
        }
    }

    @Override
    public String toString() {
        return "{"
                + "\"answerNumber\":"
                + answerNumber
                + ",\"answerId\":"
                + answerId
                + ",\"result\":"
                + result
                + ",\"bytes\":"
                + Arrays.toString(bytes)
                + "}";
    }

    public int getAnswerNumber() {
        return answerNumber;
    }

    public int getAnswerId() {
        return answerId;
    }

    public int getResult() {
        return result;
    }
}