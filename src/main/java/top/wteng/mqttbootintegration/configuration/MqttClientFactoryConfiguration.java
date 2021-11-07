package top.wteng.mqttbootintegration.configuration;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;

@Configuration
public class MqttClientFactoryConfiguration {
    @Autowired private MqttConfiguration mqttConfiguration;

    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        options.setConnectionTimeout(0);
        options.setKeepAliveInterval(90);
        options.setServerURIs(new String[]{mqttConfiguration.getUrl()});
        if (mqttConfiguration.getUsername() != null) {
            options.setUserName(mqttConfiguration.getUsername());
        }
        if (mqttConfiguration.getPassword() != null) {
            options.setPassword(mqttConfiguration.getPassword().toCharArray());
        }
        factory.setConnectionOptions(options);
        return factory;
    }
}
