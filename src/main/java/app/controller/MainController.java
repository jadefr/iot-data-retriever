package app.controller;

import app.dao.OntologyAccess;
import app.model.DataSource;
import app.model.solcast.SolcastBean;
import app.service.solcast.Solcast;
import app.service.solcast.SolcastGSONWriting;
import app.service.solcast.SolcastReading;
import app.util.OntologyDataCreation;
import com.google.gson.Gson;
import app.model.DefaultRestResult;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.jena.ontology.OntModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import tk.plogitech.darksky.forecast.*;
import tk.plogitech.darksky.forecast.model.Latitude;
import tk.plogitech.darksky.forecast.model.Longitude;

import javax.validation.Valid;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

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


   @GetMapping(value = "/solcastNoCoordinates", produces = "application/json")
    //public SolcastBean solcast() throws IOException {
    public String solcast() {
        // Init required objects
        Gson gson = new Gson();
        DefaultRestResult restResult = new DefaultRestResult();

        // WebService Body
        try {
            SolcastBean solcastBean = new SolcastBean();
            SolcastGSONWriting solcastGSONWriting = new SolcastGSONWriting();
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
    }


    @RequestMapping(value = "/solcast", method = RequestMethod.GET, produces = "application/json")
    public String getDataSourceDetails(@RequestParam(value = "latitude", required = true) String latitude,
                                       @RequestParam(value = "longitude", required = true) String longitude) throws IOException {

        Gson gson = new Gson();
        DefaultRestResult restResult = new DefaultRestResult();

         // WebService Body
        try {

            OntologyAccess oa = new OntologyAccess();
            OntModel om = oa.loadOntologyModelFromUrl("http://datawebhost.com.br/ontologies/merged-wcm.owl");
            SolcastReading sr = new SolcastReading(latitude, longitude);
            OntologyDataCreation odc = new OntologyDataCreation(om);
            odc.createInstanceSolcast();
            odc.writeOntology();




            SolcastBean solcastBean = new SolcastBean();
            SolcastGSONWriting solcastGSONWriting = new SolcastGSONWriting();
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
    }

    @GetMapping("/greeting")
    public String greeting(@RequestParam(name = "name", required = false, defaultValue = "World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting " + name;
    }

}
