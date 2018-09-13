package controller;

import dao.SolcastSPARQL;
import util.darksky.DarkSky;
import util.darksky.DarkSkyJSONReading;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;

@RestController
@RequestMapping("/dataintegration")
public class Controller {

    @RequestMapping("/solcast")
    public ArrayList getSolcast() throws IOException {
        ArrayList solcastList = SolcastSPARQL.getSolcastFromOntology();
        return solcastList;
    }

    @RequestMapping("/darksky")
    public DarkSky getDarkSky() throws IOException {
        DarkSkyJSONReading dsjr = new DarkSkyJSONReading();
        return DarkSkyJSONReading.readDarkSky();
    }

    @RequestMapping("/teste")
    public String getTest(){
        return "testando";
    }

    @RequestMapping("/hello/{name}")
    public String hello(@PathVariable String name) {
        return "Hello, " + name + "!";
    }


}
