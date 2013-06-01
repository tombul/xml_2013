package fu.berlin.de.webdatabrowser.webdataparser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

import org.xml.sax.SAXException;

import android.content.Context;
import android.util.Log;
import fu.berlin.de.webdatabrowser.deep.rdf.DeebResource;

/**
 * This class provides methods to transform arbitrary web-data to XML-documents
 * as well as to transfom XML-documents using XSL-stylesheets.
 */
public class WebDataParser {
    private static final String LOG_TAG = "Parser";

    /**
     * Parses any input, trying to find any RDF-Ressources. Any resource found
     * is added to the RDF-model.
     * 
     * @param source Inputstream of the data
     * @return A list of the found DeebResources
     */
    public static List<DeebResource> parse(InputStream source) {
        // TODO find out what kind of data 'source' contains and generate
        // appropiate XML

        // TODO generate DeebResources from that data

        // TODO put that resources in the RDF-model

        // TODO return the data from this request (for visualization)
        return null;
    }

    /**
     * Transforms a XML-document using a given stylesheet.
     * 
     * @param context The applications context
     * @param sourceInputStream InputStream of a xml-document
     * @param xslID ID of a XSL-stylesheet
     * @return Outputstream of the transformed document
     */
    public static OutputStream applyXSL(Context context, InputStream sourceInputStream, int xslID) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(32);

        try {
            Source source = new DOMSource(
                    DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(sourceInputStream));
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
        catch(SAXException e) {
            Log.e(LOG_TAG, Log.getStackTraceString(e));
        }
        catch(ParserConfigurationException e) {
            Log.e(LOG_TAG, Log.getStackTraceString(e));
        }

        return outputStream;
    }
}
