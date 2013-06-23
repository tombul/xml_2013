package fu.berlin.de.webdatabrowser.deep.vocabulary;

import com.hp.hpl.jena.rdf.model.Property;

public class Europeana extends DeebVocabulary {

    private static final String NS = "http://www.europeana.eu/schemas/ese/";

    private Europeana() {
    }

    public static String getURI() {
        return NS;
    }

    public static final Property language = getModel().createProperty(NS, "language");

    public static final Property country  = getModel().createProperty(NS, "country");

    public static final Property URI      = getModel().createProperty(NS, "URI");

    public static final Property provider = getModel().createProperty(NS, "provider");

    public static final Property preview  = getModel().createProperty(NS, "preview");

}
