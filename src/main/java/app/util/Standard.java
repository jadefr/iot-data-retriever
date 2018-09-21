package util;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Resource;

public class Standard {

    //private final String baseURI1 = "http://www.semanticweb.org/jadef/ontologies/2018/4/oboe_wcm#";
    //private final String baseURI2 = "http://www.semanticweb.org/jadef/ontologies/2018/4/wcm#";
    //private final String baseURI3 = "http://ecoinformatics.org/oboe/oboe.1.2/oboe-core.owl#";


    private final OntModel ontModel;
    private final String standardName;

    //acessar a classe com Standard standard = new Standard(ontModel, standardName);
    //                              standard.createIndividualAndLink(measurementIndividual);
    /**
     *
     * @param ontModel
     * @param standardName
     */
    public Standard(OntModel ontModel, String standardName) {
        this.ontModel = ontModel;
        this.standardName = standardName;
    }

    public void createIndividualAndLink(Individual measurementIndividual) {

        String baseURI1 = OntologyDataCreation.BASE_URI1;
        String baseURI2 = OntologyDataCreation.BASE_URI2;
        String baseURI3 = OntologyDataCreation.BASE_URI3;
        String baseURI4 = OntologyDataCreation.BASE_URI4;
        String baseURI5 = "http://ecoinformatics.org/oboe/oboe.1.2/oboe-standards.owl#";

        //testa se o individuo pertence ao prefixo 2, 5 ou 1, nessa ordem
        OntClass standardClass = ontModel.getOntClass(baseURI2 + standardName);
        if (standardClass == null) {
            standardClass = ontModel.getOntClass(baseURI5 + standardName);
        }
        if (standardClass == null) {
            standardClass = ontModel.getOntClass(baseURI1 + standardName);
        }

        if (standardName != null) {
            //Cria o individuo com o prefixo 4
            Individual standardIndividual = standardClass.createIndividual(baseURI4 + standardName);

            //Pega a object property usesStandard
            ObjectProperty usesStandard = ontModel.getObjectProperty(baseURI3 + "usesStandard");

            //Liga o individual Standard ao individuo Measurement atraves da object property usesStandard
            Resource usesStandardLink = measurementIndividual.addProperty(usesStandard, standardIndividual);

        }

    }

}
