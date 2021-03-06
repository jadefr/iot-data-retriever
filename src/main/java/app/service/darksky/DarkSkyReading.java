package app.service.darksky;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import tk.plogitech.darksky.forecast.*;
import tk.plogitech.darksky.forecast.model.Latitude;
import tk.plogitech.darksky.forecast.model.Longitude;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DarkSkyReading {

    private static String latitude;
    private static String longitude;

    public DarkSkyReading(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static DarkSky readDarkSky() throws IOException {

        //URL url = new URL("https://api.darksky.net/forecast/687d8276befb3d1b35d918e6dedaa7f9/-21.758819,-43.350500?lang=pt&units=si");
        URL url = new URL("https://api.darksky.net/forecast/687d8276befb3d1b35d918e6dedaa7f9/" + latitude + "," + longitude + "?lang=pt&units=si");
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        InputStream inputStream = httpURLConnection.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String data = "";
        while (line != null) {
            data = data + line;
            line = bufferedReader.readLine();
            //System.out.println("API DarkSky: " + data);
        }
        int size = data.length();

        Gson gson = new Gson();
        DarkSky darkSky = gson.fromJson(data, new TypeToken<DarkSky>() {
        }.getType());

        //System.out.println("gson: " + darkSky.toString());
        return darkSky;
    }

    public String darkSkyClient(String latitude, String longitude) throws ForecastException {
        ForecastRequest request = new ForecastRequestBuilder()
                .key(new APIKey("687d8276befb3d1b35d918e6dedaa7f9"))
                .location(new GeoCoordinates(new Longitude(Double.valueOf(longitude)), new Latitude(Double.valueOf(latitude)))).build();

        DarkSkyClient client = new DarkSkyClient();
        String forecast = client.forecastJsonString(request);
        return forecast;
    }
}
