package top.wteng.mqttbootintegration.entity;


import java.lang.reflect.Method;

public class HandlerCache {
    private Object handlerBean; // 处理函数所在的对象引用
    private Method handlerMethod; // 处理函数的反射
    private String messagePattern; // 主题，@MessagePattern注解中的值
    private Class<?> convertType; // 消息体对应的实体类型，即处理函数第二个参数的类型
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
