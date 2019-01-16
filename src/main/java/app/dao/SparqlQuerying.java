package app.dao;

import app.model.Provider;
import com.google.gson.Gson;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.query.*;
import java.lang.*;

import java.io.IOException;
import java.util.ArrayList;

public class SparqlQuerying {

    private final String solcastQuery = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
            + "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
            + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
            + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
            + "PREFIX oboe-core: <http://ecoinformatics.org/oboe/oboe.1.2/oboe-core.owl#>\n"
            + "PREFIX merged-wcm: <http://www.semanticweb.org/jadef/ontologies/2018/8/merged-wcm#>\n"
            + "PREFIX sosa: <http://www.w3.org/ns/sosa/>\n"
            + "SELECT ?measurement ?measurementValue ?sensor ?characteristic ?operatingProperty ?definition ?operatingRange ?x ?y ?z ?standard ?operatingStandard ?rangeStandard ?observation ?entity\n"
            + " 	WHERE { ?measurement oboe-core:hasValue ?measurementValue.\n" +
            "               ?measurement oboe-core:usesStandard ?standard.\n" +
            "               ?measurementValue oboe-core:hasCode ?x.\n" +
            "               ?measurement sosa:madeBySensor ?sensor.\n" +
            "               ?measurement oboe-core:ofCharacteristic ?characteristic.\n" +
            "               ?sensor merged-wcm:hasOperatingProperty ?operatingProperty.\n" +
            "               ?operatingProperty oboe-core:usesStandard ?operatingStandard.\n" +
            "               ?sensor merged-wcm:hasOperatingRange ?operatingRange.\n" +
            "               ?operatingRange oboe-core:usesStandard ?rangeStandard.\n" +
            "               ?measurement merged-wcm:hasDefinition ?definition.\n" +
            "               ?operatingProperty oboe-core:hasCode ?y.\n" +
            "               ?observation oboe-core:ofEntity ?entity.\n" +
            "               ?operatingRange oboe-core:hasCode ?z}";

    private final String darkSkyQuery = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
            + "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
            + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
            + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
            + "PREFIX oboe-core: <http://ecoinformatics.org/oboe/oboe.1.2/oboe-core.owl#>\n"
            + "PREFIX merged-wcm: <http://www.semanticweb.org/jadef/ontologies/2018/8/merged-wcm#>\n"
            + "PREFIX sosa: <http://www.w3.org/ns/sosa/>\n"
            + "SELECT ?measurement ?measurementValue ?sensor ?characteristic ?definition ?x ?y ?z ?standard ?observation ?entity \n"
            + " 	WHERE { ?measurement oboe-core:hasValue ?measurementValue.\n" +
            "               ?measurement oboe-core:usesStandard ?standard.\n" +
            "               ?measurementValue oboe-core:hasCode ?x.\n" +
            "               ?measurement sosa:madeBySensor ?sensor.\n" +
            "               ?measurement oboe-core:ofCharacteristic ?characteristic.\n" +
            "               ?observation oboe-core:ofEntity ?entity.\n" +
            "               ?measurement merged-wcm:hasDefinition ?definition}";

    public ArrayList<String> getDataFromOntology(Model model, Provider... provider) throws IOException {

        String query1 = "";
        String query2 = "";
        int length = provider.length;

        if (provider[0].getType() == "SOLCAST") {
            query1 = solcastQuery;
            query2 = darkSkyQuery;
        } else if (provider[0].getType() == "DARKSKY") {
            query1 = darkSkyQuery;
            query2 = solcastQuery;
        }

        Dataset dataset = DatasetFactory.create(model);

        Query consulta1 = QueryFactory.create(query1);
        Query consulta2 = QueryFactory.create(query2);
        QueryExecution qexec1 = QueryExecutionFactory.create(consulta1, dataset);
        QueryExecution qexec2 = QueryExecutionFactory.create(consulta2, dataset);
        ResultSet resultado1 = qexec1.execSelect();
        ResultSet resultado2 = qexec2.execSelect();

        ArrayList<String> resultsList = new ArrayList<>();
        while (resultado1.hasNext()) {

                QuerySolution next1 = resultado1.next();
                String result1 = null;
                result1 = next1.toString();
                result1 = result1.replace("http://www.semanticweb.org/jadef/ontologies/2018/8/merged-wcm#", "");
                System.out.println("reultado do sparql1: " + result1);
                resultsList.add(result1);

        }

        while (resultado2.hasNext()) {

            QuerySolution next2 = resultado2.next();
            String result2 = null;
            result2 = next2.toString();
            result2 = result2.replace("http://www.semanticweb.org/jadef/ontologies/2018/8/merged-wcm#", "");
            System.out.println("reultado do sparql2: " + result2);
            resultsList.add(result2);

        }


        return resultsList;

    }


}
