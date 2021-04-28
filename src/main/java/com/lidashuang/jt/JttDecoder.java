package com.lidashuang.jt;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
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
public class JttDecoder extends ByteToMessageDecoder {

    /** 标记字符 */
    private static final byte MARK = 0x7e;
    /** 转义字符 */
    private static final byte MARK_T = 0x7d;
    /** 转义字符 模型1 */
    private static final byte MARK_T_B1 = 0x02;
    /** 转义字符 模型2 */
    private static final byte MARK_T_B2 = 0x01;
    /** 日志对象 */
    private static final Logger LOGGER = LoggerFactory.getLogger(JttDecoder.class);
    /** 消息的头部对象 */
    private static final JttMessageHeader JTT_MESSAGE_HEADER = new JttMessageHeader();

    /** 分包数据保存地方 */
    private int subcontractMid = 0;
    private ByteArrayOutputStream subcontractStream = null;


    @Override
    protected void decode(ChannelHandlerContext context, ByteBuf byteBuf, List<Object> list) throws Exception {
        // 读取数据流
        final byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        LOGGER.info("[ 接收的数据 ] ===> " + Arrays.toString(bytes));
        // 解码且添加到管道中
        list.addAll(jtDecode(bytes));
    }

    /**
     * JT 解码/拆包操作
     * @param bytes 数据字节码
     * @return 解析的消息对象
     * @throws Exception 解析出现的异常
     */
    private List<JttMessage> jtDecode(byte[] bytes) throws Exception {
        // 创建返回的集合对象
        final List<JttMessage> result = new ArrayList<>();
        // 验证一下参数
        if (bytes == null || bytes.length == 0) {
            // 抛出异常 @0 用来快速定位
            LOGGER.error("JT 消息内容为空 ～ @0");
            throw new Exception("JT 消息解码异常～ @0");
        }
        // 采用 0x7e 表示，若校验码、消息头以及消息体中出现 0x7e，则要进行转义处理
        // 标识位 | 消息头 | 消息体 | 检验码 | 标识位
        // 当前索引
        int index = 0;
        // 判断是否是 MARK 记号开头
        if (bytes[index] == MARK) {
            // 循环读取数据，找到结尾的标记符号
            for (int i = 1; i < bytes.length; i++) {
                if (bytes[i] == MARK) {
                    // 对单条消息进行解码，且返回
                    final int dLength = i - index + 1;
                    final byte[] data = new byte[dLength];
                    System.arraycopy(bytes, index, data, 0, dLength);
                    final JttMessage jttMessage = jtDecodeByteToMessage(data);
                    if (jttMessage != null) {
                        result.add(jttMessage);
                    }
                    // 判断一下当前是不收最后一个位置，如果当前不是最后一个位置，说明存在多条消息
                    if (i + 1 < bytes.length) {
                        // 判断一下，下一位是否是 MARK 记号开头
                        if (bytes[i + 1] == MARK) {
                            // i 变大一次 且 更新索引
                            index = ++i;
                        } else {
                            LOGGER.error("JT 消息拆包出现异常 ～ @3");
                            throw new Exception("JT 消息解码异常 ～ @3");
                        }
                    } else {
                        index = i;
                    }
                }
            }
            // 消息读取完成，索引指向的下标为数组的最后一位
            // 否者说明最后一位不是 MARK 标记的，该消息为异常消息
            if (index + 1 != bytes.length) {
                LOGGER.error("JT 消息结尾标记异常 ～ @2");
                throw new Exception("JT 消息解码异常 ～ @2");
            }
        } else {
            // 抛出异常 @1 用来快速定位
            LOGGER.error("JT 消息头不是以 [ " + MARK + " ] 开头 ～ @1");
            throw new Exception("JT 消息解码异常 ～ @1");
        }
        return result;
    }

    /**
     * 单条消息解析
     * @param bytes 单条消息的字节码
     * @return 解析后的对象
     * @throws Exception 解析过程出现的异常
     */
    @SuppressWarnings("all")
    private JttMessage jtDecodeByteToMessage(byte[] bytes) throws Exception {
        // * 规则定义如下：
        // * 0x7e <————> 0x7d 后紧跟一个 0x02
        // * 0x7d <————> 0x7d 后紧跟一个 0x01
        // * 接收消息时：转义还原——>验证校验码——>解析消息

        // * 示例：
        // * 发送一包内容为 0x30 0x7e 0x08 0x7d 0x55 的数据包，
        // * 则经过封装如下：0x7e 0x30 0x7d 0x02 0x08 0x7d 0x01 0x55 0x7e

        // 开始的长度
        final int mLen = 1;
        // 结尾的长度
        final int vLen = 1;
        // 单条消息的最小长度 1 标记 + 12 消息头<12/16> + 1 校验 + 1 标记
        final int minLen = mLen + 12 + vLen + mLen;

        // 最短长度校验
        if (bytes.length < minLen) {
            LOGGER.error("JT 消息转对象长度出现异常 ～");
            throw new Exception("JT 消息转对象长度出现异常 ～");
        }

        // 索引
        int index = 0;
        // 校验码
        int checkCode = bytes[mLen];
        // 校验的状态
        boolean checkStatus;
        // 数据源
        byte[] data = new byte[bytes.length - mLen - mLen];
        // 转义还原 且 计算校验码
        for (int i = mLen; i < bytes.length - 1; i++) {
            // 转义还原
            if (bytes[i] == MARK_T && i + 1 < bytes.length - 1) {
                if (bytes[i + 1] == MARK_T_B1) {
                    data[index++] = MARK;
                    i++;
                } else if (bytes[i + 1] == MARK_T_B2) {
                    data[index++] = MARK_T;
                    i++;
                } else {
                    data[index++] = bytes[i];
                }
            } else {
                data[index++] = bytes[i];
            }
            // 计算验证码
            if (i != mLen && i != bytes.length - 2) {
                checkCode ^= data[index - 1];
            }
        }
        // 校验码验证
        checkStatus = (checkCode == data[index - 1]);
        // 压缩数组
        data = Arrays.copyOf(data, index - 1);
        // 校验码是否正确
        if (checkStatus) {
            // 解析头部字节码内容
            final JttMessageHeader header = JTT_MESSAGE_HEADER.decode(data);
            // 判断是否分包
            if (header.getSubcontract()) {
                if (header.getSubcontractIndex() == 1) {
                    // 删除分包的数据
                    cleanSubcontract();
                    subcontractMid = header.getId();
                    subcontractStream = new ByteArrayOutputStream();
                    subcontractStream.write(JttUtils.bytesArrayIntercept(data,
                            header.getHeadLength(), data.length - header.getHeadLength()));
                    return null;
                } else if (header.getSubcontractIndex() < header.getSubcontractLength()) {
                    if (subcontractStream != null && subcontractMid == header.getId()) {
                        subcontractStream.write(JttUtils.bytesArrayIntercept(data,
                                header.getHeadLength(), data.length - header.getHeadLength()));
                        return null;
                    } else {
                        LOGGER.error("JT 消息分包数据异常 ～ @1");
                        throw new Exception("JT 消息分包数据异常 ～ @1");
                    }
                } else {
                    if (subcontractStream != null && subcontractMid == header.getId()) {
                        final JttMessage jttMessage = JttRegistry.getMessageCore(header.getId()).decode(
                                JttMessage.beforeDecoderHook.execute(header.getEncryption(), subcontractStream.toByteArray()));
                        jttMessage.setHeader(header);
                        return jttMessage;
                    } else {
                        LOGGER.error("JT 消息分包数据异常 ～ @2");
                        throw new Exception("JT 消息分包数据异常 ～ @2");
                    }
                }
            } else {
                // 如果获取正常的数据，就删除分包的数据
                cleanSubcontract();
                // 去注册中心查询对应的消息处理器，进行解码操作
                final JttMessage jttMessage = JttRegistry.getMessageCore(header.getId()).decode(
                        JttMessage.beforeDecoderHook.execute(header.getEncryption(), JttUtils.bytesArrayIntercept(
                                data, header.getHeadLength(), data.length - header.getHeadLength())));
                jttMessage.setHeader(header);
                LOGGER.info("[ 接收的数据 ] (消息对象) ==> HEADER: " + header + " MESSAGE: " + jttMessage);
                return jttMessage;
            }
        } else {
            LOGGER.error("JT 消息校验码异常 ～");
            throw new Exception("JT 消息校验码异常 ～");
        }
    }

    /**
     * 清除分包数据
     */
    private void cleanSubcontract() {
        if (subcontractStream != null) {
            try {
                subcontractStream.flush();
                subcontractStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                subcontractStream = null;
            }
        }
    }
}