package top.wteng.mqttbootintegration.util;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import top.wteng.mqttbootintegration.entity.HandlerCache;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

@Component
public class DispatchUtil {
    private final Logger logger = LoggerFactory.getLogger(DispatchUtil.class);
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Async // 实现异步
    public void dispatchMessage(String topic, String payload) {
        HandlerCache hc = HandlerBeanUtil.getHandlerMethod(topic);
        if (hc == null) {
            logger.warn(String.format("not find handler method for topic %s ...", topic));
            return;
        }
        try {
            Class<?> type = hc.getConvertType();
            if (type.getTypeName().equals("java.lang.String")) {
                hc.getHandlerMethod().invoke(hc.getHandlerBean(), topic, payload);
            } else {
                Object obj = JSON.parseObject(payload, type);
                Set<ConstraintViolation<Object>> constraintViolations = validator.validate(obj);
                if (constraintViolations.size() > 0) {
                    logger.warn("validation errored on " + type.getName());
                    for (ConstraintViolation<Object> validation: constraintViolations) {
                        logger.warn(validation.getMessage());
                    }
                } else {
                    hc.getHandlerMethod().invoke(hc.getHandlerBean(), topic, obj);
                }
            }
        } catch (InvocationTargetException e) {
            logger.warn(String.format("invoke handler method for topic %s failed ...", topic));
        } catch (IllegalAccessException e) {
            logger.warn(String.format("access handler method for topic %s failed ...", topic));
        }
    }
}
