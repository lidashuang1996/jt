package com.lidashuang.jt;

import com.lidashuang.jt.jt808.*;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.ArrayList;
import java.util.List;

/**
 * 解码器
 *
 * 协议基础
 *
 * 1 通信方式
 * 协议采用的通信方式应符合 JT/T 794 中的相关规定，通信协议采用 TCP 或 UDP，平台
 * 作为服务器端，终端作为客户端。当数据通信链路异常时，终端可以采用 SMS 消息方式进
 * 行通信。
 *
 * 2 数据类型
 * 协议消息中使用的数据类型
 * 数据类型     描述及要求
 * BYTE        无符号单字节整型（字节，8 位）
 * WORD        无符号双字节整型（字，16 位）
 * DWORD       无符号四字节整型（双字，32 位）
 * BYTE[n]     n 字节
 * BCD[n]      8421 码，n 字节
 * STRING      GBK 编码，若无数据，置空
 *
 * 3 传输规则
 * 协议采用大端模式(big-endian)的网络字节序来传递字和双字。
 * 约定如下：
 * ——字节(BYTE)的传输约定：按照字节流的方式传输；
 * ——字(WORD)的传输约定：先传递高八位，再传递低八位；
 * ——双字(DWORD)的传输约定：先传递高 24 位，然后传递高 16 位，再传递高八位，最后传递低八位。
 *
 *
 * 消息的组成
 *
 * 1 消息结构
 *
 * 每条消息由标识位、消息头、消息体和校验码组成。
 * |-------|-------|-------|-------|-------|
 * | 标识位 | 消息头 | 消息体 | 检验码 | 标识位|
 * |-------|-------|-------|-------|-------|
 *
 * 2 标识位
 * 采用 0x7e 表示，若校验码、消息头以及消息体中出现 0x7e，则要进行转义处理，转义
 * 规则定义如下：
 * 0x7e <————> 0x7d 后紧跟一个 0x02；
 * 0x7d <————> 0x7d 后紧跟一个 0x01。
 * 转义处理过程如下：
 * 发送消息时：消息封装——>计算并填充校验码——>转义；
 * 接收消息时：转义还原——>验证校验码——>解析消息。
 * 示例：
 * 发送一包内容为 0x30 0x7e 0x08 0x7d 0x55 的数据包，
 * 则经过封装如下：0x7e 0x30 0x7d 0x02 0x08 0x7d 0x01 0x55 0x7e
 *
 * 3 消息头
 * 消息头内容详见
 * 起始字节  字段         数据类型        描述及要求
 * 0        消息ID       WORD
 * 2        消息体属性    WORD           消息体属性格式结构图
 * 4        终端手机号    BCD[6]         根据安装后终端自身的手机号转换。手机号不足 12位，则在前补充数字，大陆手机号补充数字 0，港澳台则根据其区号进行位数补充。
 * 10       消息流水号    WORD           按发送顺序从 0 开始循环累加
 * 12       消息包封装项                 如果消息体属性中相关标识位确定消息分包处理，则该项有内容，否则无该项
 *
 * 消息体属性格式结构图：
 * | 15  14 | 13 | 12  11  10 | 9 8 7 6 5 4 3 2 1 0 |
 * |  保留  | 分包| 数据加密方式 |    消息体长度       |
 *
 * 消息体属性格式结构图
 * 数据加密方式：
 * ——bit10~bit12 为数据加密标识位；
 * ——当此三位都为 0，表示消息体不加密；
 * ——当第 10 位为 1，表示消息体经过 RSA 算法加密；
 * ——其他保留。
 *
 * 分包：
 * 当消息体属性中第 13 位为 1 时表示消息体为长消息，进行分包发送处理，具体分包信
 * 息由消息包封装项决定；若第 13 位为 0，则消息头中无消息包封装项字段。
 *
 * 消息包封装项内容
 * 起始字节  字段         数据类型        描述及要求
 * 0        消息总包数    WORD           该消息分包后的总包数
 * 2        包序号       WORD           从 1 开始
 *
 *
 * 校验码
 *
 * 校验码指从消息头开始，同后一字节异或，直到校验码前一个字节，占用一个字节。
 *
 * 通信连接
 *
 * 1 连接的建立
 * 终端与平台的数据日常连接可采用 TCP 或 UDP 方式，终端复位后应尽快与平台建立连
 * 接，连接建立后立即向平台发送终端鉴权消息进行鉴权。
 *
 * 2 连接的维持
 * 连接建立和终端鉴权成功后，在没有正常数据包传输的情况下，终端应周期性向平台发
 * 送终端心跳消息，平台收到后向终端发送平台通用应答消息，发送周期由终端参数指定。
 *
 * 3 连接的断开
 *
 * 平台和终端均可根据 TCP 协议主动断开连接，双方都应主动判断 TCP 连接是否断开。
 * 平台判断 TCP 连接断开的方法：
 * ——根据 TCP 协议判断出终端主动断开；
 * ——相同身份的终端建立新连接，表明原连接已断开；
 * ——在一定的时间内未收到终端发出的消息，如终端心跳。
 *
 * 终端判断 TCP 连接断开的方法：
 * ——根据 TCP 协议判断出平台主动断开；
 * ——数据通信链路断开；
 * ——数据通信链路正常，达到重传次数后仍未收到应答。
 *
 * @author lidashuang
 * @version 1.0
 */
public class JtDecoder extends ByteToMessageDecoder {

    /***/
    private static final byte MARK = 0x7e;
    private static final byte MARK_T = 0x7d;

    /***/
    private static final byte MARK_T_B1 = 0x02;
    /***/
    private static final byte MARK_T_B2 = 0x01;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        // 读取数据流
        final byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        // 解码且添加到管道中
        list.addAll(jtDecode(bytes));
    }

    private static List<JtMessage> jtDecode(byte[] bytes) throws Exception {
        final List<JtMessage> result = new ArrayList<>();
        // 采用 0x7e 表示，若校验码、消息头以及消息体中出现 0x7e，则要进行转义处理
        // 标识位 | 消息头 | 消息体 | 检验码 | 标识位

        // 当前索引
        int index = 0;
        if (bytes[index] == MARK) {
            List<Byte> data = new ArrayList<>();
            data.add(MARK);
            for (int i = 1; i < bytes.length; i++) {
                data.add(bytes[i]);
                if (bytes[i] == MARK) {
                    System.out.println("bb -> " + data);
                    result.add(jtDecodeByteToMessage(data));
                    // 当前不是最后一个位置，说明存在多条消息
                    if (i < bytes.length - 1) {
                        if (bytes[i + 1] == MARK) {
                            index = ++i;
                            data = new ArrayList<>();
                            data.add(MARK);
                        } else {
                            throw new Exception("JT 消息解码异常～@3");
                        }
                    } else {
                        index = i;
                    }
                }
            }
            if (index + 1 != bytes.length) {
                throw new Exception("JT 消息解码异常～@2");
            }
        } else {
            throw new Exception("JT 消息解码异常～@1");
        }
        return result;
    }

    private static JtMessage jtDecodeByteToMessage(List<Byte> bytes) throws Exception {
        // * 规则定义如下：
        // * 0x7e <————> 0x7d 后紧跟一个 0x02；
        // * 0x7d <————> 0x7d 后紧跟一个 0x01。
        // * 接收消息时：转义还原——>验证校验码——>解析消息。

        // * 示例：
        // * 发送一包内容为 0x30 0x7e 0x08 0x7d 0x55 的数据包，
        // * 则经过封装如下：0x7e 0x30 0x7d 0x02 0x08 0x7d 0x01 0x55 0x7e

        final int minLen = 4;
        int len = bytes.size();

        if (len < minLen) {
            throw new Exception("JT 消息长度出现异常");
        }

        Integer verificationCode = null;
        boolean isVerificationPass = false;

        for (int i = 0; i < len; i++) {
            // 转义还原
            if (bytes.get(i) == MARK_T && i > 0 && i + 1 < bytes.size()) {
                if (bytes.get(i + 1) == MARK_T_B1) {
                    bytes.set(i, MARK);
                    bytes.remove(i + 1);
                    len--;
                } else if (bytes.get(i + 1) == MARK_T_B2) {
                    bytes.set(i, MARK_T);
                    bytes.remove(i + 1);
                    len--;
                }
            }
            // 验证校验码
            if (i == 1) {
                verificationCode = (int) bytes.get(i);
            } else if (i == bytes.size() - 2) {
                isVerificationPass = verificationCode != null && verificationCode == (int) bytes.get(bytes.size() - 2);
            } else if (verificationCode != null && i < bytes.size() - 2) {
                verificationCode = verificationCode ^ bytes.get(i);
            }
        }
        if (isVerificationPass) {
            // 获取类型
            final int type = Utils.bytesToHigh8Low8(new byte[] { bytes.get(1),  bytes.get(2) });
            System.out.println("type--> " + type);
            // 删除前面后面的数据
            bytes.remove(0);
            bytes.remove(bytes.size() - 1);
            bytes.remove(bytes.size() - 1);
            switch (type) {
                // 终端注册 --> 终端注册应答<8.6 终端注册应答>
                case Jt808T5.M_ID:
                    return new Jt808T5(Utils.byteListToByteArray(bytes));
                // 终端注销
                case Jt808T7.M_ID:
                    return new Jt808T7(Utils.byteListToByteArray(bytes));
                // 终端鉴权(重新连接后发送的鉴权消息)
                case Jt808T8.M_ID:
                    return new Jt808T8(Utils.byteListToByteArray(bytes));
                default:
                    return new Jt808T0(Utils.byteListToByteArray(bytes));
            }
        } else {
            throw new Exception("JT 消息验证码异常");
        }
    }
}