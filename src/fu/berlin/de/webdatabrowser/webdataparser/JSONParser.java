package fu.berlin.de.webdatabrowser.webdataparser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class JSONParser {
    private static String getJSONFileID(String code) {
        // If code containts an ID of a JSON-Document, extract the ID and return
        // it, otherwise null
        Matcher matcher = Pattern.compile("europeana.eu/resolve/record/.*\">").matcher(code);
        if(!matcher.find()) {
            return null;
        }
        String ID = matcher.group();
        ID = ID.substring(28, ID.length() - 2); // remove help chars used to
                                                // find
        return ID;
    }

    // gets an ID and returns the JSON-document of this ID
    private static String getJSONContent(String ID) {
        String url = "http://europeana.eu/api/v2/record/" + ID + ".json?wskey=PmbkTtFZf&profile=full";

        // Create reader to read web content
        BufferedReader r = null;
        try {
            r = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
        }
        catch(IOException e1) {
        }

        // No internet connection or no JSON document with the ID
        if(r == null)
            return null;

        // Read web content and copy into a String
        String line = null;
        String content = "";
        try {
            while((line = r.readLine()) != null) {
                content += line;
            }
        }
        catch(IOException e) {
        }

        return content;
    }

    // Gets a JSON-document and a list of tags, extract content and creates xml
    // tags
    private static String convertTags(String[][] regularExp, String content) {
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
                tags += "\t<" + regularExp[i][2] + ">" + value + "</" + regularExp[i][2] + ">\n";
            }
        }
        return tags;
    }

    // Gets String with web content and returns a xml-document for an object
    // whose ID is in the content
    public static String parseJSON(String webContent) {

        String JSONID = getJSONFileID(webContent);
        if(JSONID == null) // The page doesn't contain any ID's
            return null;

        String JSONFileContent = getJSONContent(JSONID);
        if(JSONFileContent == null) // Did not get content with the ID
            return null;

        // Tags we want: titel, #chars to content, new tagname
        String[][] regularExp = {
                { "dcTitle.*", "18", "titel" },
                { "dcType.*", "17", "type" },
                { "edmPreview*", "13", "preview" },
                { "dcDescription.*", "24", "description" },
                { "edmLanguage.*", "22", "language" },
                { "edmCountry.*", "21", "country" },
                { "edmLandingPage.*}", "17", "url" },
                { "edmDataProvider.*", "26", "provider" } };

        String tags = convertTags(regularExp, JSONFileContent); // create tags
        String xmlDoc = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                + "<historicalObject>\n" + tags + "</historicalObject>"; // create
                                                                         // XML-document
        return xmlDoc;
    }
}