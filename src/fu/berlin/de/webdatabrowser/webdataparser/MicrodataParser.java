/**
 * 
 */
package fu.berlin.de.webdatabrowser.webdataparser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.jamesmurty.utils.XMLBuilder;

import fu.berlin.de.webdatabrowser.R;

/**
 * @author tom
 * 
 *         This class provides the necessary features for parsing embedded
 *         microdata from stackoverflow.
 * 
 */
public class MicrodataParser extends BasicParser {
    private static final String LOG_TAG = "MicrodataParser";

    public ByteArrayOutputStream parseMicroData(String source, String url, Context context) {
        int microDataID = R.raw.stackoverflow_to_xml;
        String agent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/27.0.1453.110 Safari/537.36";
        ByteArrayInputStream stream = null;
        // String html = getAsyncResults(url, agent);
        try {
            Element feedLink = Jsoup.parse(source, url).getElementsByAttributeValue("rel", "alternate").first();
            String xml = getAsyncResults(feedLink.absUrl("href"), agent);
            // String xmlSource = getXMLSource(xml);
            stream = new ByteArrayInputStream(xml.getBytes("UTF-8"));
            ByteArrayOutputStream out = WebDataParser.applyXSL(context, stream, microDataID);
            writeToFile(out.toString(), "postXSLT");
            return out;
        }
        catch(IOException e) {
            Log.e(LOG_TAG, Log.getStackTraceString(e));
        }
        return null;
    }

    public ByteArrayOutputStream parseMicroDataHtml(String source, String url, Context context) {
        int microDataID = R.raw.stackoverflow_to_xml;
        String agent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/27.0.1453.110 Safari/537.36";
        ByteArrayInputStream stream = null;
        // String html = getAsyncResults(url, agent);
        try {
            Element itemscope = Jsoup.parse(source, url).getElementsByAttribute("itemscope").first();
            String xmlSource = getXMLSource(itemscope);
            writeToFile(xmlSource, "preXSLT.xml");
            stream = new ByteArrayInputStream(xmlSource.getBytes("UTF-8"));
            ByteArrayOutputStream out = WebDataParser.applyXSL(context, stream, microDataID);
            writeToFile(out.toString(), "postXSLT.xml");
            return out;
        }
        catch(IOException e) {
            Log.e(LOG_TAG, Log.getStackTraceString(e));
        }
        return null;
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
        String xmlHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        String html = "<root>" + cleanHtml.getElementById("question-header").outerHtml();
        html = html.concat(cleanHtml.getElementById("mainbar").outerHtml());
        String xml = xmlHeader.concat(html + "</root>");
        ByteArrayOutputStream out = new ByteArrayOutputStream(32);
        try {
            XMLBuilder builder = XMLBuilder.parse(new InputSource(new ByteArrayInputStream(xml.getBytes("UTF-8"))));
            xml = builder.asString();
            xml = xmlHeader + xml;
        }
        catch(UnsupportedEncodingException e) {
            Log.e(LOG_TAG, Log.getStackTraceString(e));
        }
        catch(IOException e) {
            Log.e(LOG_TAG, Log.getStackTraceString(e));
        }
        catch(ParserConfigurationException e) {
            Log.e(LOG_TAG, Log.getStackTraceString(e));
        }
        catch(SAXException e) {
            Log.e(LOG_TAG, Log.getStackTraceString(e));
        }
        catch(TransformerException e) {
            Log.e(LOG_TAG, Log.getStackTraceString(e));
        }

        return xml;
    }

    private void writeToFile(String input, String fileName) {
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)) {
            Log.d("Test", "sdcard mounted and writable");
            File myFile = new File(Environment.getExternalStorageDirectory().getPath() + "/" + fileName);
            Log.d("test-path", Environment.getExternalStorageDirectory().getPath() + "/" + fileName);
            try {
                myFile.createNewFile();
                FileOutputStream fOut = new FileOutputStream(myFile);
                OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
                myOutWriter.append(input);
                myOutWriter.close();
                fOut.close();
                Log.d("Test-done", "writen without errors");
            }
            catch(IOException e) {
                Log.e("Test", Log.getStackTraceString(e));
            }
        }
        else if(Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            Log.d("Test", "sdcard mounted readonly");
        }
        else {
            Log.d("Test", "sdcard state: " + state);
        }
    }

}
