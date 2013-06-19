package fu.berlin.de.webdatabrowser.deep.vocabulary;

import com.hp.hpl.jena.rdf.model.Property;

public class City extends Vocabulary {

    private static final String  DBPediaOwlNS    = "http://dbpedia.org/resource/classes#";

    public static final Property populationTotal = getModel().createProperty(DBPediaOwlNS, "populationTotal");

}
