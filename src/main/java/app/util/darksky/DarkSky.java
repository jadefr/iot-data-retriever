package app.util.darksky;

public class DarkSky {

    private String latitude;
    private String longitude;
    private String timezone;
    private Currently currently;
    private Hourly hourly;

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getTimezone() {
        return timezone;
    }

    public Currently getCurrently() {
        return currently;
    }

    public Hourly getHourly() {
        return hourly;
    }

}
