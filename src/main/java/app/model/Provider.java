package app.model;

import java.util.List;

public class Provider {

    private final static String TYPE_SOLCAST = "SOLCAST";

    private final static String TYPE_DARKSKY = "DARKSKY";

    public String type;

    private List<Measurement> measurements;

    public static String getTypeSolcast() {
        return TYPE_SOLCAST;
    }

    public static String getTypeDarksky() {
        return TYPE_DARKSKY;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Measurement> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(List<Measurement> measurements) {
        this.measurements = measurements;
    }

  }
