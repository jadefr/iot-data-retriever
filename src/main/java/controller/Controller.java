package controller;

import dao.SolcastSPARQL;
import model.solcastmodel.SolcastBean;
import model.solcastmodel.SolcastGSONWriting;
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
    public SolcastBean getSolcast() throws IOException {
        SolcastBean solcastBean = new SolcastBean();
        SolcastGSONWriting solcastGSONWriting = new SolcastGSONWriting();
        solcastBean.setMeasurements(solcastGSONWriting.writeGSON());
        return solcastBean;
    }

    @RequestMapping("/darksky")
    public DarkSky getDarkSky() throws IOException {
        DarkSkyJSONReading dsjr = new DarkSkyJSONReading();
        return DarkSkyJSONReading.readDarkSky();
    }

  /*  @RequestMapping("/teste")
    public String getTest() {
        return "testando";
    }

    @RequestMapping("/hello/{name}")
    public String hello(@PathVariable String name) {
        return "Hello, " + name + "!";
    }
*/

}
