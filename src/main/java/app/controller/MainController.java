package app.controller;

import app.dao.OntologyAccess;
import app.dao.SparqlQuerying;
import app.model.IntegratedData;
import app.model.Measurement;
import app.model.Provider;
import app.service.RdfReading;
import app.service.darksky.DarkSkyReading;
import app.service.GsonWriting;
import app.service.solcast.SolcastReading;
import app.util.OntologyDataCreation;
import com.google.gson.Gson;
import app.model.DefaultRestResult;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.jena.ontology.OntModel;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class MainController {

    private String providers;
    private String latitude;
    private String longitude;

    @GetMapping(value = "/", produces = "application/json")
    public String index() {
        // Init required objects
        Gson gson = new Gson();
        DefaultRestResult restResult = new DefaultRestResult();

        // WebService Body
        ArrayList<String> linksList = new ArrayList<>();
        linksList.add("/solcast");
        linksList.add("/darksky");
        restResult.setLinks(linksList);

        // Return the object as a json string
        return gson.toJson(restResult);
    }


    @CrossOrigin
    @RequestMapping(value = "/getdata", method = RequestMethod.GET, produces = "application/json")
    public String getData(@RequestParam(value = "providers") String providers,
                          @RequestParam(value = "latitude") String latitude,
                          @RequestParam(value = "longitude") String longitude) {


        String[] providersList = providers.split(",");


        // I created an array to receive multiple coordinates, but will consider only one location - index 0
        String[] latitudeList = latitude.split(",");
        String[] longitudeList = longitude.split(",");


        IntegratedData integratedData = new IntegratedData();

        Gson gson = new Gson();
        DefaultRestResult restResult = new DefaultRestResult();

        // WebService Body
        try {

            OntModel om = OntologyAccess.loadOntologyModelFromUrl("http://datawebhost.com.br/ontologies/merged-wcm.owl");
            OntologyDataCreation odc = new OntologyDataCreation(om);

            if (providersList.length == 1) {

                Provider provider = new Provider();

                if (providersList[0].equals("solcast")) {
                    provider = callSolcast(latitudeList, longitudeList, 0);
                    odc.createInstanceSolcast();
                } else if (providersList[0].equals("darksky")) {
                    provider = callDarksky(latitudeList, longitudeList, 0);
                    odc.createInstanceDarkSky();
                }

                integratedData = getData(odc, provider);

            }


            if (providersList.length == 2) {
                Provider solcast = callSolcast(latitudeList, longitudeList, 0);
                odc.createInstanceSolcast();

                Provider darksky = callDarksky(latitudeList, longitudeList, 0);
                odc.createInstanceDarkSky();

                integratedData = getData(odc, solcast, darksky);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Convert solcastBean to json and set it for restResult
        JsonElement jsonElement = gson.toJsonTree(integratedData);
        JsonObject jsonObject = (JsonObject) jsonElement;
        restResult.setJsonData(jsonObject);

        // Return the object as a json string
        return gson.toJson(restResult);
    }


    private static Provider callSolcast(String[] latitudeList, String[] longitudeList, int index) {
        Provider solcast = new Provider();
        solcast.setType("SOLCAST");
        SolcastReading sr = new SolcastReading(latitudeList[index], longitudeList[index]);
        return solcast;
    }

    private static Provider callDarksky(String[] latitudeList, String[] longitudeList, int index) {
        Provider darksky = new Provider();
        darksky.setType("DARKSKY");
        DarkSkyReading dsr = new DarkSkyReading(latitudeList[index], longitudeList[index]);
        return darksky;
    }


    private static IntegratedData getData(OntologyDataCreation odc, Provider... providers) throws IOException {
        org.apache.jena.rdf.model.Model finalOntology = odc.writeOntology();

        SparqlQuerying sparqlQuerying = new SparqlQuerying();

        sparqlQuerying.selectQueriesAccordingToProvider(providers);

        ArrayList<String> sparqlList = sparqlQuerying.getResultList(finalOntology);
        RdfReading rdfReading = new RdfReading();
        rdfReading.readRDF(sparqlList);
        List<Measurement> measurementList = GsonWriting.writeGSON(rdfReading);
        IntegratedData integratedData = new IntegratedData();
        integratedData.setMeasurements(measurementList);

        return integratedData;
    }

}
