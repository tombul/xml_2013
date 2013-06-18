/**
 * 
 */
package fu.berlin.de.webdatabrowser.webdataparser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import android.content.Context;
import fu.berlin.de.webdatabrowser.R;

/**
 * @author Nicolas Lehmann
 * 
 */
public class XMLParser extends BasicParser {

    public String parseXML(String url, Context context) {
        String newUrl = url.replace("http://www.openarchives.org/Register/BrowseSites?viewRecord=", "");
        newUrl = newUrl.concat("?verb=Identify");
        String newSource = getAsyncResults(newUrl, null);
        if(newSource != null) {
            ByteArrayOutputStream outputStream = WebDataParser.applyXSL(context, new ByteArrayInputStream(newSource.getBytes()), R.raw.xslt_oaipmh_identify);
            String xmlDocument = outputStream.toString();
            return xmlDocument;
        }
        return null;
    }

}
