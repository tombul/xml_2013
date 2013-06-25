package fu.berlin.de.webdatabrowser.deep.vocabulary;

import com.hp.hpl.jena.rdf.model.Property;

public final class DBPediaOwl extends DeebVocabulary {

    private static final String NS = "http://dbpedia.org/ontology/";

    private DBPediaOwl() {
    }

    public static String getURI() {
        return NS;
    }

    public static final Property populationTotal = getModel().createProperty(NS, "populationTotal");

    public static final Property leader          = getModel().createProperty(NS, "leader");

    public static final Property areaTotal       = getModel().createProperty(NS, "areaTotal");

    public static final Property birthPlace      = getModel().createProperty(NS, "birthPlace");

    public static final Property hometown        = getModel().createProperty(NS, "hometown");

}
