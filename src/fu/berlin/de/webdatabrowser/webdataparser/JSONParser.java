package fu.berlin.de.webdatabrowser.webdataparser;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fu.berlin.de.webdatabrowser.deep.rdf.DeebResource;
import fu.berlin.de.webdatabrowser.deep.rdf.resources.HistoricalObject;
import fu.berlin.de.webdatabrowser.util.HttpRequestAsyncTask;
import fu.berlin.de.webdatabrowser.util.HttpResponseHandler;

public class JSONParser implements HttpResponseHandler {
    private final WebDataParser resultHandler;
    static String[]             values = new String[8];

    public JSONParser(WebDataParser resultHandler) {
        this.resultHandler = resultHandler;
    }

    private String getJSONFileID(String code) {
        // If code containts an ID of a JSON-Document, extract the ID and return
        // it, otherwise null
        Matcher matcher = Pattern.compile("europeana.eu/resolve/record/.*\">").matcher(code);

        if(!matcher.find()) {
            return null;
        }

        String iD = matcher.group();
        Integer end = iD.indexOf("\">");
        iD = iD.substring(28, end); // remove help chars used to
        return iD;
    }

    // Gets String with web content and returns a xml-document for an object
    // whose ID is in the content
    public void parseJSON(String webContent) {

        String jSONID = getJSONFileID(webContent);
        if(jSONID == null) { // The page doesn't contain any ID's
            resultHandler.onParsingResultAvailable(null);
            return;
        }

        new HttpRequestAsyncTask(this).execute(
                "http://europeana.eu/api/v2/record/" + jSONID + ".json?wskey=PmbkTtFZf&profile=full");
    }

    @Override
    public void onHttpResultAvailable(String source) {
        if(source == null) { // Did not get content with the ID
            resultHandler.onParsingResultAvailable(null);
            return;
        }

        LinkedList<DeebResource> objects = new LinkedList<DeebResource>();
        HistoricalObject object = new HistoricalObject(values[6], values[0], values[1], values[3], values[4],
                values[5], values[6], values[7], values[2]);
        objects.add(object);
        resultHandler.onParsingResultAvailable(objects);
    }
}