package app.service.solcast;

import app.dao.SolcastSPARQL;
import app.model.solcast.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SolcastGSONWriting {


    public static List<Measurement> writeGSON(SolcastRDFReading solcastRDFReading) throws IOException{

        //SolcastSPARQL ss = new SolcastSPARQL();
        //SolcastRDFReading solcastRDFReading = new SolcastRDFReading(ss);

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

            measurementList.add(i, measurement);
        }

        return measurementList;
    }


}
