package app.service;

import app.model.Characteristic;
import app.model.Measurement;
import app.model.Sensor;
import app.model.SensorProperty;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GsonWriting {


    public static List<Measurement> writeGSON(RdfReading rdfReading) throws IOException {

        //SparqlQuerying ss = new SparqlQuerying();
        //RdfReading RdfReading = new RdfReading(ss);

        ArrayList<String> characteristicNames = rdfReading.getCharacteristicNames();
       /* for (String name : characteristicNames) {
           System.out.println("characteristic: " + name);
        }
        System.out.println("characteristicNames.size(): " + characteristicNames.size());

        List<String> characteristicNamesWithoutDuplicates = characteristicNames.stream().distinct().collect(Collectors.toList());
        for (String name : characteristicNamesWithoutDuplicates) {
           // System.out.println("characteristicWithoutDuplicates: " + name);
        }*/

        ArrayList<String> characteristicValues = rdfReading.getCharacteristicValues();
       /* for (String name : characteristicValues) {
            System.out.println("characteristicValues: " + name);
        }
        System.out.println("characteristicValues.size(): " + characteristicValues.size());*/


        //List<String> characteristicValuesWithoutDuplicates = characteristicValues.stream().distinct().collect(Collectors.toList());

        ArrayList<String> standards = rdfReading.getStandards();
       /* for (String name : standards) {
            System.out.println("standards: " + name);
        }
        System.out.println("standards.size(): " + standards.size());*/


        //List<String> standardsWithoutDuplicates = standards.stream().distinct().collect(Collectors.toList());

        ArrayList<String> sensors = rdfReading.getSensors();
       /* for (String name : sensors) {
            System.out.println("sensors: " + name);
        }
        System.out.println("sensors.size(): " + sensors.size());*/


        ArrayList<String> operatingPropertyNames = rdfReading.getOperatingPropertyNames();
       /*
 for (String name : operatingPropertyNames) {
            System.out.println("operatingPropertyNames: " + name);
        }
        System.out.println("operatingPropertyNames.size(): " + operatingPropertyNames.size());*/


        ArrayList<String> operatingPropertyStandards = rdfReading.getOperatingPropertyStandards();
        /*
for (String name : operatingPropertyStandards) {
            System.out.println("operatingPropertyStandards: " + name);
        }
        System.out.println("operatingPropertyStandards.size(): " + operatingPropertyStandards.size());*/


        ArrayList<String> operatingPropertyValues = rdfReading.getOperatingPropertyValues();
      /*  for (String name : operatingPropertyValues) {
            System.out.println("operatingPropertyValues: " + name);
        }
        System.out.println("operatingPropertyValues.size(): " + operatingPropertyValues.size());*/


        ArrayList<String> operatingRangeNames = rdfReading.getOperatingRangeNames();
       /* for (String name : operatingRangeNames) {
            System.out.println("operatingRangeNames: " + name);
        }
        System.out.println("operatingRangeNames.size(): " + operatingRangeNames.size());*/


        ArrayList<String> operatingRangeStandards = rdfReading.getOperatingRangeStandards();
      /*  for (String name : operatingRangeStandards) {
           System.out.println("operatingRangeStandards: " + name);
        }
        System.out.println("operatingRangeStandards.size(): " + operatingRangeStandards.size());*/


        ArrayList<String> operatingRangeValues = rdfReading.getOperatingRangeValues();
        /*for (String name : operatingRangeValues) {
           System.out.println("operatingRangeValues: " + name);
        }
        System.out.println("operatingRangeValues.size(): " + operatingRangeValues.size());*/


        ArrayList<String> definitions = rdfReading.getDefinitions();
     /*   for (String name : definitions) {
            System.out.println("definitions: " + name);
        }
        System.out.println("definitions.size(): " + definitions.size());*/

        ArrayList<String> entities = rdfReading.getEntities();
      /*  for (String entity : entities) {
            System.out.println("entity: " + entity);
        }
        System.out.println("entities.size(): " + entities.size());*/


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
