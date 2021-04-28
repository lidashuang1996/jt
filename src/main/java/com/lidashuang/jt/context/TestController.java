package com.lidashuang.jt.context;

import com.lidashuang.jt.JttContext;
import com.lidashuang.jt.JttManage;
import com.lidashuang.jt.jtt1078.Jtt1078T3;
import com.lidashuang.jt.jtt808.Jtt808T0;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author lidashuang
 * @version 1.0
 */
@RestController
@RequestMapping("/")
public class TestController {

    @RequestMapping("/list")
    public Object test() {
        try {
            final List<Map<String, Object>> r = new ArrayList<>();
            for (JttContext context : JttManage.getContextList()) {
                r.add(context.getAttribute());
            }
            return r;
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    @RequestMapping("/video/{index}")
    public Object video(@PathVariable String index) {
        try {
            final JttContext context = JttManage.getContextList().get(Integer.parseInt(index));
            final Jtt1078T3 jtt1078T3 = new Jtt1078T3(
                    14,
                    "139.159.216.99",
                    9777,
                    9888,
                    1,
                    0,
                    0
            );
            context.sendMessage(jtt1078T3);
            return jtt1078T3;
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }



    @RequestMapping("/push/{index}")
    public Object push(@PathVariable String index, @RequestBody ParamModel model) {
        ByteArrayOutputStream outputStream = null;
        try {
            final JttContext context = JttManage.getContextList().get(Integer.parseInt(index));
            outputStream = new ByteArrayOutputStream();
            for (String s : model.getContent().split(",")) {
                outputStream.write((byte) Integer.parseInt(s));
            }
            context.sendMessage(new Jtt808T0(outputStream.toByteArray()));
            return model;
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
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

}
