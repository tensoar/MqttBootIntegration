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
            // 不是通配订阅
            return pattern.equals(topic);
        }
        // 订阅中含有通配符
        String[] patternSegments = pattern.split(SEPARATOR);
        String[] topicSegments = topic.split(SEPARATOR);
        for (int i = 0; i < patternSegments.length; i ++) {
            // 对各个主题层级进行匹配
            String curPatternSeg = patternSegments[i];
            String curTopicSeg = topicSegments.length > i ? topicSegments[i]: null;
            if (curTopicSeg == null && !curPatternSeg.equals(WILDCARD_ALL)) {
                // 主题层级比订阅层级少且相应的订阅层级不是#
                return false;
            }
            if ("".equals(curTopicSeg) && "".equals(curPatternSeg)) {
                // 可能以 / 开头
                continue;
            }
            if (curPatternSeg.equals(WILDCARD_ALL)) {
                // 是#通配，则#必须是最后一级
                return i == patternSegments.length - 1;
            }
            // 当前层级不是通配，需要字符串相等
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
