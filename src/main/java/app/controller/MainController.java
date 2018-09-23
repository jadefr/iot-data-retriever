package app.controller;

import app.model.solcast.SolcastBean;
import app.model.solcast.SolcastGSONWriting;
import com.google.gson.Gson;
import app.model.DefaultRestResult;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;

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

    @GetMapping(value = "/solcast", produces = "application/json")
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

    /*
    public DarkSky getDarkSky() throws IOException {
        DarkSkyJSONReading dsjr = new DarkSkyJSONReading();
        return DarkSkyJSONReading.readDarkSky();
    }
    */
}
