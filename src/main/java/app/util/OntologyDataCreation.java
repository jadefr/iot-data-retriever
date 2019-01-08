package app.util;

/*
ler da ontologia merged-wcm e nela escrever as instancias
 */


import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import app.service.darksky.DarkSkyMeasurements;
import app.service.solcast.SolcastMeasurements;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class OntologyDataCreation {

    static OntModel ontModel;

    static final String BASE_URI1 = "http://www.semanticweb.org/jadef/ontologies/2018/4/oboe_wcm#";
    static final String BASE_URI2 = "http://www.semanticweb.org/jadef/ontologies/2018/4/wcm#";
    static final String BASE_URI3 = "http://ecoinformatics.org/oboe/oboe.1.2/oboe-core.owl#";
    static final String BASE_URI4 = "http://www.semanticweb.org/jadef/ontologies/2018/8/merged-wcm#"; //usar esse pra armazenar os dados criados
    static final String BASE_URI5 = "http://www.w3.org/ns/sosa/";
    static final String BASE_URI6 = "http://www.w3.org/ns/ssn/";
    static final String BASE_URI7 = "http://ecoinformatics.org/oboe/oboe.1.2/oboe-standards.owl#";
    private final String path = "src/main/files/";
    private final String path_out = "";

    public OntologyDataCreation(OntModel ontModel) {
        this.ontModel = ontModel;
    }


    public Model writeOntology() throws IOException {
        //metodo para criar a nova ontologia, apos a criacao de instancias pelos metodos abaixo

        //OutputStream out = new FileOutputStream(path + "\\oboe_wcm_integrated.rdf");
        OutputStream out = new FileOutputStream("\\oboe_wcm_integrated.rdf");
        Model finalOntology = ontModel.write(out, "RDF/XML-ABBREV");
        out.close();
        System.out.println(finalOntology.toString());
        return finalOntology;
    }

    private static OntModel createInstanceDarkSky(OntModel ontModel) throws IOException {

        //Individuo da classe Entity de nome DarkSky
        OntClass darkSkyClass = ontModel.getOntClass(BASE_URI3 + "Entity"); //seleciona a classe Entity
        System.out.println("darkSkyClass: " + darkSkyClass);
        Individual darkSkyIndividual = darkSkyClass.createIndividual(BASE_URI1 + "DarkSKy"); // nome da instancia
        System.out.println("darkSkyIndividual: " + darkSkyIndividual);

        //Individuo da classe Observation
        OntClass observationClass = ontModel.getOntClass(BASE_URI3 + "Observation");
        System.out.println(observationClass);
        Individual observationIndividual = observationClass.createIndividual(BASE_URI1 + "Observation");
        System.out.println(observationIndividual);

        //Recuperar object properties ja existentes na ontologia
        ObjectProperty ofEntity = ontModel.getObjectProperty(BASE_URI3 + "ofEntity");
        System.out.println(ofEntity);
        ObjectProperty hasMeasurement = ontModel.getObjectProperty(BASE_URI3 + "hasMeasurement");
        System.out.println(hasMeasurement);
        ObjectProperty hasValue = ontModel.getObjectProperty(BASE_URI3 + "hasValue");
        System.out.println(hasValue);
        ObjectProperty ofCharacteristic = ontModel.getObjectProperty(BASE_URI3 + "ofCharacteristic");
        System.out.println(ofCharacteristic);

        //Data property ja existentes na ontologia
        DatatypeProperty hasCode = ontModel.getDatatypeProperty(BASE_URI3 + "hasCode");
        System.out.println(hasCode);

        //fazer o link entre darksky e observation atraves da object property ofEntity
        Resource ofEntityLink = observationIndividual.addProperty(ofEntity, darkSkyIndividual);

        ArrayList<String[]> measurements;
        measurements = DarkSkyMeasurements.getMeasurements();

        for (int i = 0; i < measurements.size(); i++) {

            // vetor de Individual para armazenar as instancias da ontologia
            Individual[] population = new Individual[5];

            // vetor de Resources para guardar as relacoes entre instancias
            Resource[] links = new Resource[6];

            //nome da propriedade sendo medida
            String name = measurements.get(i)[0];

            //valor da propriedade sendo medida
            String value = measurements.get(i)[1];

            //Measurement
            OntClass measurementClass = ontModel.getOntClass(BASE_URI3 + "Measurement");
            Individual measurementIndividual = measurementClass.createIndividual(BASE_URI1 + "Measurement" + name);
            population[0] = measurementIndividual;

            //link entre Observation e Measurement
            Resource hasMeasurementLink = observationIndividual.addProperty(hasMeasurement, measurementIndividual);
            links[0] = hasMeasurementLink;

            //MeasurementValue
            OntClass measurementValueClass = ontModel.getOntClass(BASE_URI3 + "MeasuredValue");
            Individual measurementValueIndividual = measurementValueClass.createIndividual(BASE_URI1 + "MeasurementValue" + name);
            population[1] = measurementValueIndividual;

            //link entre Measurement e MeasurementValue
            Resource hasValueLink = measurementIndividual.addProperty(hasValue, measurementValueIndividual);
            links[1] = hasValueLink;

            //Associar os valores obtidos na API
            Resource hasCodeLink = measurementValueIndividual.addProperty(hasCode, value, XSDDatatype.XSDfloat);
            links[2] = hasCodeLink;

            //MeasuredCharacteristic
            OntClass measuredCharacteristicClass = ontModel.getOntClass(BASE_URI3 + "MeasuredCharacteristic");
            Individual measuredCharacteristicIndividual = measuredCharacteristicClass.createIndividual(BASE_URI1 + "MeasuredCharacteristic" + name);
            population[2] = measuredCharacteristicIndividual;

            //link entre Measurement e MeasuredCharacteristic
            Resource ofCharacteristicLink = measurementIndividual.addProperty(ofCharacteristic, measuredCharacteristicIndividual);
            links[3] = ofCharacteristicLink;

            //Specific domain
            OntClass specificDomainClass = ontModel.getOntClass(BASE_URI2 + name);
            Individual specificDomainIndividual = specificDomainClass.createIndividual(BASE_URI1 + name);
            population[3] = specificDomainIndividual;

            //object property do specific domain
            ObjectProperty hasSpecificDomain = ontModel.getObjectProperty(BASE_URI1 + "has" + name);

            //link entre Measurement e Specific domain
            Resource hasSpecificDomainLink = measurementIndividual.addProperty(hasSpecificDomain, specificDomainIndividual);
            links[4] = hasSpecificDomainLink;

            for (Resource link : links) {
                System.out.println(link);
            }

            //Determinar qual a unidade de medicao utilizada
            String standardName = null;
            switch (name) {
                case "Summary": {
                    standardName = "WeatherDescription";
                    break;
                }
                case "InstantaneousTemperature":
                case "ApparentTemperature":
                case "DewPoint": {
                    standardName = "Celsius";
                    break;
                }
                case "Humidity":
                case "PrecipitationProbability": {
                    standardName = "Percentage";
                    break;
                }
                case "Ozone": {
                    standardName = "Dobson";
                    break;
                }
                default:
                    break;
            }
            Standard standard = new Standard(ontModel, standardName);
            standard.createIndividualAndLink(measurementIndividual);
        }

        return ontModel;
    }


    public void createInstanceSolcast() throws IOException {

        //Recuperar a classe Entity - 1 entity com 1 observation
        OntClass entityClass = ontModel.getOntClass(BASE_URI3 + "Entity"); //seleciona a classe Entity
        System.out.println("entityClass: " + entityClass);

        //Criar o individuo entity de nome Solcast
        Individual solcastIndividual = entityClass.createIndividual(BASE_URI4 + "Solcast"); // nome da instancia
        System.out.println("solcastIndividual: " + solcastIndividual);

        //Recuperar a classe Observation - 1 observation com varios measurements
        OntClass observationClass = ontModel.getOntClass(BASE_URI3 + "Observation"); //nome da classe
        Individual observationIndividual = observationClass.createIndividual(BASE_URI4 + "ObservationSolcast"); //nome do individuo


        ////////////////////Recuperar object properties ja existentes na ontologia////////////////////
        // oboe
        ObjectProperty ofEntity = ontModel.getObjectProperty(BASE_URI3 + "ofEntity");
        ObjectProperty ofCharacteristic = ontModel.getObjectProperty(BASE_URI3 + "ofCharacteristic");
        ObjectProperty hasMeasurement = ontModel.getObjectProperty(BASE_URI3 + "hasMeasurement");
        ObjectProperty hasValue = ontModel.getObjectProperty(BASE_URI3 + "hasValue");
        ObjectProperty usesStandard = ontModel.getObjectProperty(BASE_URI3 + "usesStandard");

        // wcm - equivalentes a subpropriedades de oboe:OfCharacteristic
        ObjectProperty ofGlobalHorizontalIrradiance = ontModel.getObjectProperty(BASE_URI2 + "ofGlobalHorizontalIrradiance");
        ObjectProperty ofDirectNormalIrradiance = ontModel.getObjectProperty(BASE_URI2 + "ofDirectNormalIrradiance");
        ObjectProperty ofDiffuseHorizontalIrradiance = ontModel.getObjectProperty(BASE_URI2 + "ofDiffuseHorizontalIrradiance");
        ObjectProperty ofAirTemperature = ontModel.getObjectProperty(BASE_URI2 + "ofAirTemperature");
        ObjectProperty ofZenith = ontModel.getObjectProperty(BASE_URI2 + "ofZenith");
        ObjectProperty ofAzimuth = ontModel.getObjectProperty(BASE_URI2 + "ofAzimuth");
        ObjectProperty ofCloudOpacity = ontModel.getObjectProperty(BASE_URI2 + "ofCloudOpacity");

        // wcm - equivalentes a subpropriedades de sosa:madeBySensor  (( talvez nao precise usar))
        ObjectProperty madeByOpacitySensor = ontModel.getObjectProperty(BASE_URI4 + "madeByOpacitySensor");
        ObjectProperty madeByPyrgeometer = ontModel.getObjectProperty(BASE_URI2 + "madeByPyrgeometer");
        ObjectProperty madeByMagnetometer = ontModel.getObjectProperty(BASE_URI4 + "madeByMagnetometer");
        ObjectProperty madeByMotionLightDetector = ontModel.getObjectProperty(BASE_URI4 + "madeByMotionLightDetector");
        ObjectProperty madeBySensor = ontModel.getObjectProperty(BASE_URI5 + "madeBySensor");

        // wcm - equivalente a ssn-system:hasOperatingRange
        ObjectProperty hasOperatingRange = ontModel.getObjectProperty(BASE_URI4 + "hasOperatingRange");

        // wcm - equivalente a ssn-system:hasOperatingProperty
        ObjectProperty hasOperatingProperty = ontModel.getObjectProperty(BASE_URI4 + "hasOperatingProperty");
        ////////////////////////////////////////////////////////////////////////////////////////////////////


        /////////////////////////////individuos de sensores////////////////////////////////
        OntClass sensorClass = ontModel.getOntClass(BASE_URI5 + "Sensor");
        Individual ceilometerIndividual = sensorClass.createIndividual(BASE_URI4 + "Ceilometer");
        Individual lysimeterIndividual = sensorClass.createIndividual(BASE_URI4 + "Lysimeter");
        Individual magnetometerIndividual = sensorClass.createIndividual(BASE_URI4 + "Magnetometer");
        Individual motionLightDetectorIndividual = sensorClass.createIndividual(BASE_URI4 + "MotionLightDetector");
        Individual opacitySensorIndividual = sensorClass.createIndividual(BASE_URI4 + "OpacitySensor");
        Individual pyranometerDHIIndividual = sensorClass.createIndividual(BASE_URI4 + "PyranometerDHI");
        Individual pyranometerGHIIndividual = sensorClass.createIndividual(BASE_URI4 + "PyranometerGHICV");
        Individual pyrgeometerIndividual = sensorClass.createIndividual(BASE_URI4 + "Pyrgeometer");
        Individual pyrheliometerIndividual = sensorClass.createIndividual(BASE_URI4 + "Pyrheliometer");

        //////////////////////Characteristics////////////////////////////////////////////
        OntClass globalHorizontalIrradianceClass = ontModel.getOntClass(BASE_URI2 + "GlobalHorizontalIrradiance");
        Individual ghicvIndividual = globalHorizontalIrradianceClass.createIndividual(BASE_URI4 + "GlobalHorizontalIrradianceCenterValue");
        Individual ghi10Individual = globalHorizontalIrradianceClass.createIndividual(BASE_URI4 + "GlobalHorizontalIrradiance10thPercentileValue");
        Individual ghi90Individual = globalHorizontalIrradianceClass.createIndividual(BASE_URI4 + "GlobalHorizontalIrradiance90thPercentileValue");
        OntClass directNormalIrradianceClass = ontModel.getOntClass(BASE_URI2 + "DirectNormalIrradiance");
        Individual dnicvIndividual = directNormalIrradianceClass.createIndividual(BASE_URI4 + "DirectNormalIrradianceCenterValue");
        Individual dni10Individual = directNormalIrradianceClass.createIndividual(BASE_URI4 + "DirectNormalIrradiance10thPercentileValue");
        Individual dni90Individual = directNormalIrradianceClass.createIndividual(BASE_URI4 + "DirectNormalIrradiance90thPercentileValue");
        OntClass diffuseHorizontalIrradianceClass = ontModel.getOntClass(BASE_URI2 + "DiffuseHorizontalIrradiance");
        Individual dhiIndividual = diffuseHorizontalIrradianceClass.createIndividual(BASE_URI4 + "DiffuseHorizontalIrradiance");
        OntClass airTemperatureClass = ontModel.getOntClass(BASE_URI2 + "AirTemperature");
        Individual airTemperatureIndividual = airTemperatureClass.createIndividual(BASE_URI4 + "AirTemperature");
        OntClass zenithClass = ontModel.getOntClass(BASE_URI2 + "Zenith");
        Individual zenithIndividual = zenithClass.createIndividual(BASE_URI4 + "Zenith");
        OntClass azimuthClass = ontModel.getOntClass(BASE_URI2 + "Azimuth");
        Individual azimuthIndividual = azimuthClass.createIndividual(BASE_URI4 + "Azimuth");
        OntClass cloudOpacityClass = ontModel.getOntClass(BASE_URI2 + "CloudOpacity");
        Individual cloudOpacityIndividual = cloudOpacityClass.createIndividual(BASE_URI4 + "CloudOpacity");

        //Propriedades e individuos de sensores - equivalentes ao prefixo ssn-system
        OntClass operatingRangeClass = ontModel.getOntClass(BASE_URI4 + "OperatingRange");
        Individual spectralRangeIndividual = operatingRangeClass.createIndividual(BASE_URI4 + "SpectralRange");
        Individual temperatureRangeIndividual = operatingRangeClass.createIndividual(BASE_URI4 + "TemperatureRange");
        OntClass operatingPropertyClass = ontModel.getOntClass(BASE_URI4 + "SensorProperty");
        Individual viewAngleOpacitySensorIndividual = operatingPropertyClass.createIndividual(BASE_URI4 + "ViewAngleOpacitySensor");
        Individual viewAnglePyranometerIndividual = operatingPropertyClass.createIndividual(BASE_URI4 + "ViewAnglePyranometer");
        Individual viewAnglePyrheliometerIndividual = operatingPropertyClass.createIndividual(BASE_URI4 + "ViewAnglePyrheliometer");

        // sensor property standard
        OntClass celsiusClass = ontModel.getOntClass(BASE_URI7 + "Celsius");
        Individual celsiusIndividual = celsiusClass.createIndividual(BASE_URI4 + "Celsius");
        OntClass degreeClass = ontModel.getOntClass(BASE_URI7 + "Degree");
        Individual degreeIndividual = degreeClass.createIndividual(BASE_URI4 + "Degree");
        OntClass micrometerClass = ontModel.getOntClass(BASE_URI7 + "Micrometer");
        Individual micrometerIndividual = micrometerClass.createIndividual(BASE_URI4 + "Micrometer");

        //Data properties ja existentes na ontologia
        DatatypeProperty hasCode = ontModel.getDatatypeProperty(BASE_URI3 + "hasCode");
        DatatypeProperty hasDefinition = ontModel.getDatatypeProperty(BASE_URI4 + "hasDefinition");

        //fazer o link entre os individuos Solcast e Observation atraves da object property ofEntity
        Resource ofEntityLink = observationIndividual.addProperty(ofEntity, solcastIndividual);

        ArrayList<String[]> measurements;
        measurements = SolcastMeasurements.getMeasurements();

        for (int i = 0; i < measurements.size(); i++) { // 11 medicoes

            // vetor de Individual para armazenar as instancias da ontologia
            Individual[] population = new Individual[7];

            // vetor de Resources para guardar as relacoes entre instancias
            Resource[] links = new Resource[12];

            //nome da propriedade sendo medida
            String name = measurements.get(i)[0];

            //valor da propriedade sendo medida
            String value = measurements.get(i)[1];

            //Measurement
            OntClass measurementClass = ontModel.getOntClass(BASE_URI3 + "Measurement");
            Individual measurementIndividual = measurementClass.createIndividual(BASE_URI4 + "Measurement" + name);
            population[0] = measurementIndividual;

            //link entre Observation e Measurement
            Resource hasMeasurementLink = observationIndividual.addProperty(hasMeasurement, measurementIndividual);
            links[0] = hasMeasurementLink;

            //MeasurementValue
            OntClass measurementValueClass = ontModel.getOntClass(BASE_URI3 + "MeasuredValue");
            Individual measurementValueIndividual = measurementValueClass.createIndividual(BASE_URI4 + "MeasurementValue" + name);
            population[1] = measurementValueIndividual;

            //link entre Measurement e MeasurementValue
            Resource hasValueLink = measurementIndividual.addProperty(hasValue, measurementValueIndividual);
            links[1] = hasValueLink;

            //Associar os valores obtidos na API
            if (value != null) {
                Resource hasCodeLink = measurementValueIndividual.addProperty(hasCode, value);
                links[2] = hasCodeLink;
            }

            //Determinar parametros especificos de cada medicao
            String standardName = null;
            String definition = null;
            Individual sensorIndividual = null;
            Individual operatingRangeIndividual = null;
            Individual operatingRangeStandardIndividual = null;
            Individual operatingPropertyIndividual = null;
            Individual operatingPropertyStandardIndividual = null;
            Individual characteristicIndividual = null;
            String operatingRangeValue = null;
            String operatingPropertyValue = null;
            switch (name) {
                case "GlobalHorizontalIrradianceCenterValue": {
                    standardName = "WattPerMeterSquared";
                    definition = "is the total amount of shortwave radiation received from above by a surface horizontal to the ground";
                    operatingRangeValue = "0.3 - 3";
                    operatingPropertyValue = "180";
                    sensorIndividual = pyranometerGHIIndividual;
                    operatingPropertyIndividual = viewAnglePyranometerIndividual;
                    operatingPropertyStandardIndividual = degreeIndividual;
                    operatingRangeIndividual = spectralRangeIndividual;
                    operatingRangeStandardIndividual = micrometerIndividual;
                    characteristicIndividual = ghicvIndividual;
                    break;
                }

                case "DirectNormalIrradianceCenterValue": {
                    standardName = "WattPerMeterSquared";
                    definition = " is the amount of solar radiation received per unit area by a surface that is always held perpendicular " +
                            "to the rays that come in a straight line from the direction of the sun at its current position in the sky.";
                    operatingRangeValue = "0.3 - 3";
                    operatingPropertyValue = "5";
                    sensorIndividual = pyrheliometerIndividual;
                    operatingPropertyIndividual = viewAnglePyrheliometerIndividual;
                    operatingPropertyStandardIndividual = degreeIndividual;
                    operatingRangeIndividual = spectralRangeIndividual;
                    operatingRangeStandardIndividual = micrometerIndividual;
                    characteristicIndividual = dnicvIndividual;
                    break;
                }
                case "DiffuseHorizontalIrradiance": {
                    definition = "is the amount of radiation received per unit area by a surface that does not arrive on a direct path from the sun," +
                            " but has been scattered by molecules and particles in the atmosphere and comes equally from all directions";
                    operatingRangeValue = "0.3 - 3";
                    operatingPropertyValue = "180";
                    sensorIndividual = pyranometerDHIIndividual;
                    operatingPropertyIndividual = viewAnglePyranometerIndividual;
                    operatingPropertyStandardIndividual = degreeIndividual;
                    operatingRangeIndividual = spectralRangeIndividual;
                    operatingRangeStandardIndividual = micrometerIndividual;
                    characteristicIndividual = dhiIndividual;
                    break;
                }
                case "AirTemperature": {
                    standardName = "Celsius";
                    characteristicIndividual = airTemperatureIndividual;
                    break;
                }
                case "Zenith": {
                    standardName = "Degree";
                    characteristicIndividual = zenithIndividual;
                    break;
                }
                case "Azimuth": {
                    standardName = "Degree";
                    characteristicIndividual = azimuthIndividual;
                    break;
                }
                case "CloudOpacity": {
                    definition = "The attenuation of incoming light due to cloud.";
                    characteristicIndividual = cloudOpacityIndividual;
                    sensorIndividual = opacitySensorIndividual;
                    operatingRangeValue = "-40 to 50"; // celsius
                    operatingPropertyValue = "360";
                    operatingPropertyIndividual = viewAngleOpacitySensorIndividual;
                    operatingPropertyStandardIndividual = degreeIndividual;
                    operatingRangeIndividual = temperatureRangeIndividual;
                    operatingRangeStandardIndividual = celsiusIndividual;
                    break;
                }

                default:
                    break;
            }

            if (name.equals("GlobalHorizontalIrradiance10thPercentileValue")) {
                standardName = "WattPerMeterSquared";
                definition = "is the 10% value of the total amount of shortwave radiation received from above by a surface horizontal to the ground";
                sensorIndividual = pyranometerGHIIndividual;
                characteristicIndividual = ghi10Individual;
            } else if (name.equals("GlobalHorizontalIrradiance90thPercentileValue")) {
                standardName = "WattPerMeterSquared";
                definition = "is the 90% value of the total amount of shortwave radiation received from above by a surface horizontal to the ground";
                sensorIndividual = pyranometerGHIIndividual;
                characteristicIndividual = ghi90Individual;
            } else if (name.equals("DirectNormalIrradiance10thPercentileValue")) {
                standardName = "WattPerMeterSquared";
                definition = " is 10% of the amount of solar radiation received per unit area by a surface that is always held perpendicular " +
                        "to the rays that come in a straight line from the direction of the sun at its current position in the sky.";
                sensorIndividual = pyrheliometerIndividual;
                characteristicIndividual = dni10Individual;
            } else if (name.equals("DirectNormalIrradiance90thPercentileValue")) {
                standardName = "WattPerMeterSquared";
                definition = " is 90% of the amount of solar radiation received per unit area by a surface that is always held perpendicular " +
                        "to the rays that come in a straight line from the direction of the sun at its current position in the sky.";
                sensorIndividual = pyrheliometerIndividual;
                characteristicIndividual = dni90Individual;
            }

            if (standardName != null) {
                Standard standard = new Standard(ontModel, standardName);
                standard.createIndividualAndLink(measurementIndividual);
            }

            //link entre Measurement e a dataproperty hasDefinition
            if (definition != null) {
                Resource hasDefinitionLink = measurementIndividual.addProperty(hasDefinition, definition);
                links[3] = hasDefinitionLink;
            }

            //link entre measurement e sensor
            if (sensorIndividual != null) {
                Resource madeBySensorLink = measurementIndividual.addProperty(madeBySensor, sensorIndividual);
            /*    System.out.println("madeBySensorLink:: " + madeBySensorLink);
                System.out.println("madeBySensor:: " + madeBySensor);
                System.out.println("sensorIndividual:: " + sensorIndividual);*/
                links[4] = madeBySensor;
            }

            //link entre measurement e characteristic
            if (characteristicIndividual != null) {
                Resource ofCharacteristicLink = measurementIndividual.addProperty(ofCharacteristic, characteristicIndividual);
                links[11] = ofCharacteristicLink;
            }

            if (operatingPropertyStandardIndividual != null) {
                //link entre SensorProperty e Standard
                Resource usesStandardOperatingPropertyLink = operatingPropertyIndividual.addProperty(usesStandard, operatingPropertyStandardIndividual);
               /* System.out.println("usesStandardOperatingPropertyLink:: " + usesStandardOperatingPropertyLink);
                System.out.println("operatingPropertyIndividual:: " + operatingPropertyIndividual);
                System.out.println("usesStandard:: " + usesStandard);
                System.out.println("operatingPropertyStandardIndividual:: " + operatingPropertyStandardIndividual);*/
                links[5] = usesStandardOperatingPropertyLink;

                //link entre SensorProperty e hasCode
                Resource hasOperatingPropertyCodeLink = operatingPropertyIndividual.addProperty(hasCode, operatingPropertyValue);
                links[6] = hasOperatingPropertyCodeLink;

                //link entre OperatingRange e Standard
                Resource usesStandardOperatingRangeLink = operatingRangeIndividual.addProperty(usesStandard, operatingRangeStandardIndividual);
                links[7] = usesStandardOperatingRangeLink;

                //link entre OperatingRange e hasCode
                Resource hasCodeOperatingRangeLink = operatingRangeIndividual.addProperty(hasCode, operatingRangeValue);
                links[8] = hasCodeOperatingRangeLink;

                //link entre sensor e operating property
                Resource hasOperatingPropertyLink = sensorIndividual.addProperty(hasOperatingProperty, operatingPropertyIndividual);
                links[9] = hasOperatingPropertyLink;

                //link entre sensor e operating range
                Resource hasOperatingRangeLink = sensorIndividual.addProperty(hasOperatingRange, operatingRangeIndividual);
                links[10] = hasOperatingRangeLink;

            }

            for (Resource resource : links){
                if (resource!=null){
                    System.out.println(resource.toString());
                }
            }

        }


    }


}
