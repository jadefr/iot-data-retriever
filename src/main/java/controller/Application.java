package controller;

import com.google.gson.Gson;
import dao.OntologyAccess;
import dao.SolcastSPARQL;
import model.solcastmodel.SolcastGSONWriting;
import model.solcastmodel.SolcastRDFReading;
import org.apache.jena.ontology.OntModel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import util.OntologyDataCreation;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

@SpringBootApplication
public class Application {
    public static void main(String[] args) throws IOException {

        String path = "src/main/files/";
        OntModel ontModel = OntologyAccess.loadOntologyModel(path, "merged-wcm.owl");
        System.out.println(ontModel.toString());

        OntologyDataCreation odc = new OntologyDataCreation();
        OntModel om = OntologyDataCreation.createInstanceSolcast(ontModel);
        odc.writeOntology(om);

        SpringApplication.run(Application.class, args);

    }
}
