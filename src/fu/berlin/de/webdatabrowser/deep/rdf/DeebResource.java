package fu.berlin.de.webdatabrowser.deep.rdf;

import java.util.List;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;

import fu.berlin.de.webdatabrowser.deep.rdf.resources.Article;
import fu.berlin.de.webdatabrowser.deep.rdf.resources.City;
import fu.berlin.de.webdatabrowser.deep.rdf.resources.HistoricalObject;
import fu.berlin.de.webdatabrowser.deep.rdf.resources.Location;
import fu.berlin.de.webdatabrowser.deep.rdf.resources.Person;
import fu.berlin.de.webdatabrowser.deep.rdf.resources.Publication;
import fu.berlin.de.webdatabrowser.deep.rdf.resources.Review;
import fu.berlin.de.webdatabrowser.deep.rdf.resources.UserComment;
import fu.berlin.de.webdatabrowser.deep.vocabulary.Deeb;

/**
 * Abstrakte Klasse, welche eine {@link http
 * ://jena.apache.org/documentation/javadoc
 * /jena/com/hp/hpl/jena/rdf/model/Resource.html Resource} kapselt. Dieses
 * Interface sollte von den von uns erkannten Objekten (wie zum Beispiel Person,
 * Ort, Produkt etc.) implementiert werden, damit diese Resourcen in unserem
 * Tripelstore gespeichert werden koennen.
 * 
 * 
 * @author Jan-Christopher Pien 23.05.2013
 * 
 */
public abstract class DeebResource {

    public enum DeebPropertyType {

        ARTICLE("Article"),
        CITY("City"),
        HISTORICAL_OBJECT("Historical Object"),
        LOCATION("Location"),
        PERSON("Person"),
        PUBLICATION("Publication"),
        REVIEW("Review"),
        USER_COMMENT("User Comment");

        private final String text;

        private DeebPropertyType(String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }

        public static DeebPropertyType fromString(String text) {
            if(text != null) {
                for(DeebPropertyType b : DeebPropertyType.values()) {
                    if(text.equalsIgnoreCase(b.text)) {
                        return b;
                    }
                }
            }
            throw new IllegalArgumentException();
        }

    }

    private Resource resource;
    private String   identifier;

    public DeebResource(String identifier) {
        this.identifier = identifier;
    }

    /**
     * Liefert die {@link http
     * ://jena.apache.org/documentation/javadoc/jena/com/hp
     * /hpl/jena/rdf/model/Resource.html Resource}, die von diesem Objekt
     * gekapselt wird. Diese Resource wird im eigentlichen Tripelstore
     * gespeichert und beinhaltet neben den eigentlichen Properties noch die
     * Eigenschaft, um welche Art von DeebResource es sich handelt, um das
     * Objekt spaeter rekonstruieren zu k�nnen.
     * 
     * @return die gekapselte Resource
     */
    public Resource getResource() {
        return resource;
    }

    /**
     * Legt die zugrunde liegende Resource fest.
     * 
     * @param resource
     *            Die zu speichernde Resource
     */
    protected void setResource(Resource resource) {
        this.resource = resource;
    }

    public String getIdentifier() {
        return identifier;
    }

    protected void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    /**
     * Liefert alle Statements ueber diese Resource.
     * 
     * @return
     */
    public abstract List<Statement> getStatements();

    /**
     * Erzeugt ein neues DeebResource-Objekt aus einer Liste von Statements, die
     * als Ergebnis einer Query erhalten wurden, je nachdem um welche Resource
     * es sich handelt.
     * 
     * @param typeStatements
     *            die Liste an Statements, die die Resource beschreiben
     * @param resource
     *            die Resource, die gekapselt werden soll
     * @return die gewuenschte Resource
     */
    public abstract DeebResource fromStatements(List<Statement> typeStatements,
            Resource resource);

    /**
     * Erzeugt ein neues DeebResource-Objekt aus einem Resource-Objekt.
     * 
     * @param resource
     *            die Resource, die gekapselt werden soll
     * @return die gewuenschte Resource
     */
    protected abstract DeebResource fromResource(Resource resource);

    /**
     * Speichert die Deeb-Resource im angegebenen Modell. Dabei muss ein neues
     * Resource-Objekt
     * aus dem Model erzeugt werden und die Eigenschaften der Reihe nach
     * hinzugefuegt werden.
     * 
     * @param model
     */
    public abstract void saveInModel(Model model);

    /**
     * @return Html to display
     */
    public abstract String getHtml();

    /**
     * Erzeugt ein neues DeebResource-Objekt aus einem Resource-Objekt.
     * 
     * @param resource
     *            die Resource, die gekapselt werden soll
     * @return die gewuenschte Resource
     */
    public static DeebResource createResource(Resource resource) {
        String identifier = resource.getURI();
        DeebPropertyType propertyType = DeebPropertyType.fromString(resource.getProperty(Deeb.ResourceType).getString());
        DeebResource result = null;
        switch(propertyType) {
            case ARTICLE:
                result = new Article(identifier);
                break;
            case CITY:
                result = new City(identifier);
                break;
            case HISTORICAL_OBJECT:
                result = new HistoricalObject(identifier);
                break;
            case LOCATION:
                result = new Location(identifier);
                break;
            case PERSON:
                result = new Person(identifier);
                break;
            case PUBLICATION:
                result = new Publication(identifier);
                break;
            case REVIEW:
                result = new Review(identifier);
                break;
            case USER_COMMENT:
                result = new UserComment(identifier);
                break;
            default:
                break;
        }
        return result.fromResource(resource);
    }
}
