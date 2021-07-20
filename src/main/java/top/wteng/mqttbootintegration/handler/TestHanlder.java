package top.wteng.mqttbootintegration.handler;

import org.springframework.stereotype.Component;

import top.wteng.mqttbootintegration.annotation.Handler;
import top.wteng.mqttbootintegration.annotation.MessagePattern;

@Handler
@Component
public class TestHanlder {
    
    @MessagePattern("meas")
    public void testHander(String topic, Object payload) {
        System.out.println("topic = " + topic);
        System.out.println("payload = " + payload.toString());
    }
}
