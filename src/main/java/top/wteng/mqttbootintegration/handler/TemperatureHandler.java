package top.wteng.mqttbootintegration.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import top.wteng.mqttbootintegration.annotation.Handler;
import top.wteng.mqttbootintegration.annotation.MessagePattern;
import top.wteng.mqttbootintegration.entity.Temperature;

@Handler
@Component
public class TemperatureHandler {
    private final Logger logger = LoggerFactory.getLogger(TemperatureHandler.class);

    @MessagePattern(value = "temperature")
    public void temperatureHandler(String topic, Temperature temperature) throws InterruptedException {
        logger.debug(String.format(
                "temperature = %s", temperature.toString()
        ));
    }

}
