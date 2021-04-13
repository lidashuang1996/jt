package com.lidashuang.jt;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author lidashuang
 * @version 1.0
 */
public abstract class JtMessage  {

    public static class HeadMessage {
        private final Integer id;
        private final Integer headLength;
        private final Integer contentLength;
        private final Integer encryption;
        private final Boolean subcontract;
        private final String phone;
        private final Integer number;
        private final Integer subcontractLength;
        private final Integer subcontractIndex;

        public HeadMessage(Integer id, Integer headLength, Integer contentLength,
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

        public byte[] toBytes() {
            ByteArrayOutputStream outputStream = null;
            try {
                outputStream = new ByteArrayOutputStream();
                outputStream.write(Utils.integerToHigh8Low8(this.id));

                final String attribute = "00" + (subcontract ? 1 : 0)
                        + Utils.integerToBinaryString(encryption, 3)
                        + Utils.integerToBinaryString(contentLength, 10);

                outputStream.write(Utils.binaryToByte(attribute.substring(0, 8)));
                outputStream.write(Utils.binaryToByte(attribute.substring(8, 16)));

                outputStream.write(Utils.codeTo8421Bytes(this.phone));
                outputStream.write(Utils.integerToHigh8Low8(this.number));
                if (subcontract) {
                    outputStream.write(Utils.integerToHigh8Low8(this.subcontractLength));
                    outputStream.write(Utils.integerToHigh8Low8(this.subcontractIndex));
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

    /** 消息源字节码数据 */
    protected byte[] bytes;

    /** 消息头 */
    protected HeadMessage headMessage;

    /**
     * 构造输入消息源字节码数据
     */
    public JtMessage() {}

    /**
     * 构造输入消息源字节码数据
     * @param bytes 消息源字节码数据
     */
    public JtMessage(byte[] bytes) {
        this.bytes = bytes;
        this.headMessage = headDecode();
        this.decode();
    }

    /**
     * 解码为 [消息头 + 消息内容]
     */
    public abstract JtMessage decode();

    /**
     * 编码内容
     * @return 编码后内容的字节码
     */
    public abstract byte[] encode();

    public HeadMessage headDecode() {
        if (bytes.length >= 12) {
            final int encryption = bytes[2] >> 2 & 0x07;
            final boolean subcontract = (bytes[2] >> 5 & 0x01) == 1;
            final String phone = Utils.byteTo8421Code(bytes[4])
                    + Utils.byteTo8421Code(bytes[5])
                    + Utils.byteTo8421Code(bytes[6])
                    + Utils.byteTo8421Code(bytes[7])
                    + Utils.byteTo8421Code(bytes[8])
                    + Utils.byteTo8421Code(bytes[9]);
            final int headLength = (subcontract ? 16 : 12);
            final int contentLength = Utils.bytesToHigh8Low8(new byte[] {(byte) (bytes[2] & 0x01), bytes[3] });
            int subcontractIndex = 0;
            int subcontractLength = 0;
            if (subcontract) {
                if (bytes.length >= 16) {
                    subcontractIndex = Utils.bytesToHigh8Low8(new byte[] { bytes[14], bytes[15] });
                    subcontractLength = Utils.bytesToHigh8Low8(new byte[] { bytes[12], bytes[13] });
                } else {
                    throw new RuntimeException("header decode 失败！");
                }
            }

            if (contentLength + headLength != bytes.length) {
                throw new RuntimeException("header + content .length 不等于 bytes 的长度");
            }

            return new HeadMessage(
                    Utils.bytesToHigh8Low8(new byte[] { bytes[0], bytes[1] }),
                    headLength,
                    contentLength,
                    encryption,
                    subcontract,
                    phone,
                    Utils.bytesToHigh8Low8(new byte[] { bytes[10], bytes[11] }),
                    subcontractLength,
                    subcontractIndex
            );
        } else {
            throw new RuntimeException("header decode 失败！");
        }
    }

    public byte[] getBytes() {
        return bytes;
    }

    public HeadMessage getHeadMessage() {
        return headMessage;
    }
}
