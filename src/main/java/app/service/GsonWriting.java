package app.service;

import app.model.Characteristic;
import app.model.Measurement;
import app.model.Sensor;
import app.model.SensorProperty;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GsonWriting {


    public static List<Measurement> writeGSON(RdfReading RDFReading) throws IOException {

        //SparqlQuerying ss = new SparqlQuerying();
        //RdfReading RdfReading = new RdfReading(ss);

        ArrayList<String> characteristicNames = RDFReading.getCharacteristicNames();
        ArrayList<String> characteristicValues = RDFReading.getCharacteristicValues();
        ArrayList<String> standards = RDFReading.getStandards();
        ArrayList<String> sensors = RDFReading.getSensors();
        ArrayList<String> operatingPropertyNames = RDFReading.getOperatingPropertyNames();
        ArrayList<String> operatingPropertyStandards = RDFReading.getOperatingPropertyStandards();
        ArrayList<String> operatingPropertyValues = RDFReading.getOperatingPropertyValues();
        ArrayList<String> operatingRangeNames = RDFReading.getOperatingRangeNames();
        ArrayList<String> operatingRangeStandards = RDFReading.getOperatingRangeStandards();
        ArrayList<String> operatingRangeValues = RDFReading.getOperatingRangeValues();
        ArrayList<String> definitions = RDFReading.getDefinitions();

        List<Measurement> measurementList = new ArrayList<Measurement>();
        for (int i = 0; i < characteristicNames.size(); i++) {
            Measurement measurement = new Measurement();
            Characteristic characteristic = new Characteristic();
            Sensor sensor = new Sensor();
            SensorProperty operatingProperty = new SensorProperty();
            SensorProperty operatingRange = new SensorProperty();

            measurement.setStandard(standards.get(i));
            measurement.setDefinition(definitions.get(i));

            characteristic.setName(characteristicNames.get(i));
            //System.out.println(i + " ::: " + characteristicNames.get(i));
            characteristic.setValue(characteristicValues.get(i));
            measurement.setCharacteristic(characteristic);

            if (operatingPropertyNames.size() != 0) {
                operatingProperty.setName(operatingPropertyNames.get(i));
                operatingProperty.setStandard(operatingPropertyStandards.get(i));
                operatingProperty.setValue(operatingPropertyValues.get(i));


                operatingRange.setName(operatingRangeNames.get(i));
                operatingRange.setStandard(operatingRangeStandards.get(i));
                operatingRange.setValue(operatingRangeValues.get(i));

                sensor.setOperatingProperty(operatingProperty);
                sensor.setOperatingRange(operatingRange);
            }
            sensor.setName(sensors.get(i));
            measurement.setSensor(sensor);

            measurementList.add(i, measurement);
        }

        return measurementList;
    }


}
