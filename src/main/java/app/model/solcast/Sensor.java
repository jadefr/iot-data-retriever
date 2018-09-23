package app.model.solcast;

public class Sensor {

    private String name;
    private SensorProperty operatingProperty;
    private SensorProperty operatingRange; // ambos SensorProperty e OperatingRange tem os mesmos atributos

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SensorProperty getOperatingProperty() {
        return operatingProperty;
    }

    public void setOperatingProperty(SensorProperty sensorProperty) {
        this.operatingProperty = sensorProperty;
    }

    public SensorProperty getOperatingRange() {
        return operatingRange;
    }

    public void setOperatingRange(SensorProperty operatingRange) {
        this.operatingRange = operatingRange;
    }
}
