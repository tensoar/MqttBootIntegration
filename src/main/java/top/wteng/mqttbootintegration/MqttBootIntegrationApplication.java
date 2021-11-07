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

	public static void main(String[] args) {
		SpringApplication.run(MqttBootIntegrationApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		logger.info("MqttBootIntegrationApplication is running ...");
	}
}