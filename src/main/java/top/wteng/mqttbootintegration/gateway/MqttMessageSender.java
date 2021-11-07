package top.wteng.mqttbootintegration.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MqttMessageSender {
    @Autowired private MqttGateway mqttGateway;

    public void sendToMqtt(String topic, String payload) {
        this.mqttGateway.sendToMqtt(topic, payload);
    }
    public void sendToMqtt(String topic, byte[] payload) {
        this.mqttGateway.sendToMqtt(topic, payload);
    }

    public void sendToMqtt(String topic, int qos, String payload) {
        this.mqttGateway.sendToMqtt(topic, qos, payload);
    }
    public void sendToMqtt(String topic, int qos, byte[] payload) {
        this.mqttGateway.sendToMqtt(topic, qos, payload);
    }
}
