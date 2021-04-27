package com.lidashuang.jt.actuator;

import com.lidashuang.jt.JttActuator;
import com.lidashuang.jt.JttContext;
import com.lidashuang.jt.JttMessage;
import com.lidashuang.jt.JttUtils;
import com.lidashuang.jt.jtt808.Jtt808T5;
import com.lidashuang.jt.jtt808.Jtt808T6;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 终端注册的应答
 * @author lidashuang
 * @version 1.0
 */
public class Jtt808T5Actuator implements JttActuator {

    /** 注入日志对象 */
    private static final Logger LOGGER = LoggerFactory.getLogger(Jtt808T5Actuator.class);

    @Override
    public void execute(JttContext context, JttMessage message) {
        try {
            LOGGER.info("[ 接收到「注册」] ==> " + message.toString());
            final Jtt808T5 jtt808T5 = (Jtt808T5) message;
            // 省域 ID
            final int province = jtt808T5.getProvince();
            // 市县域 ID
            final int city = jtt808T5.getCity();
            // 制造商 ID
            final String manufacturer = JttUtils.bytesToHex(jtt808T5.getManufacturer());
            // 终端型号
            final String terminalModel = JttUtils.bytesToHex(jtt808T5.getTerminalModel());
            // 终端 ID
            final String terminalId = JttUtils.bytesToHex(jtt808T5.getTerminalId());
            // 车牌颜色
            final int licensePlateColor = jtt808T5.getLicensePlateColor();
            // 车辆标识
            final String vehicleMark = jtt808T5.getVehicleMark();
            // 注册信息写入到上下文对象中
            context.setAttribute("register_province", province);
            context.setAttribute("register_city", city);
            context.setAttribute("register_manufacturer", manufacturer);
            context.setAttribute("register_terminalModel", terminalModel);
            context.setAttribute("register_terminalId", terminalId);
            context.setAttribute("register_licensePlateColor", licensePlateColor);
            context.setAttribute("register_vehicleMark", vehicleMark);
            final Jtt808T6 jtt808T6 = new Jtt808T6(
                    message.getHeader().getNumber(), 0, JttUtils.generateUuid(12));
            context.sendMessage(jtt808T6);
            LOGGER.info("[ 应答到「注册」 ] ==> " + jtt808T6);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
