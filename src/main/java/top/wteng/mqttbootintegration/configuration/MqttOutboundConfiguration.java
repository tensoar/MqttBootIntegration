package top.wteng.mqttbootintegration.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import java.util.UUID;

@Configuration
public class MqttOutboundConfiguration {
    @Autowired private MqttConfiguration mqttConfiguration;
    @Autowired private MqttClientFactoryConfiguration mqttClientFactoryConfiguration;

    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound() {
        MqttPahoMessageHandler handler = new MqttPahoMessageHandler(UUID.randomUUID().toString(), mqttClientFactoryConfiguration.mqttClientFactory());
        handler.setAsync(true);
        handler.setDefaultTopic(mqttConfiguration.getDefaultTopic());
        handler.setDefaultQos(mqttConfiguration.getDefaultQos());
        return handler;
    }
}
