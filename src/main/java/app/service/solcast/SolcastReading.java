package app.service.solcast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SolcastReading {

    private static String latitude;
    private static String longitude;

    public SolcastReading(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

   /* public static Solcast readSolcast(String latitude, String longitude) throws IOException {

        //URL url = new URL("https://api.solcast.com.au/radiation/forecasts?longitude=-43.350500&latitude=-21.758819&api_key=IGZMht21u4DQIP1UlWMojkMVKrOZJgUs&format=json");
        URL url = new URL("https://api.solcast.com.au/radiation/forecasts?longitude=" + longitude + "&latitude=" + latitude + "&api_key=IGZMht21u4DQIP1UlWMojkMVKrOZJgUs&format=json");
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        InputStream inputStream = httpURLConnection.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String data = "";
        while (line != null) {
            data = data + line;
            line = bufferedReader.readLine();
            System.out.println("API Solcast: "+ data);
        }
        int size = data.length();

        Gson gson = new Gson();
        Solcast solcast = gson.fromJson(data, new TypeToken<Solcast>() {
        }.getType());

        return solcast;
    }*/

    public static Solcast readSolcast() throws IOException {

        //URL url = new URL("https://api.solcast.com.au/radiation/forecasts?longitude=-43.350500&latitude=-21.758819&api_key=IGZMht21u4DQIP1UlWMojkMVKrOZJgUs&format=json");
        URL url = new URL("https://api.solcast.com.au/radiation/forecasts?longitude=" + longitude + "&latitude=" + latitude + "&api_key=IGZMht21u4DQIP1UlWMojkMVKrOZJgUs&format=json");
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        InputStream inputStream = httpURLConnection.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String data = "";
        while (line != null) {
            data = data + line;
            line = bufferedReader.readLine();
        }
        int size = data.length();

        Gson gson = new Gson();
        Solcast solcast = gson.fromJson(data, new TypeToken<Solcast>() {
        }.getType());

        return solcast;
    }


}
