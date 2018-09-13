package dao;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

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
}
