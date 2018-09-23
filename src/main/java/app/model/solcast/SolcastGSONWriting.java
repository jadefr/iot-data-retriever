package app.model.solcastmodel;

import app.dao.SolcastSPARQL;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SolcastGSONWriting {

    private static Measurement measurement;
    private SolcastSPARQL ss;
    private static SolcastRDFReading solcastRDFReading;


    public SolcastGSONWriting() throws IOException {
        measurement = new Measurement();
        ss = new SolcastSPARQL();
        solcastRDFReading = new SolcastRDFReading(ss);
    }


    public List<Measurement> writeGSON() throws IOException {

        ArrayList<String> characteristicNames = solcastRDFReading.getCharacteristicNames();
        ArrayList<String> characteristicValues = solcastRDFReading.getCharacteristicValues();
        ArrayList<String> standards = solcastRDFReading.getStandards();
        ArrayList<String> sensors = solcastRDFReading.getSensors();
        ArrayList<String> operatingPropertyNames = solcastRDFReading.getOperatingPropertyNames();
        ArrayList<String> operatingPropertyStandards = solcastRDFReading.getOperatingPropertyStandards();
        ArrayList<String> operatingPropertyValues = solcastRDFReading.getOperatingPropertyValues();
        ArrayList<String> operatingRangeNames = solcastRDFReading.getOperatingRangeNames();
        ArrayList<String> operatingRangeStandards = solcastRDFReading.getOperatingRangeStandards();
        ArrayList<String> operatingRangeValues = solcastRDFReading.getOperatingRangeValues();
        ArrayList<String> definitions = solcastRDFReading.getDefinitions();

        ArrayList<String> solcastSparqlList = ss.getSolcastFromOntology();
        Characteristic characteristic = new Characteristic();
        Sensor sensor = new Sensor();
        SensorProperty operatingProperty = new SensorProperty();
        SensorProperty operatingRange = new SensorProperty();

        List<Measurement> measurementList = new ArrayList<Measurement>();
        for (int i = 0; i < solcastSparqlList.size() ; i++){
            measurement.setStandard(standards.get(i));
            measurement.setDefinition(definitions.get(i));

            characteristic.setName(characteristicNames.get(i));
            characteristic.setValue(characteristicValues.get(i));
            measurement.setCharacteristic(characteristic);

            operatingProperty.setName(operatingPropertyNames.get(i));
            operatingProperty.setStandard(operatingPropertyStandards.get(i));
            operatingProperty.setValue(operatingPropertyValues.get(i));

            operatingRange.setName(operatingRangeNames.get(i));
            operatingRange.setStandard(operatingRangeStandards.get(i));
            operatingRange.setValue(operatingRangeValues.get(i));

            sensor.setName(sensors.get(i));
            sensor.setOperatingProperty(operatingProperty);
            sensor.setOperatingRange(operatingRange);
            measurement.setSensor(sensor);

            measurementList.add(measurement);
        }

        return measurementList;
    }


  /*  public static void writeCharacteristic(){

        Characteristic characteristic = new Characteristic();

        //Associando as caracteristicas
        ArrayList<String> names = solcastRDFReading.getCharacteristicNames();
        for (String name : names){
            characteristic.setName(name);
        }

        //Associando os valores (medidos)
        ArrayList<String> values = solcastRDFReading.getCharacteristicValues();
        for (String value : values){
            characteristic.setValue(value);
        }

    }

    public static void writeStandardAndDefinition() throws IOException {
        //Associando valor a standard
        ArrayList<String> standards = solcastRDFReading.getStandards();
        for (String standard : standards){
            measurement.setStandard(standard);
        }

        //Associando valor a definition
        ArrayList<String> definitions = solcastRDFReading.getDefinitions();
        for (String definition : definitions){
            measurement.setDefinition(definition);
        }

    }

    public static void writeSensor() throws IOException {

        Sensor sensor = new Sensor();
        SensorPropertyGSONWriting spgw = new SensorPropertyGSONWriting();

        //Associando os nomes dos sensores
        ArrayList<String> names = solcastRDFReading.getSensors();
        for (String name : names){
            sensor.setName(name);
        }

        SensorProperty operatingProperty = spgw.writeOperatingProperty();
        sensor.setOperatingProperty(operatingProperty);

        SensorProperty operatingRange = spgw.writeOperatingRange();
        sensor.setOperatingRange(operatingRange);

    }
*/



}
