package top.wteng.mqttbootintegration.util;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationContext;

import top.wteng.mqttbootintegration.annotation.Handler;
import top.wteng.mqttbootintegration.annotation.MessagePattern;
import top.wteng.mqttbootintegration.entity.HandlerCache;

public class HandlerBeanUtil {
    private static final List<HandlerCache> messageHandlers = new ArrayList<>();
    private static final String WILDCARD_ALL = "#";
    private static final String WILDCARD_PART = "+";
    private static final String SEPARATOR = "/";
    
    public static List<String> getAndCacheMessagePatterns(ApplicationContext context) {
        return Arrays.asList(context.getBeanNamesForAnnotation(Handler.class))
                .parallelStream()
                .map(context::getBean)
                .flatMap(bean -> {
                    Class<?> handlerClass = bean.getClass();
                    List<HandlerCache> mhs = new ArrayList<>();
                    Method[] methods = handlerClass.getDeclaredMethods();
                    for (Method m: methods) {
                        MessagePattern mp = m.getDeclaredAnnotation(MessagePattern.class);
                        if (mp != null && !isMessageHandlersHas(mp.value())) {
                            Class<?>[] parameterTypes = m.getParameterTypes();
                            mhs.add(new HandlerCache(mp.value(), bean, m, parameterTypes.length > 1 ? parameterTypes[1] : String.class));
                        }
                    }
                    return mhs.stream();
                })
                .peek(messageHandlers::add)
                .map(HandlerCache::getMessagePattern)
                .collect(Collectors.toList());
    }

    private static boolean isMessageHandlersHas(String pattern) {
        return messageHandlers.stream()
                .filter(hc -> hc.getMessagePattern().equals(pattern))
                .findFirst()
                .orElse(null) != null;
    }

    private static boolean isMatchPattern(String pattern, String topic) {
        if (!pattern.contains(WILDCARD_ALL) && !pattern.contains(WILDCARD_PART)) {
            // ??????????????????
            return pattern.equals(topic);
        }
        // ????????????????????????
        String[] patternSegments = pattern.split(SEPARATOR);
        String[] topicSegments = pattern.split(SEPARATOR);
        for (int i = 0; i < patternSegments.length; i ++) {
            // ?????????????????????????????????
            String curPatternSeg = patternSegments[i];
            String curTopicSeg = topicSegments.length > i ? topicSegments[i]: null;
            if (curTopicSeg == null && !curPatternSeg.equals(WILDCARD_ALL)) {
                // ????????????????????????????????????????????????????????????#
                return false;
            }
            if ("".equals(curTopicSeg) && "".equals(curPatternSeg)) {
                // ????????? / ??????
                continue;
            }
            if (curPatternSeg.equals(WILDCARD_ALL)) {
                // ???#????????????#?????????????????????
                return i == patternSegments.length - 1;
            }
            // ????????????????????????????????????????????????
            if (!curPatternSeg.equals(WILDCARD_PART) && !curPatternSeg.equals(curTopicSeg)) {
                return false;
            }
        }
        return patternSegments.length == topicSegments.length;
    }

    public static HandlerCache getHandlerMethod(String topic) {
        return messageHandlers.stream()
                .filter(hc -> isMatchPattern(hc.getMessagePattern(), topic))
                .findFirst()
                .orElse(null);
    }
}
