package top.wteng.mqttbootintegration.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.mqtt")
public class MqttConfiguration {
    private String username;
    private String password;
    private String url;
    private String clientId = "MqttBootIntegration_" + System.currentTimeMillis();
    private String defaultTopic = "unknown";
    private Integer defaultQos = 0;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getDefaultTopic() {
        return defaultTopic;
    }

    public void setDefaultTopic(String defaultTopic) {
        this.defaultTopic = defaultTopic;
    }

    public Integer getDefaultQos() {
        return defaultQos;
    }

    public void setDefaultQos(Integer defaultQos) {
        this.defaultQos = defaultQos;
    }

    @Override
    public String toString() {
        return "MqttConfiguration{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", url='" + url + '\'' +
                ", clientId='" + clientId + '\'' +
                ", defaultTopic='" + defaultTopic + '\'' +
                ", defaultQos=" + defaultQos +
                '}';
    }
}
