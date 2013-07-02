/**
 * 
 */
package fu.berlin.de.webdatabrowser.webdataparser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import fu.berlin.de.webdatabrowser.R;
import fu.berlin.de.webdatabrowser.deep.rdf.DeebResource;
import fu.berlin.de.webdatabrowser.deep.rdf.resources.Person;
import fu.berlin.de.webdatabrowser.deep.rdf.resources.Publication;
import fu.berlin.de.webdatabrowser.util.Debug;
import fu.berlin.de.webdatabrowser.util.HttpRequestAsyncTask;
import fu.berlin.de.webdatabrowser.util.HttpResponseHandler;

/**
 * @author Nicolas Lehmann
 * 
 */
public class XMLParser implements HttpResponseHandler {
    private final WebDataParser resultHandler;

    public XMLParser(WebDataParser resultHandler) {
        this.resultHandler = resultHandler;
    }

    public void parseXML(String url) {
        String newUrl = url.replace("http://www.openarchives.org/Register/BrowseSites?viewRecord=", "");
        newUrl = newUrl.concat("?verb=ListRecords&metadataPrefix=oai_dc");
        new HttpRequestAsyncTask(this).execute(newUrl);
    }

    @Override
    public void onHttpResultAvailable(String source) {
        if(source != null) {
            ByteArrayOutputStream outputStream = WebDataParser.applyXSL(resultHandler.getContext(),
                    new ByteArrayInputStream(source.getBytes()), R.raw.xslt_oai_dc, true);

            // TODO Generate Ressources
            LinkedList<DeebResource> objects = getResourceFromXML(new ByteArrayInputStream(outputStream.toByteArray()), true);
            resultHandler.onParsingResultAvailable(objects);
        }
        else {
            resultHandler.onParsingResultAvailable(null);
        }
    }

    private LinkedList<DeebResource> getResourceFromXML(ByteArrayInputStream xmlDocument, Boolean namespaceAwareness) {
        try {
            LinkedList<DeebResource> objects = new LinkedList<DeebResource>();
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(namespaceAwareness);
            Document sourceDocument = dbf.newDocumentBuilder().parse(xmlDocument);
            Node parent = sourceDocument.getDocumentElement();
            Node activeNode = parent.getFirstChild();
            do {
                if(Node.ELEMENT_NODE == activeNode.getNodeType()) {
                    objects.add(getPublicationFromNode(activeNode.cloneNode(true)));
                }
            }
            while((activeNode = activeNode.getNextSibling()) != null);
            Debug.logLongString(Integer.toString(objects.size()));
            return objects;
        }
        catch(SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch(IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch(ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    private DeebResource getPublicationFromNode(Node node) {

        Node activeNode = node.getFirstChild();
        String title = null;
        String identifier = null;
        Person creator = null;
        String publisher = null;
        String description = null;
        List<String> subject = new LinkedList<String>();
        String date = null;
        do {
            if(Node.ELEMENT_NODE == activeNode.getNodeType()) {
                String field = activeNode.getNodeName();
                String value = activeNode.getTextContent();
                if(field.equals("dc:title")) {
                    title = value;
                }
                else if(field.equals("dc:identifier")) {
                    identifier = value;
                }
                else if(field.equals("dc:creator")) {
                    String lName = value.split(",")[0];
                    if(value.split(",").length < 2) {
                        Random r = new Random();
                        Person person = new Person(Integer.toString(r.nextInt()) + lName);
                        person.setLastName(lName);
                        creator = person;
                    }
                    else {
                        String fName = value.split(",")[1];
                        Person person = new Person(fName + " " + lName);
                        person.setGivenName(fName);
                        person.setLastName(lName);
                        creator = person;
                    }
                }
                else if(field.equals("dc:subject")) {
                    subject.add(value);
                }
                else if(field.equals("dc:publisher")) {
                    publisher = value;
                }
                else if(field.equals("dc:description")) {
                    description = value;
                }
                else if(field.equals("dc:date")) {
                    date = value;
                }
            }
        }
        while((activeNode = activeNode.getNextSibling()) != null);

        return new Publication(title, identifier, creator, date, description, publisher, subject);
    }

}
