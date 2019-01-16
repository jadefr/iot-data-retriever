package app.service;

import app.model.Characteristic;
import app.model.Measurement;
import app.model.Sensor;
import app.model.SensorProperty;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GsonWriting {


    public static List<Measurement> writeGSON(RdfReading rdfReading) throws IOException {

        //SparqlQuerying ss = new SparqlQuerying();
        //RdfReading RdfReading = new RdfReading(ss);

        ArrayList<String> characteristicNames = rdfReading.getCharacteristicNames();
        ArrayList<String> characteristicValues = rdfReading.getCharacteristicValues();
        ArrayList<String> standards = rdfReading.getStandards();
        ArrayList<String> sensors = rdfReading.getSensors();
        ArrayList<String> operatingPropertyNames = rdfReading.getOperatingPropertyNames();
        ArrayList<String> operatingPropertyStandards = rdfReading.getOperatingPropertyStandards();
        ArrayList<String> operatingPropertyValues = rdfReading.getOperatingPropertyValues();
        ArrayList<String> operatingRangeNames = rdfReading.getOperatingRangeNames();
        ArrayList<String> operatingRangeStandards = rdfReading.getOperatingRangeStandards();
        ArrayList<String> operatingRangeValues = rdfReading.getOperatingRangeValues();
        ArrayList<String> definitions = rdfReading.getDefinitions();
        ArrayList<String> entities = rdfReading.getEntities();
        for (String entity: entities){
            System.out.println(entity);
        }

        System.out.println(characteristicNames.size());
        List<Measurement> measurementList = new ArrayList<Measurement>();
        for (int i = 0; i < characteristicNames.size(); i++) {
            Measurement measurement = new Measurement();
            Characteristic characteristic = new Characteristic();
            Sensor sensor = new Sensor();
            SensorProperty operatingProperty = new SensorProperty();
            SensorProperty operatingRange = new SensorProperty();

            measurement.setStandard(standards.get(i));
            measurement.setDefinition(definitions.get(i));
            measurement.setProvider(entities.get(i));

            characteristic.setName(characteristicNames.get(i));
            //System.out.println(i + " ::: " + characteristicNames.get(i));
            characteristic.setValue(characteristicValues.get(i));
            measurement.setCharacteristic(characteristic);

            if ((operatingPropertyNames.size() != 0) && (i < operatingPropertyNames.size())) {
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
