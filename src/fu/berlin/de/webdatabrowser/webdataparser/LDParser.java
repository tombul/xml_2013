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

    static ArrayList<String> tags = new ArrayList<String>(
                                          // befuellt mit ein paar Tags die man
                                          // nehmen koennte
                                          Arrays.asList(
                                                  // Einwohnerzahl
                                                  "dbpedia-owl:populationTotal",
                                                  // Buergermeister
                                                  "dbpedia-owl:leader",
                                                  // Koordinaten POINT(13.3989,
                                                  // 52.5006)
                                                  "geo:geometry",
                                                  // Gesamtflaeche (xsd:double)
                                                  "dbpedia-owl:areaTotal",
                                                  // Geburtsort von...
                                                  "dbpedia-owl:birthPlace",
                                                  // Hauptquartier von...
                                                  "dbpedia-owl:headquarter",
                                                  // Heimatstadt von...
                                                  "dbpedia-owl:hometown"
                                                  ));

    // Gibt dom document als string zurueck
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
        targetDoc.appendChild(root);

        boolean importNode;

        for(int i = 0; i < rdfEntries.getLength(); i++) {
            Node node = rdfEntries.item(i);
            if(node.getNodeType() == Node.ELEMENT_NODE) {
                NodeList children = node.getChildNodes();
                importNode = false;
                Node copyOfN = targetDoc.importNode(node, false);
                for(int j = 0; j < children.getLength(); j++) {
                    Node cNode = children.item(j);
                    for(String element : LDParser.tags) {
                        if(cNode.getNodeName() == element) {
                            copyOfN.appendChild(targetDoc.importNode(cNode, false));
                            importNode = true;
                        }
                    }
                }
                if(importNode)
                    targetDoc.getDocumentElement().appendChild(copyOfN);
            }
        }

        return LDParser.getStringFromDoc(targetDoc);

    }

}
