package fu.berlin.de.webdatabrowser.webdataparser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.util.Log;
import fu.berlin.de.webdatabrowser.deep.rdf.DeebResource;
import fu.berlin.de.webdatabrowser.deep.rdf.resources.City;
import fu.berlin.de.webdatabrowser.deep.rdf.resources.Found;
import fu.berlin.de.webdatabrowser.deep.rdf.resources.Location;
import fu.berlin.de.webdatabrowser.deep.rdf.resources.Person;

public class LDParser {
    // befuellt mit ein paar Tags die man nehmen koennte
    static ArrayList<String>    tags           = new ArrayList<String>(
                                                       Arrays.asList(
                                                               // Einwohnerzahl
                                                               "dbpedia-owl:populationTotal",
                                                               // Buergermeister
                                                               "dbpedia-owl:leader",
                                                               // Koordinaten
                                                               // POINT(13.3989,
                                                               // 52.5006)
                                                               "geo:geometry",
                                                               // Gesamtflaeche
                                                               // (xsd:double)
                                                               "dbpedia-owl:areaTotal",
                                                               // Geburtsort
                                                               // von...
                                                               "dbpedia-owl:birthPlace",
                                                               // Hauptquartier
                                                               // von...
                                                               "dbpedia-owl:headquarter",
                                                               // Heimatstadt
                                                               // von...
                                                               "dbpedia-owl:hometown",
                                                               // Stadtname
                                                               "dbpprop:name"
                                                               ));
    private static final String LOG_TAG        = "LDParser";
    private final WebDataParser resultHandler;

    private static City         city;
    private static List<Person> birthPlaceList = new ArrayList<Person>();
    private static List<Person> hometownList   = new ArrayList<Person>();

    public LDParser(WebDataParser resultHandler) {
        this.resultHandler = resultHandler;
    }

    private DeebResource createObject(String tag, Node node) {

        DeebResource object = null;

        if(tag.equals("dbpedia-owl:birthPlace") || tag.equals("dbpedia-owl:hometown")) {

            String uri = node.getParentNode().getAttributes().getNamedItem("rdf:about").getNodeValue();
            try {
                uri = URLDecoder.decode(uri, "utf-8");
            }
            catch(UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String completeName = uri.split("^http://dbpedia.org/resource/")[1];
            String[] FirstLastname = completeName.split("_", 2);

            Person person = new Person(uri);
            person.setGivenName(FirstLastname[0]);
            if(FirstLastname.length >= 2) {
                person.setLastName(FirstLastname[1]);
            }
            else
                person.setLastName("");

            if(tag.equals("dbpedia-owl:birthPlace"))
                birthPlaceList.add(person);
            else
                hometownList.add(person);

            object = person;
        }
        else if(tag.equals("geo:geometry")) {
            // POINT(13.3989 52.5006)
            String point = node.getFirstChild().getTextContent();
            String[] lonLat = point.split("^POINT\\(")[1].split(" ");
            lonLat[1] = lonLat[1].replace(")", "");

            Location location = new Location(lonLat[1], lonLat[0]);
            location.setLon(Double.parseDouble(lonLat[0]));
            location.setLat(Double.parseDouble(lonLat[1]));
            city.setLocation(location);
            object = location;

        }
        else if(tag.equals("dbpedia-owl:leader")) {

            String uri = node.getAttributes().getNamedItem("rdf:resource").getNodeValue();
            try {
                uri = URLDecoder.decode(uri, "utf-8");
            }
            catch(UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String completeName = uri.split("^http://dbpedia.org/resource/")[1];
            String[] FirstLastname = completeName.split("_");

            Person person = new Person(uri);
            person.setGivenName(FirstLastname[0]);
            person.setLastName(FirstLastname[1]);
            city.setLeader(person);
            object = person;

        }
        else if(tag.equals("dbpedia-owl:populationTotal")) {

            city.setPopulationTotal(Double.parseDouble(node.getTextContent()));
        }
        else if(tag.equals("dbpedia-owl:areaTotal")) {

            city.setAreaTotal(Double.parseDouble(node.getTextContent()));
        }
        else if(tag.equals("dbpprop:name")) {

            city.setName(node.getTextContent());
        }

        return object;
    }

    private void completeCityObject() {

        city.setBirthPlace(birthPlaceList);
        city.setHometown(hometownList);

    }

    public City getCityObject() {
        completeCityObject();
        return city;
    }

    public void parseLD(String webContent, String url) {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        Document doc = null;

        try {
            db = dbf.newDocumentBuilder();
            doc = db.parse(new ByteArrayInputStream(webContent.getBytes()));
        }
        catch(ParserConfigurationException e) {
            Log.e(LOG_TAG, Log.getStackTraceString(e));
        }
        catch(SAXException e) {
            Log.e(LOG_TAG, Log.getStackTraceString(e));
        }
        catch(IOException e) {
            Log.e(LOG_TAG, Log.getStackTraceString(e));
        }

        NodeList rdfEntries = doc.getElementsByTagName("rdf:Description");
        Document targetDoc = db.newDocument();
        Element root = targetDoc.createElement("rdf:RDF");
        root.setAttribute("xmlns:rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
        // TODO noetige namespaces noch unvollstaendig
        root.setAttribute("xmlns:dbpedia-owl", "http://dbpedia.org/ontology/");
        targetDoc.appendChild(root);

        city = new City(url);

        Found found = new Found(url);
        found.setFoundWhere(url);
        found.setFoundWhen(new Date());
        city.addFound(found);

        for(int i = 0; i < rdfEntries.getLength(); i++) {
            Node subject = rdfEntries.item(i);
            if(subject.getNodeType() == Node.ELEMENT_NODE) {

                NodeList predicates = subject.getChildNodes();

                boolean foundTag;
                for(int j = predicates.getLength() - 1; j >= 0; j--) {

                    Node predicate = predicates.item(j);
                    foundTag = false;

                    for(String element : tags) {
                        if(predicate.getNodeName().equals(element)) {
                            createObject(element, predicate);
                            foundTag = true;
                        }
                    }

                    if(!foundTag)
                        // loescht predicate wenn es nicht in der Tagliste
                        // gefunden wurde
                        subject.removeChild(predicate);
                }
                if(subject.hasChildNodes())
                    // Subjekt wird nur ins neue Doc eingehaengt wenn es
                    // Praedikate hat
                    targetDoc.getDocumentElement().appendChild(targetDoc.adoptNode(subject));
            }
        }

        // TODO Generate Ressources
        LinkedList<DeebResource> objects = new LinkedList<DeebResource>();
        completeCityObject();
        objects.add(city);
        resultHandler.onParsingResultAvailable(objects);
    }

}