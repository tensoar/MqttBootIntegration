package top.wteng.mqttbootintegration.entity;


import java.lang.reflect.Method;

public class HandlerCache {
    private Object handlerBean;
    private Method handlerMethod;
    private String messagePattern;
    private Class<?> convertType;
    public HandlerCache(String messagePattern, Object handlerBean, Method handlerMethod, Class<?> convertType) {
        this.messagePattern = messagePattern;
        this.handlerBean = handlerBean;
        this.handlerMethod = handlerMethod;
        this.convertType = convertType;
    }
    public Object getHandlerBean() {
        return handlerBean;
    }

    public void setHandlerBean(Object handlerBean) {
        this.handlerBean = handlerBean;
    }

    public Method getHandlerMethod() {
        return handlerMethod;
    }

    public void setHandlerMethod(Method handlerMethod) {
        this.handlerMethod = handlerMethod;
    }

    public String getMessagePattern() {
        return messagePattern;
    }

    public void setMessagePattern(String messagePattern) {
        this.messagePattern = messagePattern;
    }

    public Class<?> getConvertType() {
        return convertType;
    }

    public void setConvertType(Class<?> convertType) {
        this.convertType = convertType;
    }
}
