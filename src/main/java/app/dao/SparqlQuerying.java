package app.dao;

import app.model.Provider;
import org.apache.jena.rdf.model.Model;

import java.lang.*;

import java.util.ArrayList;

public class SparqlQuerying {

    private static final String darkSkyString1 = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
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

    private static final String darkSkyString2 = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
            + "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
            + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
            + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
            + "PREFIX oboe-core: <http://ecoinformatics.org/oboe/oboe.1.2/oboe-core.owl#>\n"
            + "PREFIX merged-wcm: <http://www.semanticweb.org/jadef/ontologies/2018/8/merged-wcm#>\n"
            + "PREFIX sosa: <http://www.w3.org/ns/sosa/>\n"
            + "SELECT ?measurement ?measurementValue ?characteristic ?definition ?x ?y ?z ?standard ?observation ?entity \n"
            + " 	WHERE { ?measurement oboe-core:hasValue ?measurementValue.\n" +
            "               ?measurement oboe-core:usesStandard ?standard.\n" +
            "               ?measurementValue oboe-core:hasCode ?x.\n" +
            "               ?measurement oboe-core:ofCharacteristic ?characteristic.\n" +
            "               ?observation oboe-core:ofEntity ?entity.\n" +
            "               ?measurement merged-wcm:hasDefinition ?definition}";

    private static final String solcastString1 = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
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

    private static final String solcastString2 = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
            + "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
            + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
            + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
            + "PREFIX oboe-core: <http://ecoinformatics.org/oboe/oboe.1.2/oboe-core.owl#>\n"
            + "PREFIX merged-wcm: <http://www.semanticweb.org/jadef/ontologies/2018/8/merged-wcm#>\n"
            + "PREFIX sosa: <http://www.w3.org/ns/sosa/>\n"
            + "SELECT ?measurement ?measurementValue ?sensor ?characteristic ?operatingProperty ?definition ?operatingRange ?x ?y ?z ?operatingStandard ?rangeStandard ?observation ?entity\n"
            + " 	WHERE { ?measurement oboe-core:hasValue ?measurementValue.\n" +
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

    private static final String solcastString3 = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
            + "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
            + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
            + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
            + "PREFIX oboe-core: <http://ecoinformatics.org/oboe/oboe.1.2/oboe-core.owl#>\n"
            + "PREFIX merged-wcm: <http://www.semanticweb.org/jadef/ontologies/2018/8/merged-wcm#>\n"
            + "PREFIX sosa: <http://www.w3.org/ns/sosa/>\n"
            + "SELECT ?measurement ?measurementValue ?sensor ?characteristic ?operatingProperty ?definition ?operatingRange ?x ?y ?z ?operatingStandard ?rangeStandard ?observation ?entity\n"
            + " 	WHERE { ?measurement oboe-core:hasValue ?measurementValue.\n" +
            "               ?measurement oboe-core:usesStandard ?standard.\n" +
            "               ?measurementValue oboe-core:hasCode ?x.\n" +
            "               ?measurement oboe-core:ofCharacteristic ?characteristic.\n" +
            "               ?observation oboe-core:ofEntity ?entity}";


    //need to know the providers and thus set the strings
    private String darkSkyQuery1;
    private String darkSkyQuery2;
    private String solcastQuery1;
    private String solcastQuery2;
    private String solcastQuery3;

    private void setDarkSkyQuery1(String darkSkyQuery1) {
        this.darkSkyQuery1 = darkSkyQuery1;
    }

    private void setDarkSkyQuery2(String darkSkyQuery2) {
        this.darkSkyQuery2 = darkSkyQuery2;
    }

    private void setSolcastQuery1(String solcastQuery1) {
        this.solcastQuery1 = solcastQuery1;
    }

    private void setSolcastQuery2(String solcastQuery2) {
        this.solcastQuery2 = solcastQuery2;
    }

    private void setSolcastQuery3(String solcastQuery3) {
        this.solcastQuery3 = solcastQuery3;
    }


    public void selectQueriesAccordingToProvider(Provider... providers) {
        int providersLength = providers.length;
        if (providersLength == 1) {
            if (providers[0].getType().equals("SOLCAST")) {
                setSolcastQuery1(solcastString1);
                setSolcastQuery2(solcastString2);
                setSolcastQuery3(solcastString3);
            } else if (providers[0].getType().equals("DARKSKY")) {
                setDarkSkyQuery1(darkSkyString1);
                setDarkSkyQuery2(darkSkyString2);
            }

        } else if (providersLength == 2) {
            setSolcastQuery1(solcastString1);
            setSolcastQuery2(solcastString2);
            setSolcastQuery3(solcastString3);
            setDarkSkyQuery1(darkSkyString1);
            setDarkSkyQuery2(darkSkyString2);
        }
    }

    public ArrayList<String> getResultList(Model model) {

        OntologyAccess ontologyAccess = new OntologyAccess();
        ArrayList<String> resultList = new ArrayList<>();

        if ((solcastQuery1 != null) && (darkSkyQuery1 == null)) {        // case the only data provider is solcast
            ArrayList<String> solcastResultList1 = ontologyAccess.getDataFromOntology(model, solcastQuery1);
            ArrayList<String> solcastResultList2 = ontologyAccess.getDataFromOntology(model, solcastQuery2);
            ArrayList<String> solcastResultList3 = ontologyAccess.getDataFromOntology(model, solcastQuery3);
            resultList = gatherDataFromDifferentQueries(solcastResultList1, solcastResultList2, solcastResultList3);
        } else if ((solcastQuery1 == null) && (darkSkyQuery1 != null)) {      //case there is only darksy
            ArrayList<String> darkSkyResultList1 = ontologyAccess.getDataFromOntology(model, darkSkyQuery1);
            ArrayList<String> darkSkyResultList2 = ontologyAccess.getDataFromOntology(model, darkSkyQuery2);
            resultList = gatherDataFromDifferentQueries(darkSkyResultList1, darkSkyResultList2);
        } else if ((solcastQuery1 != null) && (darkSkyQuery1 != null)) {      //case there are both darksky and solcast
            ArrayList<String> solcastResultList1 = ontologyAccess.getDataFromOntology(model, solcastQuery1);
            ArrayList<String> solcastResultList2 = ontologyAccess.getDataFromOntology(model, solcastQuery2);
            ArrayList<String> solcastResultList3 = ontologyAccess.getDataFromOntology(model, solcastQuery3);
            ArrayList<String> darkSkyResultList1 = ontologyAccess.getDataFromOntology(model, darkSkyQuery1);
            ArrayList<String> darkSkyResultList2 = ontologyAccess.getDataFromOntology(model, darkSkyQuery2);
            resultList = gatherDataFromDifferentQueries(solcastResultList1, solcastResultList2, darkSkyResultList1, darkSkyResultList2, solcastResultList3);
        }

        /*for (String result: resultList){
            System.out.println("SparqlQueryinResult: " + result);
        }*/
        return resultList;
    }

    @SafeVarargs
    private final ArrayList<String> gatherDataFromDifferentQueries(ArrayList<String>... lists) {
        //(darkSky1, darkSky2), (solcast1, solcast2, solcast3) ou (darkSky1, darkSky2, solcast1, solcast2, solcast3)

        ArrayList<String> resultList = new ArrayList<>();

        for (ArrayList<String> list : lists) {
            resultList.addAll(list);
        }

       /* for (String line: resultList){
            System.out.println("resultList: " + line);
        }

        for (String line: lists[0]){
            System.out.println("lists[0]: " + line);
        }
        for (String line: lists[1]){
            System.out.println("lists[1]: " + line);
        }*/

        return resultList;
    }





    /*public ArrayList<String> getDataFromOntology(Model model, Provider... provider) throws IOException {

        String darkSkyQuery = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
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


        String solcastQuery = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
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


        for (String result: resultsList){
            System.out.println("result: " + result);
        }


        return resultsList;

    }
*/

}
