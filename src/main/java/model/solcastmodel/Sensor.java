package model.solcastmodel;

import java.util.List;

public class Sensor {

    private String name;
    private List<OperatingProperty> operatingProperty;
    private List<OperatingProperty> operatingRange; // ambos OperatingProperty e OperatingRange tem os mesmos atributos

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<OperatingProperty> getOperatingProperty() {
        return operatingProperty;
    }

    public void setOperatingProperty(List<OperatingProperty> operatingProperty) {
        this.operatingProperty = operatingProperty;
    }

    public List<OperatingProperty> getOperatingRange() {
        return operatingRange;
    }

    public void setOperatingRange(List<OperatingProperty> operatingRange) {
        this.operatingRange = operatingRange;
    }
}
