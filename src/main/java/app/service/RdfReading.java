package app.service;

import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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

    private String[] names = {"?measurementValue ", "?measurement ", "?standard ", "?x ", "?sensor ", "?characteristic ", "?operatingProperty ",
            "?operatingStandard ", "?operatingRange ", "?rangeStandard ", "?definition ", "?y ", "?z ", "?entity ", "?observation "};


    public void readRDF(ArrayList<String> sparqlList) throws IOException {

        ArrayList<String[]> list = new ArrayList<>();

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

        for (String sparqlLine : sparqlList) {
            //System.out.println("sparqlLine: " + sparqlLine);
            String[] attributes = sparqlLine.split("\\) \\(");
            list.add(attributes);
        }

        Set<String> set = new HashSet<String>();
        ArrayList<Integer> uniquePositionsList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            if (set.add(list.get(i)[0])) {
                uniquePositionsList.add(i);
            }
        }

        //System.out.println("uniquePositionsList.size():" + uniquePositionsList.size());

        ArrayList<String[]> uniqueElementsList = new ArrayList<>();
        for (Integer i : uniquePositionsList) {
            uniqueElementsList.add(list.get(i));
        }

       /* for (String[] uniqueElements : uniqueElementsList) {
            System.out.println("uniqueElements:" + Arrays.toString(uniqueElements));
        }*/

        ArrayList<String[]> uniqueElementsListStandardized = new ArrayList<>();
        for (String[] attributes : uniqueElementsList) {

            //System.out.println("\n" + "\n" + "attributes.length: " + attributes.length);
            String attributesString = Arrays.toString(attributes);
            //System.out.println("attributesString: " + attributesString);
            String[] standardizedElements = new String[15];

            if (attributes.length == 14 || attributes.length == 15) { // works for the queries of solcast1 and solcast2
                for (int i = 0, j = 0; i < names.length; i++, j++) {
                    if (attributesString.contains(names[i])) {
                        standardizedElements[i] = attributes[j];
                    } else {
                        standardizedElements[i] = "?standard = <does not apply>";
                        j = j - 1;
                    }
                }

            }

            if (attributes.length == 8) { //darsky2
                int[] positionsNotNull = {0, 1, 2, 3, 5, 10, 13, 14};
                standardizedElements = fillStandardizedElements(positionsNotNull, attributes);
            }

            if (attributes.length == 9) { //darksky1
                int[] positionsNotNull = {0, 1, 2, 3, 4, 5, 10, 13, 14};
                standardizedElements = fillStandardizedElements(positionsNotNull, attributes);
            }

            if (attributes.length == 6) { //works for solcast3
                int[] positionsNotNull = {0, 1, 3, 5, 13, 14};
                standardizedElements = fillStandardizedElements(positionsNotNull, attributes);
            }

            String[] darkSkyCharacteritics = {"?characteristic = <PrecipitationIntensity> ", "?characteristic = <PrecipitationProbability> ",
                    "?characteristic = <Temperature> ", "?characteristic = <ApparentTemperature> ", "?characteristic = <DewPoint> ",
                    "?characteristic = <Humidity> ", "?characteristic = <Pressure> ", "?characteristic = <WindSpeed> ", "?characteristic = <WindGust> ",
                    "?characteristic = <WindBearing> ", "?characteristic = <CloudCover> ", "?characteristic = <UvIndex> ",
                    "?characteristic = <Visibility> ", "?characteristic = <Ozone> "};

            String dsCharacteristics = Arrays.toString(darkSkyCharacteritics);
            boolean belongsToDarkSky =  dsCharacteristics.contains(attributes[5]);

            // due to some problem in the integration process, the entity may not be correct, so there is a need to ensure it
            if (belongsToDarkSky){
                standardizedElements[13] = "?entity = <DarkSky>";
            }else{
                standardizedElements[13] = "?entity = <Solcast>";
            }

            uniqueElementsListStandardized.add(standardizedElements);
        }


        /*for (String[] attributes : uniqueElementsListStandardized) {
            //System.out.println("uniqueElementsStandardizedToString: " + Arrays.toString(attributes));
        }*/


        for (String[] attributes : uniqueElementsListStandardized) {

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
                    } else if (attribute.contains("?entity")) {
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
                        //String operatingRange = value.substring(0, (value.length()) - 1);
                        //String operatingRange = value;
                        operatingRangeValues.add(value);
                    } else if (attribute.contains("?definition")) {
                        definitions.add(value);
                    }


                }
            }
        }

    }

    private String[] fillStandardizedElements(int[] positionsNotNull, String[] attributes) {
        String[] nullElements = fillNullElements();
        String[] standardizedElements = new String[15];

        for (int i = 0; i < positionsNotNull.length; i++) {
            standardizedElements[positionsNotNull[i]] = attributes[i];
            // System.out.println("standardizedElements" + positionsNotNull[i] + ": " + standardizedElements[positionsNotNull[i]]);
        }

        for (int i = 0; i < names.length; i++) {

            if (standardizedElements[i] == null) {
                standardizedElements[i] = nullElements[i];
                // System.out.println("standardizedElements" + i + ": " + standardizedElements[i]);
            }
        }
        return standardizedElements;
    }


    private String[] fillNullElements() {

        String[] doesNotApplyArray = new String[15];

        for (int i = 0; i < doesNotApplyArray.length; i++) {

            doesNotApplyArray[i] = names[i] + "= <does not apply>";

            if (i == 10 || i == 11 || i == 12) {
                doesNotApplyArray[i] = names[i] + "= \"does not apply\"";
            }

        }
       /* for (String doesNotApply : doesNotApplyArray) {
            System.out.println("doesNotApplyArray: " + doesNotApply);
        }*/

        return doesNotApplyArray;

    }


}
        /*for (String[] attributes : uniqueElementsList) {

            for (String attribute : attributes) {
                //System.out.println("attribute: " + attribute);

                if (attribute.contains("<")) {
                    int x = attribute.indexOf("<");
                    int y = attribute.indexOf(">");
                    String feature = attribute.substring(x + 1, y);

                    if (attribute.contains("?characteristic")) {
                        characteristicNames.add(feature);
                    } else if (!attributes.toString().contains("?characteristic")) {
                        characteristicNames.add("null");
                    }
                    if (attribute.contains("?standard")) {
                        standards.add(feature);
                    } else if (!attributes.toString().contains("?standard")) {
                        standards.add("null");
                    }
                    if (attribute.contains("?sensor")) {
                        sensors.add(feature);
                    } else if (!attributes.toString().contains("?sensor")) {
                        sensors.add("null");
                    }
                    if (attribute.contains("?operatingProperty")) {
                        operatingPropertyNames.add(feature);
                    } else if (attributes.toString().contains("?operatingProperty")) {
                        operatingPropertyNames.add("null");
                    }
                    if (attribute.contains("?operatingStandard")) {
                        operatingPropertyStandards.add(feature);
                    } else if (!attributes.toString().contains("?operatingStandard")) {
                        operatingPropertyStandards.add("null");
                    }
                    if (attribute.contains("?operatingRange")) {
                        operatingRangeNames.add(feature);
                    } else if (!attributes.toString().contains("?operatingRange")) {
                        operatingRangeNames.add("null");
                    }
                    if (attribute.contains("?rangeStandard")) {
                        operatingRangeStandards.add(feature);
                    } else if (!attributes.toString().contains("?rangeStandard")) {
                        operatingRangeStandards.add("null");
                    }
                    if (attribute.contains("?entity")) {
                        entities.add(feature);
                    } else if (!attributes.toString().contains("?entity")) {
                        entities.add("null");
                    }

                } else if (attribute.contains("\"")) {
                    int a = attribute.indexOf("\"");
                    int length = attribute.length();
                    String value = attribute.substring(a + 1, length - 2);

                    if (attribute.contains("?x")) {
                        characteristicValues.add(value);
                    } else if (!attributes.toString().contains("?x")) {
                        characteristicValues.add("null");
                    }

                    if (attribute.contains("?y")) {
                        operatingPropertyValues.add(value);
                    } else if (attribute.contains("?z")) {
                        //String operatingRange = value.substring(0, (value.length()) - 1);
                        String operatingRange = value;
                        operatingRangeValues.add(operatingRange);
                    } else if (attribute.contains("?definition")) {
                        definitions.add(value);
                    }

                }
            }*/













       /* for (String solcastAttribute : sparqlList) {

            String[] attributes = solcastAttribute.split("\\) \\(");

            for (String attribute : attributes) {

                System.out.println("attribute: " + attribute);

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
                    } else if (attribute.contains("?entity")) {
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
                        String operatingRange = value.substring(0, (value.length()) - 1);
                        operatingRangeValues.add(operatingRange);
                    } else if (attribute.contains("?definition")) {
                        definitions.add(value);
                    }


                }
            }
        }*/




