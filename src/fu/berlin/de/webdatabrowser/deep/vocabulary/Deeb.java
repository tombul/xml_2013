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

    public static final Property FoundWhere   = getModel().createProperty(NS, "FoundWhere");

    public static final Property FoundWhen    = getModel().createProperty(NS, "FoundWhen");

    public static final Property Found        = getModel().createProperty(NS, "Found");

}
