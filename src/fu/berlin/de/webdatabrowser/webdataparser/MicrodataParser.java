/**
 * 
 */
package fu.berlin.de.webdatabrowser.webdataparser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import android.content.Context;
import android.util.Log;
import fu.berlin.de.webdatabrowser.R;
import fu.berlin.de.webdatabrowser.deep.rdf.DeebResource;
import fu.berlin.de.webdatabrowser.util.Debug;

/**
 * @author tom
 * 
 *         This class provides the necessary features for parsing embedded
 *         microdata from stackoverflow.
 * 
 */
public class MicrodataParser {
    private static final String LOG_TAG = "MicrodataParser";
    private final WebDataParser resultHandler;

    public MicrodataParser(WebDataParser resultHandler) {
        this.resultHandler = resultHandler;
    }

    public void parseMicroDataHtml(String source, String url, Context context) {
        int microDataID = R.raw.stackoverflow_to_xml;
        ByteArrayInputStream stream = null;

        try {
            Element itemscope = Jsoup.parse(source, url).getElementsByAttribute("itemscope").first();
            String xmlSource = getXMLSource(itemscope);
            Debug.writeFileToExternalStorage(xmlSource, "mdPreMDXSLT.xml");
            Debug.logLongString(xmlSource);
            stream = new ByteArrayInputStream(xmlSource.getBytes("UTF-8"));
            ByteArrayOutputStream out = WebDataParser.applyXSL(context, stream, microDataID, false);
            Debug.writeFileToExternalStorage(out.toString(), "mdPreRDFXSLT.xml");
            Debug.logLongString(out.toString());

            // TODO Generate Ressources
            LinkedList<DeebResource> objects = new LinkedList<DeebResource>();
            resultHandler.onParsingResultAvailable(objects);
        }
        catch(IOException e) {
            Log.e(LOG_TAG, Log.getStackTraceString(e));
        }
        if(stream == null) {
            resultHandler.onParsingResultAvailable(null);
        }
    }

    private String getXMLSource(Element htmlToParse) {
        Element cleanHtml = cleanHtml(htmlToParse);
        String xml = buildXML(cleanHtml);
        return xml;
    }

    private Element cleanHtml(Element htmlToParse) {
        htmlToParse.getElementsByTag("script").remove();
        htmlToParse.getElementsByTag("input").remove();
        htmlToParse.getElementsByTag("br").remove();
        htmlToParse.getElementsByTag("form").remove();
        htmlToParse.select("img").after("</img>");
        return htmlToParse;
    }

    private String buildXML(Element cleanHtml) {
        String xmlHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
        String html = "<creativeWork xmlns=\"http://www.fu-berlin.de/deeb/WebBrowser\">" + cleanHtml.getElementById("question-header").outerHtml();
        html = html.concat(cleanHtml.getElementById("mainbar").outerHtml());
        String xml = xmlHeader.concat(html + "</creativeWork>");
        return xml;
    }
}
