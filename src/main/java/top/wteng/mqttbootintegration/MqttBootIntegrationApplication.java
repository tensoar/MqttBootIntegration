package top.wteng.mqttbootintegration;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.scheduling.annotation.EnableAsync;

import top.wteng.mqttbootintegration.util.DispatchUtil;
import top.wteng.mqttbootintegration.util.HandlerBeanUtil;

@SpringBootApplication
@EnableAsync // 开启异步
public class MqttBootIntegrationApplication implements CommandLineRunner{
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired private ApplicationContext applicationContext;
	@Autowired private DispatchUtil dispatchUtil;
	@Value("${mqtt.host}") private String mqttHost;
	@Value("${mqtt.port}") private String mqttPort;

	public static void main(String[] args) {
		SpringApplication.run(MqttBootIntegrationApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("MqttBootIntegrationApplication is running ...");
	}

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
		String mqttUrl = String.format("tcp://%s:%s", mqttHost, mqttPort);
		String clientId = UUID.randomUUID().toString();
		MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(mqttUrl, clientId, mpArr);
		logger.info(String.format("connected to mqtt %s with client id %s ...", mqttUrl, clientId));
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