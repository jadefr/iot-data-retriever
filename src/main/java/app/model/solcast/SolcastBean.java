package app.model.solcast;

import java.util.List;

public class SolcastBean {

    private List<Measurement> measurements;

    public List<Measurement> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(List<Measurement> measurements) {
        this.measurements = measurements;
    }

    public static String latitude;

    public static String longitude;


    /*public SolcastBean(String latitude, String longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }*/
}
