package fu.berlin.de.deeb.rdf;

import java.util.List;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;

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

	private Resource resource;
	private String identifier;
	
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
	 * Objekt spaeter rekonstruieren zu können.
	 * 
	 * @return die gekapselte Resource
	 */
	public Resource getResource() {
		return resource;
	}
	
	/**
	 * Legt die zugrunde liegende Resource fest.
	 * @param resource
	 * 				Die zu speichernde Resource
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
	public abstract DeebResource fromResource(Resource resource);
	
	/**
	 * Speichert die Deeb-Resource im angegebenen Modell. Dabei muss ein neues Resource-Objekt
	 * aus dem Model erzeugt werden und die Eigenschaften der Reihe nach hinzugefuegt werden.
	 * @param model
	 */
	public abstract void saveInModel(Model model);

}
