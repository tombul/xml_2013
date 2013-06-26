/**
 * 
 */
package fu.berlin.de.webdatabrowser.webdataparser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.LinkedList;

import fu.berlin.de.webdatabrowser.R;
import fu.berlin.de.webdatabrowser.deep.rdf.DeebResource;
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
            Debug.writeFileToExternalStorage(source, "xmlPreXMLXSLT.xml");
            Debug.logLongString(source);
            ByteArrayOutputStream outputStream = WebDataParser.applyXSL(resultHandler.getContext(),
                    new ByteArrayInputStream(source.getBytes()), R.raw.xslt_oai_dc, true);
            String xmlDocument = outputStream.toString();
            Debug.writeFileToExternalStorage(xmlDocument, "xmlPreRDFXSLT.xml");
            Debug.logLongString(xmlDocument);

            // TODO Generate Ressources
            LinkedList<DeebResource> objects = new LinkedList<DeebResource>();
            resultHandler.onParsingResultAvailable(objects);
        }
        else {
            resultHandler.onParsingResultAvailable(null);
        }
    }
}
