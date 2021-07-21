package top.wteng.mqttbootintegration;

import java.lang.reflect.InvocationTargetException;
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
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.scheduling.annotation.EnableAsync;
import com.alibaba.fastjson.JSON;

import top.wteng.mqttbootintegration.entity.HandlerCache;
import top.wteng.mqttbootintegration.util.HandlerBeanUtil;

@SpringBootApplication
@EnableAsync
public class MqttBootIntegrationApplication implements CommandLineRunner{
	 private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired public ApplicationContext applicationContext;
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
	public MessageHandler messageHandler() {
		return message ->
				dispatchMessage((String) message.getHeaders().get("mqtt_receivedTopic"), (String)message.getPayload());
	}

	private void dispatchMessage(String topic, String payload) {
		HandlerCache hc = HandlerBeanUtil.getHandlerMethod(topic);
		if (hc == null) {
			logger.warn(String.format("not find handler method for topic %s ...", topic));
			return;
		}
		try {
			Class<?> type = hc.getConvertType();
			hc.getHandlerMethod().invoke(hc.getHandlerBean(), topic, type.getTypeName().equals("java.lang.String") ? payload: JSON.parseObject(payload, type));
		} catch (InvocationTargetException e) {
			logger.warn(String.format("invoke handler method for topic %s failed ...", topic));
		} catch (IllegalAccessException e) {
			logger.warn(String.format("access handler method for topic %s failed ...", topic));
		}
	}
}
