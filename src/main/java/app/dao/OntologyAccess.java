package app.dao;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OntologyAccess {

    public static OntModel loadOntologyModel(String path, String fileName) throws IOException {

        OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);

        Path pathObj = Paths.get(path, fileName);
        InputStream input = new FileInputStream(pathObj.toString());
        ontModel.read(input, "RDF/XML");
        Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
        reasoner = reasoner.bindSchema(ontModel);
        OntModelSpec ontModelSpec = OntModelSpec.OWL_DL_MEM_TRANS_INF;
        ontModelSpec.setReasoner(reasoner);

        //ontologia carregada na maquina de inferencia
        //  ontModel = ModelFactory.createOntologyModel(ontModelSpec, ontModel);
        return ontModel;
    }

    public static OntModel loadOntologyModelFromUrl(String url) throws IOException {

        OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);

        InputStream input = new URL(url).openStream();
        ontModel.read(input, "RDF/XML");
        Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
        reasoner = reasoner.bindSchema(ontModel);
        OntModelSpec ontModelSpec = OntModelSpec.OWL_DL_MEM_TRANS_INF;
        ontModelSpec.setReasoner(reasoner);

        return ontModel;
    }

    public ArrayList<String> getDataFromOntology(Model model, String queryString) {

        Dataset dataset = DatasetFactory.create(model);
        Query consulta = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.create(consulta, dataset);
        ResultSet resultado1 = qexec.execSelect();

        ArrayList<String> resultsList = new ArrayList<>();
        while (resultado1.hasNext()) {

            QuerySolution next = resultado1.next();
            String result = null;
            result = next.toString();
            result = result.replace("http://www.semanticweb.org/jadef/ontologies/2018/8/merged-wcm#", "");
            //System.out.println("resultado do sparql: " + result);
            resultsList.add(result);

        }

        return resultsList;

    }



    /*
    ArrayList<String> resultList = new ArrayList<>();

        for (ArrayList<String> list : lists) {
        for (String line : list) {
            boolean lineNotFound = true;
            for (String resultListLine : resultList) {
                if (line.equals(resultListLine)) {
                    lineNotFound = false;
                }
            }
            if (lineNotFound) {
                resultList.add(line);
            }
        }
    }

    ArrayList<String> listWithoutDuplicates = (ArrayList<String>) resultList.stream().distinct().collect(Collectors.toList());

        for (String line: resultList){
        System.out.println("resultList: " + line);
    }
        for (String line: listWithoutDuplicates){
        System.out.println("listWithoutDuplicates: " + line);
    }
        return resultList;

       /* ArrayList<String> resultList = new ArrayList<>();
        resultList.addAll(lists[0]); // darksky1 ou solcast1

        for (String list1Line : lists[1]) { //darksky2 ou solcast2
            if (!resultList.contains(list1Line)) {
                resultList.add(list1Line);
            }
        }
        ArrayList<String> darkSkyResultList = resultList; // (darksky1 + darksky2) ou (solcast1 + solcast2)

        ArrayList<String> solcastResultList = new ArrayList<>();
        if (lists.length == 3) {      // solcast3
            for (String list2Line : lists[2]) {
                if (!resultList.contains(list2Line)) {
                    resultList.add(list2Line);
                }
            }
            solcastResultList = resultList;
        }

        if (lists.length == 5) {
            resultList.addAll(darkSkyResultList);
            resultList.addAll(solcastResultList);
        }

        for (String line: lists[0]){
            System.out.println("solcast1: " + line);
        }
        for (String line: lists[1]){
            System.out.println("solcast2: " + line);
        }
        for (String line: lists[2]){
            System.out.println("solcast3: " + line);
        }
        return resultList;*/

}
