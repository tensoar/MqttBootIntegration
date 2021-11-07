package top.wteng.mqttbootintegration.gateway;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
public interface MqttGateway {
    void sendToMqtt(@Header(value = MqttHeaders.TOPIC) String topic, String payload);
    void sendToMqtt(@Header(value = MqttHeaders.TOPIC) String topic, byte[] payload);

    void sendToMqtt(@Header(value = MqttHeaders.TOPIC) String topic, @Header(value = MqttHeaders.QOS) int qos, String payload);
    void sendToMqtt(@Header(value = MqttHeaders.TOPIC) String topic, @Header(value = MqttHeaders.QOS) int qos, byte[] payload);
}

