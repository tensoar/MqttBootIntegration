package top.wteng.mqttbootintegration;

import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.wteng.mqttbootintegration.entity.Temperature;
import top.wteng.mqttbootintegration.gateway.MqttMessageSender;

@SpringBootTest
public class SendMqttMessageTests {
    @Autowired private MqttMessageSender messageSender;

    @Test
    public void sendSomeMessage() {
        String topic = "temperature";
        for (int i = 0; i < 15; i ++) {
            Temperature temperature = new Temperature();
            temperature.setId(i);
            temperature.setValue(i * 10);
            messageSender.sendToMqtt(topic, JSON.toJSONBytes(temperature));
        }
    }
}
