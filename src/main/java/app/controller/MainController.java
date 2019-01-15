package app.controller;

import app.dao.OntologyAccess;
import app.dao.SparqlQuerying;
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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class MainController {

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


  /* @GetMapping(value = "/solcastNoCoordinates", produces = "application/json")
    //public Provider solcast() throws IOException {
    public String solcast() {
        // Init required objects
        Gson gson = new Gson();
        DefaultRestResult restResult = new DefaultRestResult();

        // WebService Body
        try {
            Provider solcastBean = new Provider();
            GsonWriting solcastGSONWriting = new GsonWriting();
            solcastBean.setMeasurements(solcastGSONWriting.writeGSON());

            // Convert solcastBean to json and set it for restResult
            JsonElement jsonElement = gson.toJsonTree(solcastBean);
            JsonObject jsonObject = (JsonObject) jsonElement;
            restResult.setJsonData(jsonObject);

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Return the object as a json string
        return gson.toJson(restResult);
    }*/


    @RequestMapping(value = "/solcast", method = RequestMethod.GET, produces = "application/json")
    public String getDataSourceDetails(@RequestParam(value = "latitude", required = true) String latitude,
                                       @RequestParam(value = "longitude", required = true) String longitude) throws IOException {

        Gson gson = new Gson();
        DefaultRestResult restResult = new DefaultRestResult();

        // WebService Body
        try {

            OntModel om = OntologyAccess.loadOntologyModelFromUrl("http://datawebhost.com.br/ontologies/merged-wcm.owl");
            SolcastReading sr = new SolcastReading(latitude, longitude);
            OntologyDataCreation odc = new OntologyDataCreation(om);
            odc.createInstanceSolcast();
            org.apache.jena.rdf.model.Model finalOntology = odc.writeOntology();
            ArrayList<String> sparqlList = SparqlQuerying.getDataFromOntology(finalOntology);
            RdfReading rdfReading = new RdfReading();
            rdfReading.writeRDF(sparqlList);
            List<Measurement> measurementList = GsonWriting.writeGSON(rdfReading);
            Provider provider = new Provider();
            provider.setMeasurements(measurementList);

            // Convert provider to json and set it for restResult
            JsonElement jsonElement = gson.toJsonTree(provider);
            JsonObject jsonObject = (JsonObject) jsonElement;
            restResult.setJsonData(jsonObject);

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Return the object as a json string
        return gson.toJson(restResult);
    }

    @RequestMapping(value = "/darksky", method = RequestMethod.GET, produces = "application/json")
    public String getDarkSky(@RequestParam(value = "latitude", required = true) String latitude,
                                       @RequestParam(value = "longitude", required = true) String longitude) throws IOException {

        Gson gson = new Gson();
        DefaultRestResult restResult = new DefaultRestResult();

       // WebService Body
        try {

            DarkSkyReading dsr = new DarkSkyReading(latitude, longitude);
            OntModel om = OntologyAccess.loadOntologyModelFromUrl("http://datawebhost.com.br/ontologies/merged-wcm.owl");
            OntologyDataCreation odc = new OntologyDataCreation(om);
            odc.createInstanceDarkSky();
            org.apache.jena.rdf.model.Model finalOntology = odc.writeOntology();
            System.out.println(finalOntology);

            ArrayList<String> sparqlList = SparqlQuerying.getDarkSkyFromOntology(finalOntology);
            RdfReading rdfReading = new RdfReading();
            rdfReading.writeRDF(sparqlList);
            List<Measurement> measurementList = GsonWriting.writeGSON(rdfReading);
            Provider provider = new Provider();
            provider.setMeasurements(measurementList);


            // Convert solcastBean to json and set it for restResult
            JsonElement jsonElement = gson.toJsonTree(provider);
            JsonObject jsonObject = (JsonObject) jsonElement;
            restResult.setJsonData(jsonObject);

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Return the object as a json string
        return gson.toJson(restResult);
    }


    @RequestMapping(value = "/getdata", method = RequestMethod.GET, produces = "application/json")
    public String getData(@RequestParam(value = "providers", required = true) String providers,
                          @RequestParam(value = "latitude", required = true) String latitude,
                          @RequestParam(value = "longitude", required = true) String longitude) {

        String[] providersList = providers.split(",");
        String[] latitudeList = latitude.split(",");
        String[] longitudeList = longitude.split(",");

       /* for (int i = 0; i < providersList.length; i++) {
            System.out.println("Processando um Provider e suas coordenadas");
            System.out.println(providersList[i]);
            System.out.println(latitudeList[i]);
            System.out.println(longitudeList[i]);
        }*/

        Gson gson = new Gson();
        DefaultRestResult restResult = new DefaultRestResult();

        // WebService Body
        try {

            OntModel om = OntologyAccess.loadOntologyModelFromUrl("http://datawebhost.com.br/ontologies/merged-wcm.owl");
            OntologyDataCreation odc = new OntologyDataCreation(om);
            SolcastReading sr = new SolcastReading(latitudeList[0], longitudeList[0]);
            odc.createInstanceSolcast();
            DarkSkyReading dsr = new DarkSkyReading(latitudeList[1], longitudeList[1]);
            odc.createInstanceDarkSky();
            org.apache.jena.rdf.model.Model finalOntology = odc.writeOntology();
            System.out.println(finalOntology);

            ArrayList<String> sparqlList = SparqlQuerying.getDarkSkyFromOntology(finalOntology); //darksky
            RdfReading rdfReading = new RdfReading();
            rdfReading.writeRDF(sparqlList);
            List<Measurement> measurementList = GsonWriting.writeGSON(rdfReading);
            Provider provider = new Provider();
            provider.setMeasurements(measurementList);


            // Convert solcastBean to json and set it for restResult
            JsonElement jsonElement = gson.toJsonTree(provider);
            JsonObject jsonObject = (JsonObject) jsonElement;
            restResult.setJsonData(jsonObject);

        } catch (IOException e) {
            e.printStackTrace();
        }


        // Return the object as a json string
        return gson.toJson(restResult);
    }

    @GetMapping("/greeting")
    public String greeting(@RequestParam(name = "name", required = false, defaultValue = "World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting " + name;
    }

}
