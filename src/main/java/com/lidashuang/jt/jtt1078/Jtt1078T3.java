package com.lidashuang.jt.jtt1078;

import com.lidashuang.jt.JttMessage;
import com.lidashuang.jt.JttUtils;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * 实时视屏音频传输协议
 * 消息
 * 0x9101
 * @author lidashuang
 * @version 1.0
 */
public class Jtt1078T3 extends JttMessage {

    /** 消息 ID */
    public final static int M_ID = 0x9101;

    /** 服务器 IP 地址长度 */
    private int serverHostLength;
    /** 服务器 IP 地址 */
    private String serverHost;
    /** 服务器 IP TCP 端口 */
    private int serverTcpPort;
    /** 服务器 IP UDP 端口 */
    private int serverUdpPort;
    /** 服务器逻辑通道号 */
    private int logicalChannel;
    /** 数据类型 */
    private int dataType;
    /** 码流类型 */
    private int bitstreamType;

    public Jtt1078T3() {}

    public Jtt1078T3(int serverHostLength, String serverHost, int serverTcpPort,
                     int serverUdpPort, int logicalChannel, int dataType, int bitstreamType) {
        this.serverHostLength = serverHostLength;
        this.serverHost = serverHost;
        this.serverTcpPort = serverTcpPort;
        this.serverUdpPort = serverUdpPort;
        this.logicalChannel = logicalChannel;
        this.dataType = dataType;
        this.bitstreamType = bitstreamType;
    }

    public Jtt1078T3(int serverHostLength, String serverHost, int serverTcpPort, int serverUdpPort,
                     int logicalChannel, int dataType, int bitstreamType, byte[] bytes) {
        this.serverHostLength = serverHostLength;
        this.serverHost = serverHost;
        this.serverTcpPort = serverTcpPort;
        this.serverUdpPort = serverUdpPort;
        this.logicalChannel = logicalChannel;
        this.dataType = dataType;
        this.bitstreamType = bitstreamType;
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
            outputStream.write((byte) serverHostLength);
            outputStream.write(serverHost.getBytes(StandardCharsets.UTF_8));
            outputStream.write(JttUtils.integerToHigh8Low8(serverTcpPort));
            outputStream.write(JttUtils.integerToHigh8Low8(serverUdpPort));
            outputStream.write((byte) logicalChannel);
            outputStream.write((byte) dataType);
            outputStream.write((byte) bitstreamType);
            return this.bytes;
        } catch (Exception e) {
            throw new RuntimeException("[ Jtt1078T3 ] encode() ==> " + e);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public Jtt1078T3 decode(byte[] bytes) {
        try {
            final int len = bytes[0];
            return new Jtt1078T3(
                    len,
                    new String(JttUtils.bytesArrayIntercept(bytes, 1, len), StandardCharsets.UTF_8),
                    JttUtils.bytesToHigh8Low8(JttUtils.bytesArrayIntercept(bytes, 1 + len, 2)),
                    JttUtils.bytesToHigh8Low8(JttUtils.bytesArrayIntercept(bytes, 3 + len, 2)),
                    bytes[5 + len],
                    bytes[6 + len],
                    bytes[7 + len],
                    bytes
            );
        } catch (Exception e) {
            throw new RuntimeException("[ Jtt1078T3 ] decode() ==> " + e);
        }
    }

    @Override
    public String toString() {
        return "{"
                + "\"bytes\":"
                + Arrays.toString(bytes)
                + "}";
    }


}
