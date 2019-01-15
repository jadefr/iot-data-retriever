package app.service.solcast;

/*
classe destinada a recuperar os dados obtidos pela API e nomea-los de acordo com as devidas classes da ontologia
pegar os dados da primeira linha somente get(0)
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SolcastMeasurements {

    public static ArrayList<String[]> getMeasurements() throws IOException {

        Solcast solcast = SolcastReading.readSolcast();
        ArrayList<String[]> measurements = new ArrayList<>();

        List<ForecastsIndex> forecastsIndices = solcast.getForecasts();
      /*  for (ForecastsIndex fi: forecastsIndices){
            System.out.println(fi.getGhi());
        }*/
        System.out.println(forecastsIndices.get(0).getGhi());

        //GlobalHorizontalIrradianceCenterValue
        String[] measurement1 = {"GlobalHorizontalIrradianceCenterValue", forecastsIndices.get(0).getGhi()};
        measurements.add(measurement1);
        System.out.println(measurements.get(0)[0] + " = " + measurements.get(0)[1]);

        //GlobalHorizontalIrradiance10thPercentileValue
        String[] measurement2 = {"GlobalHorizontalIrradiance10thPercentileValue", forecastsIndices.get(0).getGhi10()};
        measurements.add(measurement2);
        System.out.println(measurements.get(1)[0] + " = " + measurements.get(1)[1]);

        //GlobalHorizontalIrradiance90thPercentileValue
        String[] measurement3 = {"GlobalHorizontalIrradiance90thPercentileValue", forecastsIndices.get(0).getGhi90()};
        measurements.add(measurement3);

        //DirectNormalIrradianceCenterValue
        String[] measurement4 = {"DirectNormalIrradianceCenterValue", forecastsIndices.get(0).getDni()};
        measurements.add(measurement4);

        //DirectNormalIrradiance10thPercentileValue
        String[] measurement5 = {"DirectNormalIrradiance10thPercentileValue", forecastsIndices.get(0).getDni10()};
        measurements.add(measurement5);

        //DirectNormalIrradiance90thPercentileValue
        String[] measurement6 = {"DirectNormalIrradiance90thPercentileValue", forecastsIndices.get(0).getDni90()};
        measurements.add(measurement6);

        //DiffuseHorizontalIrradiance
        String[] measurement7 = {"DiffuseHorizontalIrradiance", forecastsIndices.get(0).getDhi()};
        measurements.add(measurement7);

        //AirTemperature
        String[] measurement8 = {"AirTemperature", forecastsIndices.get(0).getAirTemp()};
        measurements.add(measurement8);

        //Zenith
        String[] measurement9 = {"Zenith", forecastsIndices.get(0).getZenith()};
        measurements.add(measurement9);

        //Azimuth
        String[] measurement10 = {"Azimuth", forecastsIndices.get(0).getAzimuth()};
        measurements.add(measurement10);

        //CloudOpacity
        String[] measurement11 = {"CloudOpacity", forecastsIndices.get(0).getCloudOpacity()};
        measurements.add(measurement11);

        return measurements;
    }

}
