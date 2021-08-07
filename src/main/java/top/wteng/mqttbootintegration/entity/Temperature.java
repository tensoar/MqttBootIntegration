package top.wteng.mqttbootintegration.entity;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class Temperature {
    @NotNull
    @Positive(message = "id必须为正整数")
    private Integer id;

    @NotNull
    @Min(value = -100, message = "Temperature::温度下限不能低于-100")
    @Max(value = 100, message = "Temperature::温度上限不能高于100")
    private float value;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Temperature{" +
                "id=" + id +
                ", value=" + value +
                '}';
    }
}
