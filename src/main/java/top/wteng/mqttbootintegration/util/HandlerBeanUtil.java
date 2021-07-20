package top.wteng.mqttbootintegration.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.regex.Pattern;

import org.springframework.context.ApplicationContext;

import top.wteng.mqttbootintegration.annotation.Handler;
import top.wteng.mqttbootintegration.annotation.MessagePattern;

public class HandlerBeanUtil {
    private static final Map<String, Method> messageHandler = new HashMap<>();
    
    public static List<String> getMessagePatterns(ApplicationContext context) {
        return Arrays.asList(context.getBeanNamesForAnnotation(Handler.class))
                                .parallelStream()
                                .map(beanName -> context.getBean(beanName).getClass())
                                .flatMap(handerClass -> Arrays.asList(handerClass.getDeclaredMethods()).stream())
                                .peek(method -> {
                                    MessagePattern mp = method.getDeclaredAnnotation(MessagePattern.class);
                                    if (mp != null && !messageHandler.containsKey(mp.value())) {
                                        messageHandler.put(mp.value(), method);
                                    }
                                })
                                .map(method -> method.getDeclaredAnnotation(MessagePattern.class))
                                .filter(mp -> mp != null)
                                .map(mp -> mp.value())
                                .collect(Collectors.toList());
    }

    public static Method getHandlerMethod(String topic) {
        String mKey = messageHandler.keySet().stream()
                        .filter(key -> Pattern.matches(key, topic)).findFirst().orElse(null);
        if (mKey == null) {
            return null;
        }
        return messageHandler.get(mKey);
    }
}
