package fu.berlin.de.webdatabrowser.webdataparser;

import java.util.Date;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fu.berlin.de.webdatabrowser.deep.rdf.DeebResource;
import fu.berlin.de.webdatabrowser.deep.rdf.resources.Found;
import fu.berlin.de.webdatabrowser.deep.rdf.resources.HistoricalObject;
import fu.berlin.de.webdatabrowser.util.HttpRequestAsyncTask;
import fu.berlin.de.webdatabrowser.util.HttpResponseHandler;

public class JSONParser implements HttpResponseHandler {
    private final WebDataParser resultHandler;
    static String[]             values = new String[8];
    private String              URL;

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

    // Gets a JSON-document and a list of tags, extract content and creates xml
    // tags
    private void convertTags(String[][] regularExp, String content) {
        // HistoricalObject object = new HistoricalObject(identifier)

        for(int i = 0; i < regularExp.length; i++) {
            Matcher matcher = Pattern.compile(regularExp[i][0]).matcher(content);

            if(matcher.find()) {
                int pos = matcher.start() + Integer.valueOf(regularExp[i][1]);
                String value = "";

                while(content.charAt(pos) != '"') {
                    value += String.valueOf(content.charAt(pos));
                    pos++;
                }
                values[i] = value;
            }
        }
    }

    // Gets String with web content and returns a xml-document for an object
    // whose ID is in the content
    public void parseJSON(String webContent) {

        String jSONID = getJSONFileID(webContent);
        if(jSONID == null) { // The page doesn't contain any ID's
            resultHandler.onParsingResultAvailable(null);
            return;
        }

        URL = "http://europeana.eu/api/v2/record/" + jSONID + ".json?wskey=PmbkTtFZf&profile=full";

        new HttpRequestAsyncTask(this).execute(
                "http://europeana.eu/api/v2/record/" + jSONID + ".json?wskey=PmbkTtFZf&profile=full");
    }

    @Override
    public void onHttpResultAvailable(String source) {
        if(source == null) { // Did not get content with the ID
            resultHandler.onParsingResultAvailable(null);
            return;
        }

        // Tags we want: titel, #chars to content, new tagname
        String[][] regularExp = {
                { "dcTitle.*", "18", "title" },
                { "dcType.*", "17", "type" },
                { "edmPreview.*", "13", "preview" },
                { "dcDescription.*", "24", "description" },
                { "edmLanguage.*", "22", "language" },
                { "edmCountry.*", "21", "country" },
                { "edmLandingPage.*", "17", "url" },
                { "edmDataProvider.*", "26", "provider" } };

        convertTags(regularExp, source); // create tags

        LinkedList<DeebResource> objects = new LinkedList<DeebResource>();
        HistoricalObject object = new HistoricalObject(values[6], values[0], values[1], values[3], values[4],
                values[5], values[6], values[7], values[2]);
        Found found = new Found(URL);
        found.setFoundWhere(URL);
        found.setFoundWhen(new Date());
        object.addFound(found);
        objects.add(object);
        resultHandler.onParsingResultAvailable(objects);
    }
}