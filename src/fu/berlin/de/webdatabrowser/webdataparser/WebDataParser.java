package fu.berlin.de.webdatabrowser.webdataparser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import android.content.Context;
import android.util.Log;
import fu.berlin.de.webdatabrowser.deep.rdf.DeebResource;
import fu.berlin.de.webdatabrowser.ui.WebDataBrowserActivity;

/**
 * This class provides methods to transform arbitrary web-data to XML-documents
 * as well as to transfom XML-documents using XSL-stylesheets.
 */
public class WebDataParser {
    private static final String          LOG_TAG            = "Parser";
    private final WebDataBrowserActivity webDataBrowser;
    private static Boolean               namespaceAwareness = false;

    public WebDataParser(WebDataBrowserActivity webDataBrowser) {
        this.webDataBrowser = webDataBrowser;
    }

    /**
     * Parses any input, trying to find any RDF-Ressources. Any resource found
     * is added to the RDF-model.
     * 
     * @param source Inputstream of the data
     * @return A list of the found DeebResources
     */
    public void parse(String sourceCode, String url) {
        // get XML Document from a parser
        if(url.contains("europeana")) {
            Log.d(LOG_TAG, "trying to parse json");
            new JSONParser(this).parseJSON(sourceCode);
        }
        else if(url.contains("abe.tudelft.nl/index.php/faculty-architecture/oai") || url.contains("http://www.openarchives.org/Register/BrowseSites?viewRecord=")) {
            Log.d(LOG_TAG, "trying to parse xml");
            new XMLParser(this).parseXML(url);
        }
        else if(url.contains("dbpedia")) {
            Log.d(LOG_TAG, "trying to parse rdf");
            new LDParser(this).parseLD(sourceCode, url);
        }
        else if(url.matches(".*stackoverflow.com/questions/.+")) {
            Log.d(LOG_TAG, "trying to parse microdata");
            new MicrodataParser(this).parseMicroDataHtml(sourceCode, url, webDataBrowser);
        }
        else {
            Log.d(LOG_TAG, "no suitable parser available");
            webDataBrowser.onParsingResultAvailable(new LinkedList<DeebResource>());
        }
    }

    /**
     * Transforms a XML-document using a given stylesheet.
     * 
     * @param context The applications context
     * @param sourceInputStream InputStream of a xml-document
     * @param xslID ID of a XSL-stylesheet
     * @return Outputstream of the transformed document
     */
    public static ByteArrayOutputStream applyXSL(Context context, InputStream sourceInputStream, int xslID, Boolean namespaceAwareness) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(32);

        try {
            WebDataParser.namespaceAwareness = namespaceAwareness;
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(WebDataParser.namespaceAwareness);
            Document sourceDocument = dbf.newDocumentBuilder().parse(sourceInputStream);
            Source source = new DOMSource(sourceDocument);
            Transformer transformer = TransformerFactory.newInstance().newTransformer(
                    new StreamSource(context.getResources().openRawResource(xslID)));
            StreamResult result = new StreamResult(outputStream);
            transformer.transform(source, result);
        }
        catch(IOException e) {
            Log.e(LOG_TAG, Log.getStackTraceString(e));
        }
        catch(TransformerException e) {
            Log.e(LOG_TAG, Log.getStackTraceString(e));
        }
        catch(SAXParseException e) {
            Log.e(LOG_TAG, e.getMessage() + " " + e.getLineNumber() + ", " + e.getColumnNumber());
            Log.e(LOG_TAG, Log.getStackTraceString(e));
        }
        catch(SAXException e) {
            Log.e(LOG_TAG, Log.getStackTraceString(e));
        }
        catch(ParserConfigurationException e) {
            Log.e(LOG_TAG, Log.getStackTraceString(e));
        }

        return outputStream;
    }

    public void onParsingResultAvailable(List<DeebResource> resources) {
        if(resources == null) {
            Log.w(LOG_TAG, "nothing could be parsed");
            webDataBrowser.onParsingResultAvailable(new LinkedList<DeebResource>());
            return;
        }

        webDataBrowser.onParsingResultAvailable(resources);
    }

    public Context getContext() {
        return webDataBrowser;
    }
}
