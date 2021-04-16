package com.lidashuang.jt.jt808;

import com.lidashuang.jt.JtMessage;

import java.util.Arrays;

/**
 * @author lidashuang
 * @version 1.0
 *
 * 数据格式
 * 终端注销
 * 消息 ID：0x0003
 * 终端注销消息体为空
 */
public class Jt808T7 extends JtMessage {
    /** 消息 ID：0x0003 */
    public static final int M_ID = 0x0003;

    @Override
    public int getType() {
        return M_ID;
    }

    @Override
    public JtMessage decode(byte[] bytes) {
        try {
            this.bytes = bytes;
            this.header = new Header(this.bytes);
            final int cLength = this.header.getContentLength();
            if (cLength == 0) {
                throw new Exception("消息内容理论为空，现在存在内容, 内容为 ==> " + Arrays.toString(this.bytes));
            }
        } catch (Exception e) {
            // 抛出异常，终止执行
            throw new RuntimeException(e);
        }
        return this;
    }

    @Override
    public byte[] encode() {
        // 以后处理 客户端需要
        return new byte[0];
    }

}
