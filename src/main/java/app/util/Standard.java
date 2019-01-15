package app.util;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Resource;

public class Standard {


    public static void createIndividualAndLink(OntModel ontModel, String standardName, Individual measurementIndividual) {

        String baseURI3 = OntologyDataCreation.BASE_URI3;
        String baseURI4 = OntologyDataCreation.BASE_URI4;
        String baseURI7 = OntologyDataCreation.BASE_URI7;

        //testa se o individuo pertence ao prefixo 7
        OntClass standardClass = ontModel.getOntClass(baseURI7 + standardName);
        System.out.println();

        if (standardClass == null) {
            // se nao estiver no prefixo 7, cria-se a classe no prefixo 4
            standardClass = ontModel.createClass(baseURI4 + standardName);
        }


        //Cria o individuo com o prefixo 4
        Individual standardIndividual = standardClass.createIndividual(baseURI4 + standardName);
        System.out.println(standardIndividual);

        //Pega a object property usesStandard
        ObjectProperty usesStandard = ontModel.getObjectProperty(baseURI3 + "usesStandard");

        //Liga o individual Standard ao individuo Measurement atraves da object property usesStandard
        Resource usesStandardLink = measurementIndividual.addProperty(usesStandard, standardIndividual);


    }

}
