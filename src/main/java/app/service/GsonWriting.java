package app.service;

import app.model.Characteristic;
import app.model.Measurement;
import app.model.Sensor;
import app.model.SensorProperty;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import java.lang.*;

public class GsonWriting {


    public static List<Measurement> writeGSON(RdfReading rdfReading) throws IOException {


        ArrayList<String> characteristicNames = rdfReading.getCharacteristicNames();
        ArrayList<String> characteristicNamesFormatted = formatArrayList(characteristicNames);

        ArrayList<String> characteristicValues = rdfReading.getCharacteristicValues();

        ArrayList<String> standards = rdfReading.getStandards();
        ArrayList<String> standardsFormatted = formatArrayList(standards);

        ArrayList<String> sensors = rdfReading.getSensors();
        ArrayList<String> sensorsFormatted = formatArrayList(sensors);

        ArrayList<String> operatingPropertyNames = rdfReading.getOperatingPropertyNames();
        ArrayList<String> operatingPropertyNamesFormatted = formatArrayList(operatingPropertyNames);

        ArrayList<String> operatingPropertyStandards = rdfReading.getOperatingPropertyStandards();

        ArrayList<String> operatingPropertyValues = rdfReading.getOperatingPropertyValues();

        ArrayList<String> operatingRangeNames = rdfReading.getOperatingRangeNames();
        ArrayList<String> operatingRangeNamesFormatted = formatArrayList(operatingRangeNames);

        ArrayList<String> operatingRangeStandards = rdfReading.getOperatingRangeStandards();

        ArrayList<String> operatingRangeValues = rdfReading.getOperatingRangeValues();

        ArrayList<String> definitions = rdfReading.getDefinitions();

        ArrayList<String> entities = rdfReading.getEntities();
        ArrayList<String> entitiesFormatted = formatArrayList(entities);

        List<Measurement> measurementList = new ArrayList<>();
        for (int i = 0; i < characteristicNamesFormatted.size(); i++) {
            Measurement measurement = new Measurement();
            Characteristic characteristic = new Characteristic();
            Sensor sensor = new Sensor();
            SensorProperty operatingProperty = new SensorProperty();
            SensorProperty operatingRange = new SensorProperty();

            measurement.setStandard(standardsFormatted.get(i));
            measurement.setDefinition(definitions.get(i));
            measurement.setProvider(entitiesFormatted.get(i));

            characteristic.setName(characteristicNamesFormatted.get(i));
            //System.out.println(i + " ::: " + characteristicNames.get(i));
            characteristic.setValue(characteristicValues.get(i));
            measurement.setCharacteristic(characteristic);

            if ((operatingPropertyNamesFormatted.size() != 0) && (i < operatingPropertyNamesFormatted.size())) {
                operatingProperty.setName(operatingPropertyNamesFormatted.get(i));
                operatingProperty.setStandard(operatingPropertyStandards.get(i));
                operatingProperty.setValue(operatingPropertyValues.get(i));


                operatingRange.setName(operatingRangeNamesFormatted.get(i));
                operatingRange.setStandard(operatingRangeStandards.get(i));
                operatingRange.setValue(operatingRangeValues.get(i));

                sensor.setOperatingProperty(operatingProperty);
                sensor.setOperatingRange(operatingRange);
            }
            sensor.setName(sensorsFormatted.get(i));
            measurement.setSensor(sensor);

            measurementList.add(i, measurement);
        }

        return measurementList;
    }


    private static List<String> separateSubstrings(String name) {

        // get the positions of upper cases occurrences
        List<Integer> positionsList = new ArrayList<Integer>();
        boolean upperCase = false;
        boolean number = false;
        char ch;

        for (int i = 0; i < name.length(); i++) {
            ch = name.charAt(i);
            upperCase = Character.isUpperCase(ch);
            number = Character.isDigit(ch);

            if (upperCase) {
                positionsList.add(i);
            }

            if (number) {
                positionsList.add(i);
                break;
            }
        }


        // separate the words of the string
        List<String> substringList = new ArrayList<String>();
        String substring = "";

        String initialSubstring = name.substring(0, positionsList.get(0));
        substringList.add(initialSubstring);
        substringList.add(" ");

        for (int i = 0; i < (positionsList.size() - 1); i++) {
            //substringList.add(" ");
            substring = name.substring(positionsList.get(i), positionsList.get(i + 1));
            substringList.add(substring);
            substringList.add(" ");
        }
        String lastSubstring = name.substring(positionsList.get(positionsList.size() - 1), name.length());
        substringList.add(lastSubstring);

        return substringList;

    }

    private static String formatNameString(List<String> substringList) {

        StringBuilder stringBuilder = new StringBuilder();
        for (String s : substringList) {
            stringBuilder.append(s);
        }
        String name = stringBuilder.toString();

        // adjust some characteristic names
        if (name.contains("Percentile")) {
            name = name.replace("thPercentileValue", "%");
        }

        // adjust some operating property names
        if (name.contains("Angle Pyranometer")){
            name = name.replace(" Pyranometer","");
        }
        if (name.contains("Angle Pyrheliometer")){
            name = name.replace(" Pyrheliometer","");
        }

        //adjust some sensor names
        if (name.contains("G H I C V")){
            name = name.replace(" G H I C V","");
            System.out.println("replace: " + name);
        }
        if (name.contains("D H I")){
            name = name.replace(" D H I","");
            System.out.println("replace: " + name);
        }

        String formattedName = name.trim();

        return formattedName;
    }

    private static ArrayList<String> formatArrayList(ArrayList<String> characteristicNames) {

        List<String> substringList = new ArrayList<>();
        String formattedName = "";
        ArrayList<String> characteristicNamesFormatted = new ArrayList<>();

        for (String name : characteristicNames) {

            if (!name.contains("does not ")) {

                substringList = separateSubstrings(name);
                formattedName = formatNameString(substringList);
                characteristicNamesFormatted.add(formattedName);

            } else {
                characteristicNamesFormatted.add("does not apply");
            }
        }

        return characteristicNamesFormatted;
    }

}
