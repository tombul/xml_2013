package fu.berlin.de.webdatabrowser.webdataparser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JSONParser extends BasicParser {

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

    // gets an ID and returns the JSON-document of this ID
    private String getJSONContent(String ID) {
        String url = "http://europeana.eu/api/v2/record/" + ID + ".json?wskey=PmbkTtFZf&profile=full";
        return getAsyncResults(url, null);
    }

    // Gets a JSON-document and a list of tags, extract content and creates xml
    // tags
    private String convertTags(String[][] regularExp, String content) {
        String tags = "";
        for(int i = 0; i < regularExp.length; i++) {
            Matcher matcher = Pattern.compile(regularExp[i][0]).matcher(content);
            if(matcher.find()) {
                int pos = matcher.start() + Integer.valueOf(regularExp[i][1]);
                String value = "";
                while(content.charAt(pos) != '"') {
                    value += String.valueOf(content.charAt(pos));
                    pos++;
                }
                tags += "\t<" + regularExp[i][2] + ">" + value.replace("&", "&amp;") + "</" + regularExp[i][2] + ">\n";
            }
        }
        return tags;
    }

    // Gets String with web content and returns a xml-document for an object
    // whose ID is in the content
    public String parseJSON(String webContent) {

        String jSONID = getJSONFileID(webContent);
        if(jSONID == null) { // The page doesn't contain any ID's
            return null;
        }

        String jSONFileContent = getJSONContent(jSONID);
        if(jSONFileContent == null) { // Did not get content with the ID
            return null;
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

        String tags = convertTags(regularExp, jSONFileContent); // create tags
        String xmlDoc = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                + "<historicalObject>\n" + tags + "</historicalObject>"; // create
                                                                         // XML-document

        return xmlDoc;
    }
}