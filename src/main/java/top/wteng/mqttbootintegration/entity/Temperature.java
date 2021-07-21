package top.wteng.mqttbootintegration.entity;

public class Temperature {
    private Integer id;
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
