package app.dao;

import com.google.gson.Gson;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.query.*;

import java.io.IOException;
import java.util.ArrayList;

public class SolcastSPARQL {

    //private static final String PATH = "C:\\";
   /* private static Model model;

    public SolcastSPARQL(Model model) {
        this.model = model;
    }*/

    public static ArrayList<String> getSolcastFromOntology(Model model) throws IOException {

        //OntModel ontModel = OntologyAccess.loadOntologyModelFromUrl("http://datawebhost.com.br/ontologies/oboe_wcm_integrated.rdf");
        //OntModel ontModel = OntologyAccess.loadOntologyModel(PATH, "oboe_wcm_integrated.rdf");

        String query = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                + "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
                + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
                + "PREFIX oboe-core: <http://ecoinformatics.org/oboe/oboe.1.2/oboe-core.owl#>\n"
                + "PREFIX merged-wcm: <http://www.semanticweb.org/jadef/ontologies/2018/8/merged-wcm#>\n"
                + "PREFIX sosa: <http://www.w3.org/ns/sosa/>\n"
                + "SELECT ?measurement ?measurementValue ?sensor ?characteristic ?operatingProperty ?definition ?operatingRange ?x ?y ?z ?standard ?operatingStandard ?rangeStandard\n"
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
                "               ?operatingRange oboe-core:hasCode ?z}";

        //Dataset dataset = DatasetFactory.create(ontModel);
        Dataset dataset = DatasetFactory.create(model);

        org.apache.jena.query.Query consulta;
        consulta = QueryFactory.create(query);// Fazendo o parse da string da consulta e criando o objeto Query

        QueryExecution qexec = QueryExecutionFactory.create(consulta, dataset);// Executando a consulta e obtendo o resultado
        ResultSet resultado = qexec.execSelect();

        ArrayList<String> resultsList = new ArrayList<>();
        while (resultado.hasNext()) {
            QuerySolution next = resultado.next();
            String result = null;
            result = next.toString();
            result = result.replace("http://www.semanticweb.org/jadef/ontologies/2018/8/merged-wcm#", "");
            //System.out.println("reultado do sparql: " + result);
            resultsList.add(result);
        }

/*        Gson gson = new Gson();
        String resultJson = gson.toJson(resultsList);
        return resultJson;
*/
        return resultsList;
    }
}
