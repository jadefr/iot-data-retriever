package model.solcastmodel;

import dao.SolcastSPARQL;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SolcastGSONWriting {

    public static void writeGSON() throws IOException {

        Measurements measurements = new Measurements();
        SolcastSPARQL ss = new SolcastSPARQL();
        SolcastRDFReading solcastRDFReading = new SolcastRDFReading(ss);

        ArrayList<String> standards = solcastRDFReading.getStandards();
        measurements.setStandard(standards);

    }


}
