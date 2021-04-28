package com.lidashuang.jt.jtt1078;

import com.lidashuang.jt.JttMessage;

import java.util.Arrays;

/**
 * 实时视屏音频传输控制
 * 消息
 * 0x9102
 * @author lidashuang
 * @version 1.0
 */
public class Jtt1078T4 extends JttMessage {

    /** 消息 ID */
    public final static int M_ID = 0x9101;

    /** 控制指令 */
    private int controlCommand;
    /** 关闭音视屏类型 */
    private int closeAudioVideoType;
    /** 切换码流类型 */
    private int switchBitstreamType;

    public Jtt1078T4() {}

    public Jtt1078T4(int controlCommand, int closeAudioVideoType, int switchBitstreamType) {
        this.controlCommand = controlCommand;
        this.closeAudioVideoType = closeAudioVideoType;
        this.switchBitstreamType = switchBitstreamType;
    }

    public Jtt1078T4(int controlCommand, int closeAudioVideoType, int switchBitstreamType, byte[] bytes) {
        this.controlCommand = controlCommand;
        this.closeAudioVideoType = closeAudioVideoType;
        this.switchBitstreamType = switchBitstreamType;
        this.bytes = bytes;
    }

    @Override
    public int getMid() {
        return M_ID;
    }

    @Override
    @SuppressWarnings("all")
    public byte[] encode() {
        try {
            this.bytes = new byte[] {
                    (byte) controlCommand,
                    (byte) closeAudioVideoType,
                    (byte) switchBitstreamType
            };
            return this.bytes;
        } catch (Exception e) {
            throw new RuntimeException("[ Jtt1078T4 ] encode() ==> " + e);
        }
    }

    @Override
    public Jtt1078T4 decode(byte[] bytes) {
        try {
            return new Jtt1078T4(bytes[0], bytes[1], bytes[2]);
        } catch (Exception e) {
            throw new RuntimeException("[ Jtt1078T4 ] decode() ==> " + e);
        }
    }

    @Override
    public String toString() {
        return "{"
                + "\"bytes\":"
                + Arrays.toString(bytes)
                + ",\"controlCommand\":"
                + controlCommand
                + ",\"closeAudioVideoType\":"
                + closeAudioVideoType
                + ",\"switchBitstreamType\":"
                + switchBitstreamType
                + "}";
    }
}
