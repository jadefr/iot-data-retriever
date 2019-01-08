package app.service.darksky;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Retorna uma lista de instancias coletadas pela API DarkSky (atraves da classe
 * DSJsonReading)
 *
 * @author jadef
 */

public class DarkSkyMeasurements {

    public static ArrayList<String[]> getMeasurements() throws IOException {

        DarkSky darkSky = DarkSkyJSONReading.readDarkSky();
        ArrayList<String[]> measurements = new ArrayList<>(); // guarda o nome e valor do parametro medido

        //acessando os atributos (atraves dos getters) do objeto dark sky e adicionando-os na lista de medicoes
        String[] measurement1 = {"Summary", darkSky.getCurrently().getSummary()};
        measurements.add(measurement1);
        System.out.println(measurements.get(0)[0] + " = " + measurements.get(0)[1]);

        String[] measurement3 = {"InstantaneousTemperature", darkSky.getCurrently().getTemperature()};
        measurements.add(measurement3);

        String[] measurement4 = {"PrecipitationIntensity", darkSky.getCurrently().getPrecipIntensity()};
        measurements.add(measurement4);

        float precipProb = Float.parseFloat(darkSky.getCurrently().getPrecipProbability());
        String precipProbString = String.valueOf(precipProb * 100);
        String[] measurement5 = {"PrecipitationProbability", precipProbString};
        measurements.add(measurement5);

        String[] measurement6 = {"ApparentTemperature", darkSky.getCurrently().getApparentTemperature()};
        measurements.add(measurement6);

        String[] measurement7 = {"DewPoint", darkSky.getCurrently().getDewPoint()};
        measurements.add(measurement7);

        float humidity = Float.parseFloat(darkSky.getCurrently().getHumidity());
        String humidityString = String.valueOf(humidity * 100);
        String[] measurement8 = {"Humidity", humidityString};
        measurements.add(measurement8);

        String[] measurement9 = {"WindSpeed", darkSky.getCurrently().getWindSpeed()};
        measurements.add(measurement9);

        String[] measurement10 = {"WindGust", darkSky.getCurrently().getWindGust()};
        measurements.add(measurement10);

        String[] measurement11 = {"WindBearing", darkSky.getCurrently().getWindBearing()};
        measurements.add(measurement11);

        String[] measurement12 = {"Ozone", darkSky.getCurrently().getOzone()};
        measurements.add(measurement12);

        return measurements;
    }
}
