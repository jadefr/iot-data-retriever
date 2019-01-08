package app.service.darksky;

import tk.plogitech.darksky.forecast.*;
import tk.plogitech.darksky.forecast.model.Latitude;
import tk.plogitech.darksky.forecast.model.Longitude;

public class DarkSkyAPIReading { //not used
    /*
    this class uses the Dark Sky client library to retrieve data from the API
     */

    public String getDarkSky() throws ForecastException {
        ForecastRequest request = new ForecastRequestBuilder()
                .key(new APIKey("687d8276befb3d1b35d918e6dedaa7f9"))
                .location(new GeoCoordinates(new Longitude(13.377704), new Latitude(52.516275))).build();

        DarkSkyClient client = new DarkSkyClient();
        String forecast = client.forecastJsonString(request);
        return forecast;
    }

}
