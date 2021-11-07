package top.wteng.mqttbootintegration.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import top.wteng.mqttbootintegration.util.DispatchUtil;
import top.wteng.mqttbootintegration.util.HandlerBeanUtil;

import java.util.List;
import java.util.UUID;

@Configuration
public class MqttInboundConfiguration {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired private ApplicationContext applicationContext;
    @Autowired private DispatchUtil dispatchUtil;
    @Autowired private MqttClientFactoryConfiguration mqttClientFactoryConfiguration;
    @Autowired private MqttConfiguration mqttConfiguration;

    @Bean
    // mqtt接收频道
    public MessageChannel mqttReceiverChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageProducer mqttReceiver() {
        // 取出所有主题进行订阅
        List<String> mps = HandlerBeanUtil.getAndCacheMessagePatterns(applicationContext);
        String[] mpArr = mps.toArray(new String[0]);
//        String mqttUrl = mqttConfiguration.getUrl();
        String clientId = mqttConfiguration.getClientId();
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(clientId, mqttClientFactoryConfiguration.mqttClientFactory(), mpArr);
//        logger.info(String.format("connected to mqtt %s with client id %s ...", mqttUrl, clientId));
        logger.info(String.format("topic subscribed: %s", String.join(",", mpArr)));
        adapter.setOutputChannel(mqttReceiverChannel());
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttReceiverChannel")
    // 绑定了mqtt接收频道，收到消息后会触发此函数
    public MessageHandler messageHandler() {
        // 分发消息,异步处理
        return message ->
                dispatchUtil.dispatchMessage((String) message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC), (String)message.getPayload());
    }
}
