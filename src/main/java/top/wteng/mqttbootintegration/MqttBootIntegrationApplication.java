package top.wteng.mqttbootintegration;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
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

import top.wteng.mqttbootintegration.annotation.Handler;

@SpringBootApplication
@EnableAsync
public class MqttBootIntegrationApplication implements CommandLineRunner{
	// private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired public ApplicationContext applicationContext;
	private int count = 0;

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
		
		String [] beanNames = applicationContext.getBeanNamesForAnnotation(Handler.class);
		System.out.println("beanames = " + String.join(",", beanNames));
		MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
			"tcp://114.115.140.5", UUID.randomUUID().toString(), "data/gpb/+/eemeas"
		);
		adapter.setOutputChannel(mqttReceiverChannel());
		return adapter;
	}

	@Bean
	@ServiceActivator(inputChannel = "mqttReceiverChannel")
	public MessageHandler messageHandler() {
		return message -> handleMessage(message.getPayload());
	}

	private void handleMessage(Object payload) {
		if (count > 0) {
			return;
		}
		count += 1;
		// System.out.println("payload received: " + payload.toString());
	}
}
