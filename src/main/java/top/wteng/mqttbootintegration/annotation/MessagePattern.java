package top.wteng.mqttbootintegration.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MessagePattern {
    // 主题,可以用通配符,应用启动时会取出该值进行订阅,收到消息时也会依据此值与主题进行匹配,确定处理函数
    String value();
}
