package com.lidashuang.jt.jt808;

import com.lidashuang.jt.JtMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * @author lidashuang
 * @version 1.0
 */
public class Jt808T0 extends JtMessage {

    private static final Logger LOGGER = LoggerFactory.getLogger(Jt808T0.class);

    public Jt808T0() {
    }

    public Jt808T0(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public void decode() {
    }

    @Override
    public byte[] encode() {
        return bytes;
    }

}
