package app.service;

import java.io.IOException;
import java.util.ArrayList;

public class RdfReading {

    // private ArrayList solcastList;

    private ArrayList<String> characteristicNames;
    private ArrayList<String> characteristicValues; //x
    private ArrayList<String> standards;
    private ArrayList<String> sensors;
    private ArrayList<String> operatingPropertyNames;
    private ArrayList<String> operatingPropertyStandards;
    private ArrayList<String> operatingPropertyValues; //y
    private ArrayList<String> operatingRangeNames;
    private ArrayList<String> operatingRangeStandards;
    private ArrayList<String> operatingRangeValues; //z
    private ArrayList<String> definitions;
    private ArrayList<String> entities;

    public ArrayList<String> getCharacteristicNames() {
        return characteristicNames;
    }

    public ArrayList<String> getCharacteristicValues() {
        return characteristicValues;
    }

    public ArrayList<String> getStandards() {
        return standards;
    }

    public ArrayList<String> getSensors() {
        return sensors;
    }

    public ArrayList<String> getOperatingPropertyNames() {
        return operatingPropertyNames;
    }

    public ArrayList<String> getOperatingPropertyStandards() {
        return operatingPropertyStandards;
    }

    public ArrayList<String> getOperatingPropertyValues() {
        return operatingPropertyValues;
    }

    public ArrayList<String> getOperatingRangeNames() {
        return operatingRangeNames;
    }

    public ArrayList<String> getOperatingRangeStandards() {
        return operatingRangeStandards;
    }

    public ArrayList<String> getOperatingRangeValues() {
        return operatingRangeValues;
    }

    public ArrayList<String> getDefinitions() {
        return definitions;
    }

    public ArrayList<String> getEntities() {
        return entities;
    }

    public void writeRDF(ArrayList<String> solcastList) throws IOException {
        //ArrayList<String> solcastList = ss.getDataFromOntology();

        characteristicNames = new ArrayList<String>();
        characteristicValues = new ArrayList<String>();
        standards = new ArrayList<String>();
        sensors = new ArrayList<String>();
        operatingPropertyNames = new ArrayList<String>();
        operatingPropertyStandards = new ArrayList<String>();
        operatingPropertyValues = new ArrayList<String>();
        operatingRangeNames = new ArrayList<String>();
        operatingRangeStandards = new ArrayList<String>();
        operatingRangeValues = new ArrayList<String>();
        definitions = new ArrayList<String>();
        entities = new ArrayList<String>();

        for (String solcastAttribute : solcastList) {

            String[] attributes = solcastAttribute.split("\\) \\(");

            for (String attribute : attributes) {

                if (attribute.contains("<")) {
                    int x = attribute.indexOf("<");
                    int y = attribute.indexOf(">");
                    String feature = attribute.substring(x + 1, y);

                    if (attribute.contains("?characteristic")) {
                        characteristicNames.add(feature);
                    } else if (attribute.contains("?standard")) {
                        standards.add(feature);
                    } else if (attribute.contains("?sensor")) {
                        sensors.add(feature);
                    } else if (attribute.contains("?operatingProperty")) {
                        operatingPropertyNames.add(feature);
                    } else if (attribute.contains("?operatingStandard")) {
                        operatingPropertyStandards.add(feature);
                    } else if (attribute.contains("?operatingRange")) {
                        operatingRangeNames.add(feature);
                    } else if (attribute.contains("?rangeStandard")) {
                        operatingRangeStandards.add(feature);
                    }else if (attribute.contains("?entity")) {
                        entities.add(feature);
                    }

                } else if (attribute.contains("\"")) {
                    int a = attribute.indexOf("\"");
                    int length = attribute.length();
                    String value = attribute.substring(a + 1, length - 2);

                    if (attribute.contains("?x")) {
                        characteristicValues.add(value);
                    } else if (attribute.contains("?y")) {
                        operatingPropertyValues.add(value);
                    } else if (attribute.contains("?z")) {
                        String operatingRange = value.substring(0,(value.length())-1);
                        operatingRangeValues.add(operatingRange);
                    } else if (attribute.contains("?definition")) {
                        definitions.add(value);
                    }


                }
            }
        }


    }
}
