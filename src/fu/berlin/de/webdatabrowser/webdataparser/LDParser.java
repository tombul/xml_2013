package fu.berlin.de.webdatabrowser.webdataparser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.SAXException;

public final class LDParser {

    static ArrayList<String> tags = new ArrayList<String>( // befüllt mit ein
                                                           // paar Tags die man
                                                           // nehmen könnte
                                  Arrays.asList("dbpedia-owl:populationTotal", // Einwohnerzahl
                                          "dbpedia-owl:leader", // Bürgermeister
                                          "geo:geometry", // Koordinaten
                                                          // POINT(13.3989
                                                          // 52.5006)
                                          "dbpedia-owl:areaTotal", // Gesamtfläche
                                                                   // (xsd:double)
                                          "dbpedia-owl:birthPlace", // Geburtsort
                                                                    // von...
                                          "dbpedia-owl:headquarter", // Hauptquartier
                                                                     // von...
                                          "dbpedia-owl:hometown" // Heimatstadt
                                                                 // von...
                                  ));

    // Gibt dom document als string zurück
    private static String getStringFromDoc(org.w3c.dom.Document doc) {
        DOMImplementationLS domImplementation = (DOMImplementationLS) doc.getImplementation();
        LSSerializer lsSerializer = domImplementation.createLSSerializer();
        return lsSerializer.writeToString(doc);
    }

    public static String parseLD(String webContent) throws ParserConfigurationException, SAXException, IOException {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(webContent);

        NodeList rdfEntries = doc.getElementsByTagName("rdf:Description");
        Document targetDoc = db.newDocument();

        Element root = targetDoc.createElement("rdf:RDF");
        root.setAttribute("xmlns:rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
        root.setAttribute("xmlns:dbpedia-owl", "http://dbpedia.org/ontology/");
        root.setAttribute("xmlns:geo", "http://www.w3.org/2003/01/geo/wgs84_pos#");
        targetDoc.appendChild(root);

        // Subjekte die die gewünschten Prädikate enthalten werden in ein neues
        // Dokument verfrachtet
        for(int i = 0; i < rdfEntries.getLength(); i++) {
            Node subject = rdfEntries.item(i);
            if(subject.getNodeType() == Node.ELEMENT_NODE) {

                NodeList predicates = subject.getChildNodes();

                boolean foundTag;
                for(int j = predicates.getLength() - 1; j >= 0; j--) {

                    Node predicate = predicates.item(j);
                    foundTag = false;

                    for(String element : tags) {
                        if(predicate.getNodeName() == element) {
                            foundTag = true;
                        }
                    }

                    if(!foundTag)
                        subject.removeChild(predicate); // löscht predicate wenn
                                                        // es nicht in der
                                                        // Tagliste gefunden
                                                        // wurde
                }
                if(subject.hasChildNodes())
                    targetDoc.getDocumentElement().appendChild(targetDoc.adoptNode(subject)); // Subjekt
                                                                                              // wird
                                                                                              // nur
                                                                                              // ins
                                                                                              // neue
                                                                                              // Doc
                                                                                              // eingehängt
                                                                                              // wenn
                                                                                              // es
                                                                                              // Prädikate
                                                                                              // hat
            }
        }

        return LDParser.getStringFromDoc(targetDoc);

    }

}
