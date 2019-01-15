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

import java.awt.*;
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
    private final String path_out = "C:\\";

    public OntologyDataCreation(OntModel ontModel) {
        this.ontModel = ontModel;
    }


    public Model writeOntology() throws IOException {
        //metodo para criar a nova ontologia, apos a criacao de instancias pelos metodos abaixo

        OutputStream out = new FileOutputStream(path_out + "\\oboe_wcm_integrated.rdf");
        //OutputStream out = new FileOutputStream("\\oboe_wcm_integrated.rdf");
        Model finalOntology = ontModel.write(out, "RDF/XML-ABBREV");
        out.close();
        //System.out.println("FINAL ONTOLOGY: " + finalOntology.toString());
        return finalOntology;
    }

  /*  public static void createInstanceDarkSky() throws IOException {

        //Individuo da classe Entity de nome DarkSky
        OntClass darkSkyClass = ontModel.getOntClass(BASE_URI3 + "Entity"); //seleciona a classe Entity
        Individual darkSkyIndividual = darkSkyClass.createIndividual(BASE_URI4 + "DarkSky"); // nome da instancia
        //System.out.println("darkSkyIndividual: " + darkSkyIndividual);

        //Individuo da classe Observation
        OntClass observationClass = ontModel.getOntClass(BASE_URI3 + "Observation");
        Individual observationIndividual = observationClass.createIndividual(BASE_URI4 + "Observation");

        //Recuperar object properties ja existentes na ontologia
        ObjectProperty ofEntity = ontModel.getObjectProperty(BASE_URI3 + "ofEntity");
        ObjectProperty hasMeasurement = ontModel.getObjectProperty(BASE_URI3 + "hasMeasurement");
        ObjectProperty hasValue = ontModel.getObjectProperty(BASE_URI3 + "hasValue");
        ObjectProperty ofCharacteristic = ontModel.getObjectProperty(BASE_URI3 + "ofCharacteristic");

        //Data property ja existentes na ontologia
        DatatypeProperty hasCode = ontModel.getDatatypeProperty(BASE_URI3 + "hasCode");

        //fazer o link entre darksky e observation atraves da object property ofEntity
        Resource ofEntityLink = observationIndividual.addProperty(ofEntity, darkSkyIndividual);

        ArrayList<String[]> measurements = DarkSkyMeasurements.getMeasurements();

        for (int i = 1; i < measurements.size(); i++) {

            // vetor de Individual para armazenar as instancias da ontologia
            Individual[] population = new Individual[11];

            // vetor de Resources para guardar as relacoes entre instancias
            Resource[] links = new Resource[12];

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

            for (Individual individual: population) {
                System.out.println(individual);
            }

            //link entre Measurement e MeasuredCharacteristic
            Resource ofCharacteristicLink = measurementIndividual.addProperty(ofCharacteristic, measuredCharacteristicIndividual);
            links[3] = ofCharacteristicLink;

            //Specific domain
            OntClass specificDomainClass = ontModel.getOntClass(BASE_URI2 + name);
            System.out.println("specificDomainClass: " + specificDomainClass);
            Individual specificDomainIndividual = specificDomainClass.createIndividual(BASE_URI1 + name);
            System.out.println("specificDomainIndividual: " + specificDomainIndividual);
            population[3] = specificDomainIndividual;


            //object property do specific domain
            //ObjectProperty hasSpecificDomain = ontModel.getObjectProperty(BASE_URI1 + "has" + name);
            ObjectProperty hasSpecificDomain = ontModel.createObjectProperty(BASE_URI4 + "has" + name);
            System.out.println("hasSpecificDomain: " + hasSpecificDomain);

            //link entre Measurement e Specific domain
            Resource hasSpecificDomainLink = measurementIndividual.addProperty(hasSpecificDomain, specificDomainIndividual);
            System.out.println("hasSpecificDomainLink: " + hasSpecificDomainLink);
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

    }*/


    public void createInstanceDarkSky() throws IOException {

        OntClass entityClass = ontModel.getOntClass(BASE_URI3 + "Entity");
        Individual darkskyIndividual = entityClass.createIndividual(BASE_URI4 + "DarkSky");

        OntClass observationClass = ontModel.getOntClass(BASE_URI3 + "Observation");
        Individual observationIndividual = observationClass.createIndividual(BASE_URI4 + "ObservationDarkSky");

        // recuperar as object properties
        ObjectProperty ofEntity = ontModel.getObjectProperty(BASE_URI3 + "ofEntity");
        ObjectProperty ofCharacteristic = ontModel.getObjectProperty(BASE_URI3 + "ofCharacteristic");
        ObjectProperty hasMeasurement = ontModel.getObjectProperty(BASE_URI3 + "hasMeasurement");
        ObjectProperty hasValue = ontModel.getObjectProperty(BASE_URI3 + "hasValue");
        ObjectProperty usesStandard = ontModel.getObjectProperty(BASE_URI3 + "usesStandard");
        ObjectProperty madeBySensor = ontModel.getObjectProperty(BASE_URI5 + "madeBySensor");
        ObjectProperty hasOperatingProperty = ontModel.getObjectProperty(BASE_URI4 + "hasOperatingProperty");


        // sensores
        OntClass sensorClass = ontModel.getOntClass(BASE_URI5 + "Sensor");
        Individual rainGaugeIndividual = sensorClass.createIndividual(BASE_URI4 + "RainGauge");
        Individual thermometerIndividual = sensorClass.createIndividual(BASE_URI4 + "Thermometer");
        Individual hygrometerIndividual = sensorClass.createIndividual(BASE_URI4 + "Hygrometer");
        Individual barometerIndividual = sensorClass.createIndividual(BASE_URI4 + "Barometer");
        Individual anemometerIndividual = sensorClass.createIndividual(BASE_URI4 + "Anemometer");
        Individual satelliteImageryIndividual = sensorClass.createIndividual(BASE_URI4 + "SatelliteImagery");
        Individual visibilitySensorIndividual = sensorClass.createIndividual(BASE_URI4 + "VisibilitySensor");
        Individual dobsonSpectrophotometerIndividual = sensorClass.createIndividual(BASE_URI4 + "DobsonSpectrophotometer");

        // sensor property
        OntClass operatingPropertyClass = ontModel.getOntClass(BASE_URI4 + "SensorProperty");
        Individual accuracyIndividual = operatingPropertyClass.createIndividual(BASE_URI4 + "Accuracy");
        Individual resolutionIndividual = operatingPropertyClass.createIndividual(BASE_URI4 + "Resolution");
        Individual maximumIntensityIndividual = operatingPropertyClass.createIndividual(BASE_URI4 + "MaximumIntensity");


       /* // sensor property standard
        OntClass celsiusClass = ontModel.getOntClass(BASE_URI7 + "Celsius");
        Individual celsiusStandard = celsiusClass.createIndividual("Celsius");
        OntClass millimeterPerHourClass = ontModel.createClass(BASE_URI4 + "MillimeterPerHour");
        Individual millimeterPerHourIndividual = millimeterPerHourClass.createIndividual(BASE_URI4 + "MillimeterPerHour");*/

        // characteristics
        OntClass precipitationIntensityClass = ontModel.createClass(BASE_URI4 + "PrecipitationIntensity");
        Individual precipitationIntensityIndividual = precipitationIntensityClass.createIndividual(BASE_URI4 + "PrecipitationIntensity");
        OntClass precipitationProbabilityClass = ontModel.createClass(BASE_URI4 + "PrecipitationProbability");
        Individual precipitationProbabilityIndividual = precipitationProbabilityClass.createIndividual(BASE_URI4 + "PrecipitationProbability");
        OntClass temperatureClass = ontModel.createClass(BASE_URI4 + "Temperature");
        Individual temperatureIndividual = temperatureClass.createIndividual(BASE_URI4 + "Temperature");
        OntClass apparentTemperatureClass = ontModel.createClass(BASE_URI4 + "ApparentTemperature");
        Individual apparentTemperatureIndividual = apparentTemperatureClass.createIndividual(BASE_URI4 + "ApparentTemperature");
        OntClass dewPointClass = ontModel.createClass(BASE_URI4 + "DewPoint");
        Individual dewPointIndividual = dewPointClass.createIndividual(BASE_URI4 + "DewPoint");
        OntClass humidityClass = ontModel.createClass(BASE_URI4 + "Humidity");
        Individual humidityIndividual = humidityClass.createIndividual(BASE_URI4 + "Humidity");
        OntClass pressureClass = ontModel.createClass(BASE_URI4 + "Pressure");
        Individual pressureIndividual = pressureClass.createIndividual(BASE_URI4 + "Pressure");
        OntClass windSpeedClass = ontModel.createClass(BASE_URI4 + "WindSpeed");
        Individual windSpeedIndividual = windSpeedClass.createIndividual(BASE_URI4 + "WindSpeed");
        OntClass windGustClass = ontModel.createClass(BASE_URI4 + "WindGust");
        Individual windGustIndividual = windGustClass.createIndividual(BASE_URI4 + "WindGust");
        OntClass windBearingClass = ontModel.createClass(BASE_URI4 + "WindBearing");
        Individual windBearingIndividual = windBearingClass.createIndividual(BASE_URI4 + "WindBearing");
        OntClass cloudCoverClass = ontModel.createClass(BASE_URI4 + "CloudCover");
        Individual cloudCoverIndividual = cloudCoverClass.createIndividual(BASE_URI4 + "CloudCover");
        OntClass uvIndexClass = ontModel.createClass(BASE_URI4 + "UvIndex");
        Individual uvIndexIndividual = uvIndexClass.createIndividual(BASE_URI4 + "UvIndex");
        OntClass visibilityClass = ontModel.createClass(BASE_URI4 + "Visibility");
        Individual visibilityIndividual = visibilityClass.createIndividual(BASE_URI4 + "Visibility");
        OntClass ozoneClass = ontModel.createClass(BASE_URI4 + "Ozone");
        Individual ozoneIndividual = ozoneClass.createIndividual(BASE_URI4 + "Ozone");

        //data properties ja existentes na ontologia
        DatatypeProperty hasCode = ontModel.getDatatypeProperty(BASE_URI3 + "hasCode");
        DatatypeProperty hasDefinition = ontModel.getDatatypeProperty(BASE_URI4 + "hasDefinition");

        //link entre DarkSky (entity) e Observation
        Resource ofEntityLink = observationIndividual.addProperty(ofEntity, darkskyIndividual); // Observation ofEntity DarkSky

        ArrayList<String[]> measurements = DarkSkyMeasurements.getMeasurements();

        for (int i = 0; i < measurements.size(); i++) {

            Individual[] population = new Individual[10];
            Resource[] links = new Resource[10];

            String name = measurements.get(i)[0];
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

            //associar os valores obtidos na API
            if (value != null) {
                Resource hasCodeLink = measurementValueIndividual.addProperty(hasCode, value);
                links[2] = hasCodeLink;
            }

            //determinar parametros especificos de cada medicao
            String standardName = null;
            String definition = null;
            Individual sensorIndividual = null;
            Individual operatingPropertyIndividual = null;
            Individual operatingPropertyStandardIndividual = null;
            Individual characteristicIndividual = null;
            String operatingPropertyValue = null;
            switch (name) {
                case "PrecipitationIntensity": {
                    standardName = "MillimeterPerHour";
                    definition = "Precipitation intensity is the amount of rain that falls over time. The intensity of rain is measured in the height of the water layer covering the ground in a period of time.";
                    operatingPropertyValue = "144";
                    sensorIndividual = rainGaugeIndividual;
                    characteristicIndividual = precipitationIntensityIndividual;
                    break;
                }
                case "PrecipitationProbability": {
                    standardName = "Percentage"; //Relative
                    definition = "Precipitation probability is the forecast of 0.01 in of liquid equivalent precipitation at a specific point over a specific period of time.";
                    characteristicIndividual = precipitationProbabilityIndividual;
                    break;
                }
                case "InstantaneousTemperature": {
                    standardName = "Celsius";
                    sensorIndividual = thermometerIndividual;
                    definition = "Air temperature is a measure of how hot or cold the air is. More specifically, temperature describes the kinetic energy of the gases that constitute the air." +
                            "As gas molecules move more quickly, air temperature increases.";
                    characteristicIndividual = temperatureIndividual;
                    break;
                }
                case "ApparentTemperature": {
                    standardName = "Celsius";
                    definition = "Apparent temperature is the temperature equivalent perceived by humans, caused by the combined effects of air temperature, relative humidity, and wind speed.";
                    characteristicIndividual = apparentTemperatureIndividual;
                    break;
                }
                case "DewPoint": {
                    standardName = "Celsius";
                    sensorIndividual = hygrometerIndividual;
                    definition = "Dew point is the temperature to which air must be cooled to become saturated with water vapour. In other words, dew point is the temperature at which dew form";
                    operatingPropertyIndividual = accuracyIndividual;
                    operatingPropertyValue = "0.2";
                    //operatingPropertyStandardIndividual = celsiusStandard;
                    characteristicIndividual = dewPointIndividual;
                    break;
                }
                case "Humidity": {
                    standardName = "Relative";
                    sensorIndividual = hygrometerIndividual;
                    definition = "Relative humidity is the ratio of the partial pressure of water vapour to the equilibrium vapour pressure of water at a given temperature. In other words, it is" +
                            "the amount of moisture in the air compared to what the air can \"hold\" at that temperature. When the air cannot \"hold\" all the moisture, then it condenses as dew.";
                    characteristicIndividual = humidityIndividual;
                    break;
                }
                case "Pressure": {
                    standardName = "Hectopascal";
                    sensorIndividual = barometerIndividual;
                    definition = "Corresponds to the pressure exerted by the earth's atmosphere at sea level.";
                    characteristicIndividual = pressureIndividual;
                    break;
                }
                case "WindSpeed": {
                    standardName = "MeterPerSecond";
                    sensorIndividual = anemometerIndividual;
                    definition = "Wind Speed, or wind flow velocity, is a fundamental atmospheric quantity caused by air moving from high to low pressure, usually due to changes in temperature.";
                    characteristicIndividual = windSpeedIndividual;
                    break;
                }
                case "WindGust": {
                    standardName = "MeterPerSecond";
                    sensorIndividual = anemometerIndividual;
                    definition = "Wind gust is a sudden, brief increase in the speed of the wind. The duration of a gust is usually less than 20 seconds.";
                    characteristicIndividual = windGustIndividual;
                    break;
                }
                case "WindBearing": {
                    standardName = "Degree";
                    sensorIndividual = anemometerIndividual;
                    definition = "Wind bearing is the direction that the wind is coming from in degrees, with true north at 0" + "\u00b0" + "and progressing clockwise. If wind speed is 0, then this value will not be defined";
                    characteristicIndividual = windBearingIndividual;
                    break;
                }
                case "CloudCover": {
                    standardName = "Relative";
                    sensorIndividual = satelliteImageryIndividual;
                    definition = "Cloud cover is the fraction of the sky obscured by clouds when observed from a particular location.";
                    characteristicIndividual = cloudCoverIndividual;
                    break;
                }
                case "UvIndex": {
                    definition = "UV Index is an international standard measurement of the strength of sunburn-producing ultraviolet (UV) radiation at a particular place and time.";
                    characteristicIndividual = uvIndexIndividual;
                    break;
                }
                case "Visibility": {
                    standardName = "Kilometer";
                    sensorIndividual = visibilitySensorIndividual;
                    definition = "Visibility is a measure of the distance at which an object or light can be clearly discerned.";
                    break;
                }
                case "Ozone": {
                    standardName = "Dobson";
                    sensorIndividual = dobsonSpectrophotometerIndividual;
                    definition = "Corresponds to the columnar density of total atmospheric ozone at the given time.";
                    break;
                }

                default:
                    break;
            }

            if (standardName != null) {
                Standard.createIndividualAndLink(ontModel, standardName, measurementIndividual);
            }

            //link entre Measurement e hasDefinition
            if (definition != null) {
                Resource hasDefinitionLink = measurementIndividual.addProperty(hasDefinition, definition);
                links[3] = hasDefinitionLink;
            }

            //link entre measurement e sensor
            if (sensorIndividual != null) {
                Resource madeBySensorLink = measurementIndividual.addProperty(madeBySensor, sensorIndividual);
                links[4] = madeBySensorLink;
            }

            //link entre measurement e characteristic
            if (characteristicIndividual != null) {
                Resource ofCharacteristicLink = measurementIndividual.addProperty(ofCharacteristic, characteristicIndividual);
                links[5] = ofCharacteristicLink;
            }

            if (operatingPropertyStandardIndividual != null) {
                //link entre SensorProperty e Standard
                Resource usesStandardOperatingPropertyLink = operatingPropertyIndividual.addProperty(usesStandard, operatingPropertyStandardIndividual);
                links[6] = usesStandardOperatingPropertyLink;

                //link entre SensorProperty e hasCode
                Resource hasOperatingPropertyCodeLink = operatingPropertyIndividual.addProperty(hasCode, operatingPropertyValue);
                links[7] = hasOperatingPropertyCodeLink;

                //link entre sensor e operating property
                Resource hasOperatingPropertyLink = sensorIndividual.addProperty(hasOperatingProperty, operatingPropertyIndividual);
                links[8] = hasOperatingPropertyLink;
            }

            /*for (Resource resource : links) {
                if (resource != null) {
                    System.out.println(resource.toString());
                }
            }*/


        }

    }

    public void createInstanceSolcast() throws IOException {

        //Recuperar a classe Entity - 1 entity com 1 observation
        OntClass entityClass = ontModel.getOntClass(BASE_URI3 + "Entity"); //seleciona a classe Entity
        //System.out.println("entityClass: " + entityClass);

        //Criar o individuo entity de nome Solcast
        Individual solcastIndividual = entityClass.createIndividual(BASE_URI4 + "Solcast"); // nome da instancia
        //System.out.println("solcastIndividual: " + solcastIndividual);

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
                Standard.createIndividualAndLink(ontModel, standardName, measurementIndividual);
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

            /*for (Resource resource : links) {
                if (resource != null) {
                    System.out.println(resource.toString());
                }
            }*/

        }


    }


}
