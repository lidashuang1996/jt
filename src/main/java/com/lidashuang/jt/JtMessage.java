package com.lidashuang.jt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author lidashuang
 * @version 1.0
 */
public abstract class JtMessage  {

    /** 日志对象 */
    protected static final Logger LOGGER = LoggerFactory.getLogger(JtMessage.class);

    /** 最大消息长度 */
    protected static final int MAX_MESSAGE_CONTENT_LENGTH = 1024;

    /** 默认的消息长度 */
    protected static final String DEFAULT_MESSAGE_PHONE = "000000000000";

    /** 消息源字节码数据 */
    protected byte[] bytes;

    /** 消息头 */
    protected Header header;

    /**
     * 获取消息内容
     * @return 消息内容的字节码
     */
    public byte[] getBytes() {
        return bytes;
    }

    /**
     * 获取头部对象
     * @return 头部对象
     */
    public Header getHeader() {
        return header;
    }

    /**
     * 获取消息类型
     * @return 消息类型
     */
    public abstract int getType();

    /**
     * 编码 [消息头 + 消息内容]
     * @return 编码后内容的字节码
     */
    public abstract byte[] encode();

    /**
     * 解码 [消息头 + 消息内容]
     * @return 解码后的消息对象
     */
    public abstract JtMessage decode();

    /**
     * 消息头的模型
     */
    public static class Header {
        /** 最小消息长度 */
        private static final int MIN_SIZE = 12;
        /** 最大消息长度 */
        private static final int MAX_SIZE = 16;
        /** 消息的 ID <类型> */
        private final Integer id;
        /** 头长度 */
        private final Integer headLength;
        /** 内容长度 */
        private final Integer contentLength;
        /** 加密类型 */
        private final Integer encryption;
        /** 是否分包 */
        private final Boolean subcontract;
        /** 手机号码 */
        private final String phone;
        /** 消息的流水号 */
        private final Integer number;
        /** 分包的总数 */
        private final Integer subcontractLength;
        /** 分包的索引 */
        private final Integer subcontractIndex;

        /**
         * 构造
         * 解码操作
         */
        public Header(byte[] bytes) {
            if (bytes.length >= MIN_SIZE) {
                // 消息的类型
                this.id = JtUtils.bytesToHigh8Low8(JtUtils.bytesArrayIntercept(bytes, 0, 2));
                // 消息的加密类型
                this.encryption = (bytes[2] >> 2) & 0x07;
                // 消息是否分包
                this.subcontract = (((bytes[2] >> 5) & 0x01) == 1);
                // 头部消息的长度
                this.headLength = (subcontract ? 16 : 12);
                // 内容消息的长度
                // 消息长度占 8 + 2 比特，最大表示长度为 2 ^ 10 = 1024
                this.contentLength = JtUtils.bytesToHigh8Low8(new byte[] { (byte) (bytes[2] & 0x03), bytes[3] });
                // 获取手机号码
                this.phone
                        = JtUtils.byteTo8421Code(bytes[4])
                        + JtUtils.byteTo8421Code(bytes[5])
                        + JtUtils.byteTo8421Code(bytes[6])
                        + JtUtils.byteTo8421Code(bytes[7])
                        + JtUtils.byteTo8421Code(bytes[8])
                        + JtUtils.byteTo8421Code(bytes[9]);
                this.number = JtUtils.bytesToHigh8Low8(JtUtils.bytesArrayIntercept(bytes, 10, 2));
                // 分包的索引
                int subcontractIndex = 0;
                // 分包的长度
                int subcontractLength = 0;
                // 写入分包数据
                if (subcontract) {
                    if (bytes.length >= MAX_SIZE) {
                        subcontractIndex = JtUtils.bytesToHigh8Low8(JtUtils.bytesArrayIntercept(bytes, 14, 2));
                        subcontractLength = JtUtils.bytesToHigh8Low8(JtUtils.bytesArrayIntercept(bytes, 12, 2));
                    } else {
                        LOGGER.info("解码消息 HEADER 分包消息长度不够 ～");
                        throw new RuntimeException("解码消息 HEADER 分包消息长度不够 ～");
                    }
                }
                this.subcontractIndex = subcontractIndex;
                this.subcontractLength = subcontractLength;
            } else {
                LOGGER.info("解码消息 HEADER 消息长度不够 ～");
                throw new RuntimeException("解码消息 HEADER 消息长度不够 ～");
            }
        }

        /**
         * 构造
         * 赋值操作
         */
        public Header(Integer id, Integer headLength, Integer contentLength,
                      Integer encryption, Boolean subcontract, String phone,
                      Integer number, Integer subcontractLength, Integer subcontractIndex) {
            this.id = id;
            this.headLength = headLength;
            this.contentLength = contentLength;
            this.encryption = encryption;
            this.subcontract = subcontract;
            this.phone = phone;
            this.number = number;
            this.subcontractLength = subcontractLength;
            this.subcontractIndex = subcontractIndex;
        }

        public Integer getId() {
            return id;
        }

        public Integer getHeadLength() {
            return headLength;
        }

        public Integer getContentLength() {
            return contentLength;
        }

        public Integer getEncryption() {
            return encryption;
        }

        public Boolean getSubcontract() {
            return subcontract;
        }

        public String getPhone() {
            return phone;
        }

        public Integer getNumber() {
            return number;
        }

        public Integer getSubcontractLength() {
            return subcontractLength;
        }

        public Integer getSubcontractIndex() {
            return subcontractIndex;
        }

        /**
         * 编码操作
         * @return 编码后的字节码
         */
        public byte[] toBytes() {
            ByteArrayOutputStream outputStream = null;
            try {
                // 创建输出流对象
                outputStream = new ByteArrayOutputStream();
                // 写入消息的 ID
                outputStream.write(JtUtils.integerToHigh8Low8(this.id));
                // 消息属性的二进制字符串
                final String attribute
                        = "00" // 留空
                        + (this.subcontract ? "1" : "0") // 是否分包
                        + JtUtils.integerToBinaryString(this.encryption, 3) // 数据加密方式
                        + JtUtils.integerToBinaryString(this.contentLength, 10); // 消息体长度

                // 写入消息的属性的字节码数据
                outputStream.write(JtUtils.binaryToByte(attribute.substring(0, 8)));
                outputStream.write(JtUtils.binaryToByte(attribute.substring(8, 16)));

                // 写入手机号码的字节码数据
                outputStream.write(JtUtils.codeTo8421Bytes(this.phone));
                // 写入消息的流水号
                outputStream.write(JtUtils.integerToHigh8Low8(this.number));
                // 判断是否分包
                if (this.subcontract) {
                    // 写入分包的字节码数据
                    outputStream.write(JtUtils.integerToHigh8Low8(this.subcontractLength));
                    outputStream.write(JtUtils.integerToHigh8Low8(this.subcontractIndex));
                }
                return outputStream.toByteArray();
            } catch (Exception e) {
                e.printStackTrace();
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
                    + "\"id\":"
                    + id
                    + ",\"headLength\":"
                    + headLength
                    + ",\"contentLength\":"
                    + contentLength
                    + ",\"encryption\":"
                    + encryption
                    + ",\"subcontract\":"
                    + subcontract
                    + ",\"phone\":\""
                    + phone + '\"'
                    + ",\"number\":"
                    + number
                    + ",\"subcontractLength\":"
                    + subcontractLength
                    + ",\"subcontractIndex\":"
                    + subcontractIndex
                    + "}";
        }
    }
}
