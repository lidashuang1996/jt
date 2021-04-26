package com.lidashuang.jt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * JTT 头部消息
 * @author lidashuang
 * @version 1.0
 */
public class JttMessageHeader extends JttMessage {

    /** 日志对象 */
    private final static Logger LOGGER = LoggerFactory.getLogger(JttMessageHeader.class);

    /** 最小消息长度 */
    private final static int MIN_SIZE = 12;
    /** 最大消息长度 */
    private final static int MAX_SIZE = 16;
    /** 消息的 ID <类型> */
    private Integer id;
    /** 头长度 */
    private Integer headLength;
    /** 内容长度 */
    private Integer contentLength;
    /** 加密类型 */
    private Integer encryption;
    /** 是否分包 */
    private Boolean subcontract;
    /** 手机号码 */
    private String phone;
    /** 消息的流水号 */
    private Integer number;
    /** 分包的总数 */
    private Integer subcontractLength;
    /** 分包的索引 */
    private Integer subcontractIndex;

    public JttMessageHeader() { }

    public JttMessageHeader(Integer encryption, String phone, Integer number) {
        this.encryption = encryption;
        this.phone = phone;
        this.number = number;
    }

    public JttMessageHeader(Integer id, Integer headLength, Integer contentLength,
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

    @Override
    public byte[] encode() {
        ByteArrayOutputStream outputStream = null;
        try {
            // 创建输出流对象
            outputStream = new ByteArrayOutputStream();
            // 写入消息的 ID
            outputStream.write(JttUtils.integerToHigh8Low8(this.id));
            // 消息属性的二进制字符串
            final String attribute
                    = "00" // 留空
                    + (this.subcontract ? "1" : "0") // 是否分包
                    + JttUtils.integerToBinaryString(this.encryption, 3) // 数据加密方式
                    + JttUtils.integerToBinaryString(this.contentLength, 10); // 消息体长度

            // 写入消息的属性的字节码数据
            outputStream.write(JttUtils.binaryToByte(attribute.substring(0, 8)));
            outputStream.write(JttUtils.binaryToByte(attribute.substring(8, 16)));

            // 写入手机号码的字节码数据
            outputStream.write(JttUtils.codeTo8421Bytes(this.phone));
            // 写入消息的流水号
            outputStream.write(JttUtils.integerToHigh8Low8(this.number));
            // 判断是否分包
            if (this.subcontract) {
                // 写入分包的字节码数据
                outputStream.write(JttUtils.integerToHigh8Low8(this.subcontractLength));
                outputStream.write(JttUtils.integerToHigh8Low8(this.subcontractIndex));
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
    public JttMessageHeader decode(byte[] bytes) {
        if (bytes.length >= MIN_SIZE) {
            // 消息的类型
            final int id = JttUtils.bytesToHigh8Low8(JttUtils.bytesArrayIntercept(bytes, 0, 2));
            // 消息的加密类型
            final int encryption = (bytes[2] >> 2) & 0x07;
            // 消息是否分包
            final boolean subcontract = (((bytes[2] >> 5) & 0x01) == 1);
            // 头部消息的长度
            final int headLength = (subcontract ? 16 : 12);
            // 内容消息的长度
            // 消息长度占 8 + 2 比特，最大表示长度为 2 ^ 10 = 1024
            final int contentLength = JttUtils.bytesToHigh8Low8(new byte[] { (byte) (bytes[2] & 0x03), bytes[3] });
            // 获取手机号码
            final String phone = JttUtils.bytesTo8421Code(JttUtils.bytesArrayIntercept(bytes, 4, 6));
            final int number = JttUtils.bytesToHigh8Low8(JttUtils.bytesArrayIntercept(bytes, 10, 2));
            // 分包的索引
            int subcontractIndex = 0;
            // 分包的长度
            int subcontractLength = 0;
            // 写入分包数据
            if (subcontract) {
                if (bytes.length >= MAX_SIZE) {
                    subcontractIndex = JttUtils.bytesToHigh8Low8(JttUtils.bytesArrayIntercept(bytes, 14, 2));
                    subcontractLength = JttUtils.bytesToHigh8Low8(JttUtils.bytesArrayIntercept(bytes, 12, 2));
                } else {
                    LOGGER.info("解码消息 HEADER 分包消息长度不够 ～");
                    throw new RuntimeException("解码消息 HEADER 分包消息长度不够 ～");
                }
            }
            this.subcontractIndex = subcontractIndex;
            this.subcontractLength = subcontractLength;
            return new JttMessageHeader(id, headLength, contentLength, encryption,
                    subcontract, phone, number, subcontractLength, subcontractIndex);
        } else {
            LOGGER.info("解码消息 HEADER 消息长度不够 ～");
            throw new RuntimeException("解码消息 HEADER 消息长度不够 ～");
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getHeadLength() {
        return headLength;
    }

    public void setHeadLength(Integer headLength) {
        this.headLength = headLength;
    }

    public Integer getContentLength() {
        return contentLength;
    }

    public void setContentLength(Integer contentLength) {
        this.contentLength = contentLength;
    }

    public Integer getEncryption() {
        return encryption;
    }

    public void setEncryption(Integer encryption) {
        this.encryption = encryption;
    }

    public Boolean getSubcontract() {
        return subcontract;
    }

    public void setSubcontract(Boolean subcontract) {
        this.subcontract = subcontract;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getSubcontractLength() {
        return subcontractLength;
    }

    public void setSubcontractLength(Integer subcontractLength) {
        this.subcontractLength = subcontractLength;
    }

    public Integer getSubcontractIndex() {
        return subcontractIndex;
    }

    public void setSubcontractIndex(Integer subcontractIndex) {
        this.subcontractIndex = subcontractIndex;
    }

    @Override
    public int getMid() {
        return 0;
    }
}
