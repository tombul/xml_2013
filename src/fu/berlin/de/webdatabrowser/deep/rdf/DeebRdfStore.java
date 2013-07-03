package fu.berlin.de.webdatabrowser.deep.rdf;

import java.util.List;

import com.hp.hpl.jena.rdf.model.Model;

/**
 * Interface fuer den RDFStore.
 * 
 * @author Jan-Christopher Pien
 *         27.05.2013
 * 
 */
public interface DeebRdfStore {

    /**
     * Laedt ein im USB-Speicher gespeicherten Store ein.
     * 
     * @return
     *         false, falls der Store nicht geladen werden konnte, ein leerer
     *         Store wird dann erstellt, true andernfalls
     */
    public boolean loadStore();

    /**
     * Speichert einen Store auf dem USB-Speicher.
     * 
     * @return
     *         false, falls der Store nicht gespeichert werden konnte, true
     *         andernfalls
     */
    public boolean saveStore();

    /**
     * Loescht den gesamten Store und erstellt einen neuen, leeren Store.
     */
    public void cleanStore();

    /**
     * Fuegt eine neue Ressource dem Tripelstore hinzu. Eine {@link http
     * ://jena.apache
     * .org/documentation/javadoc/jena/com/hp/hpl/jena/rdf/model/Resource.html
     * Ressource} hat verschiedene mit ihr verbundenen Eigenschaften, die
     * jeweils als Tripel im Tripelstore gespeichert
     * werden.
     * 
     * @param resource
     *            Die einzufuegende Ressource
     */
    public void addResource(DeebResource resource);

    /**
     * Fuehrt eine SPARQL-Query aus und gibt das Ergebnis als eine Liste von
     * {@link http
     * ://jena.apache.org/documentation/javadoc/jena/com/hp/hpl/jena/rdf
     * /model/Statement.html Statements} zurueck.
     * Diese Liste ist nie null, kann aber leer sein, wenn kein Ergebnis
     * gefunden wurde. Es duerfen waehrend Ausfuehrung der Query keine
     * Veraenderungen
     * an den urspruenglichen Daten stattfinden!
     * 
     * @param queryString
     *            Die auszufuehrende Query. Dabei sollten Variablen nicht direkt
     *            eingebunden werden,
     *            sondern mittels Fragezeichen als Platzhalter versehen werden.
     *            Die Variablen
     *            werden dann mit params uebergeben.
     * @param Liste von Platzhaltern, für die die Resource abgefragt werden
     *            sollen.
     * @return
     *         Liste der Resourcen, die gefunden wurden
     */
    public List<Object> performQuery(String queryString);

    /**
     * Gibt das genutzte Model zurueck.
     * 
     * @return
     *         Das genutzte Model.
     */
    public Model getModel();

}
