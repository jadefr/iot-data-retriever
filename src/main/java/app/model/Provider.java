package app.model;

import java.util.List;

public class Provider {

    final static String TYPE_SOLCAST = "SOLCAST";

    final static String TYPE_DARKSKY = "DARKSKY";

    private List<Measurement> measurements;

    public void setType(String type){

    }

    public List<Measurement> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(List<Measurement> measurements) {
        this.measurements = measurements;
    }

  }
