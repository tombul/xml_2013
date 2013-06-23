package fu.berlin.de.webdatabrowser.deep.vocabulary;

import com.hp.hpl.jena.rdf.model.Property;

/**
 * Schema.org vocabulary
 * 
 * @author Jan-Christopher
 * 
 */
public class Schema extends DeebVocabulary {

    private static final String NS = "http://Schema.org/";

    public static String getURI() {
        return NS;
    }

    public static final Property name          = getModel().createProperty(NS, "name");

    public static final Property URL           = getModel().createProperty(NS, "URL");

    public static final Property keywords      = getModel().createProperty(NS, "keywords");

    public static final Property image         = getModel().createProperty(NS, "image");

    public static final Property award         = getModel().createProperty(NS, "award");

    public static final Property description   = getModel().createProperty(NS, "description");

    public static final Property datePublished = getModel().createProperty(NS, "datePublished");

    public static final Property dateModified  = getModel().createProperty(NS, "dateModified");

    public static final Property author        = getModel().createProperty(NS, "author");

    public static final Property comment       = getModel().createProperty(NS, "comment");

    public static final Property review        = getModel().createProperty(NS, "review");

    public static final Property commentText   = getModel().createProperty(NS, "commentText");

    public static final Property commentTime   = getModel().createProperty(NS, "commentTime");

    public static final Property discusses     = getModel().createProperty(NS, "discusses");

    public static final Property creator       = getModel().createProperty(NS, "creator");

    public static final Property about         = getModel().createProperty(NS, "about");

    public static final Property editor        = getModel().createProperty(NS, "editor");

}
