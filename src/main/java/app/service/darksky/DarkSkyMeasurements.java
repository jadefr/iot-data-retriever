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

        DarkSky darkSky = DarkSkyReading.readDarkSky();
        ArrayList<String[]> measurements = new ArrayList<>(); // guarda o nome e valor do parametro medido

        //acessando os atributos (atraves dos getters) do objeto dark sky e adicionando-os na lista de medicoes
        String[] measurement1 = {"PrecipitationIntensity", darkSky.getCurrently().getPrecipIntensity()};
        measurements.add(measurement1);

        float precipProb = Float.parseFloat(darkSky.getCurrently().getPrecipProbability());
        String precipProbString = String.valueOf(precipProb * 100);
        String[] measurement2 = {"PrecipitationProbability", precipProbString};
        measurements.add(measurement2);

        String[] measurement3 = {"InstantaneousTemperature", darkSky.getCurrently().getTemperature()};
        measurements.add(measurement3);

        String[] measurement4 = {"ApparentTemperature", darkSky.getCurrently().getApparentTemperature()};
        measurements.add(measurement4);

        String[] measurement5 = {"DewPoint", darkSky.getCurrently().getDewPoint()};
        measurements.add(measurement5);

        /*float humidity = Float.parseFloat(darkSky.getCurrently().getHumidity());
        String humidityString = String.valueOf(humidity * 100);
        String[] measurement6 = {"Humidity", humidityString};
        measurements.add(measurement6);*/

        String[] measurement6 = {"Humidity", darkSky.getCurrently().getHumidity()};
        measurements.add(measurement6);

        String[] measurement7 = {"Pressure", darkSky.getCurrently().getPressure()};
        measurements.add(measurement7);

        String[] measurement8 = {"WindSpeed", darkSky.getCurrently().getWindSpeed()};
        measurements.add(measurement8);

        String[] measurement9 = {"WindGust", darkSky.getCurrently().getWindGust()};
        measurements.add(measurement9);

        String[] measurement10 = {"WindBearing", darkSky.getCurrently().getWindBearing()};
        measurements.add(measurement10);

        String[] measurement11 = {"CloudCover", darkSky.getCurrently().getCloudCover()};
        measurements.add(measurement11);

        String[] measurement12 = {"UvIndex", darkSky.getCurrently().getUvIndex()};
        measurements.add(measurement12);

        String[] measurement13 = {"Visibility", darkSky.getCurrently().getVisibility()};
        measurements.add(measurement13);

        String[] measurement14 = {"Ozone", darkSky.getCurrently().getOzone()};
        measurements.add(measurement14);

       /* for (String[] measurement : measurements) {
            System.out.println(measurement[0] + " = " + measurement[1]);
        }*/


        return measurements;
    }
}
