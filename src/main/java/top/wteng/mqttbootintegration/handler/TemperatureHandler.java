package top.wteng.mqttbootintegration.handler;

import org.springframework.stereotype.Component;

import top.wteng.mqttbootintegration.annotation.Handler;
import top.wteng.mqttbootintegration.annotation.MessagePattern;
import top.wteng.mqttbootintegration.entity.Temperature;

@Handler
@Component
public class TemperatureHandler {

    @MessagePattern(value = "temperature")
    public void temperatureHandler(String topic, Temperature temperature) throws InterruptedException {
        System.out.printf("thread id: %d, temperature = %s%n", Thread.currentThread().getId(), temperature.toString());
    }

}
