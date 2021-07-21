package top.wteng.mqttbootintegration.util;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import top.wteng.mqttbootintegration.entity.HandlerCache;

import java.lang.reflect.InvocationTargetException;

@Component
public class DispatchUtil {
    private final Logger logger = LoggerFactory.getLogger(DispatchUtil.class);

    @Async
    public void dispatchMessage(String topic, String payload) {
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
