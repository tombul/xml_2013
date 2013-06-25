package fu.berlin.de.webdatabrowser.deep.vocabulary;

import com.hp.hpl.jena.rdf.model.Property;

public final class Deeb extends DeebVocabulary {

    private static final String NS = "http://www.fu-berlin.de/deeb/";

    public static final String getURI() {
        return NS;
    }

    private Deeb() {
    }

    public static final Property ResourceType = getModel().createProperty(NS, "ResourceType");

}
