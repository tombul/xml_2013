{"apikey":"PmbkTtFZf",
"action":"record.json",
"success":true,
"statsDuration":31,
"requestNumber":0,
"object":{"type":"IMAGE",
"title":["map (houder)"],
"about":"/2023008/5E5864A50273C9A7498067B377AD1568E6A6337D",
"proxies":[{"about":"/proxy/provider/2023008/5E5864A50273C9A7498067B377AD1568E6A6337D",
"dcDescription":{"def":["Map van vereniging voor hedendaagse kunst in Gent."]},
"dcIdentifier":{"def":["71022A99.priref.4204"]},
"dcTitle":{"def":["map (houder)"]},
"dcType":{"def":["map (houder)"]},package fu.berlin.de.webdatabrowser.webdataparser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class JSONParser{
	private static String getJSONFileID(String code) {
		// If code containts an ID of a JSON-Document, extract the ID and return it, otherwise null
		Matcher matcher = Pattern.compile("europeana.eu/resolve/record/.*\">").matcher(code);
		if (!matcher.find()){
			return null;
		}
		String ID = matcher.group();
		ID = ID.substring(28, ID.length()-2); //remove help chars used to find
		return ID;
	}

	// gets an ID and returns the JSON-document of this ID
	private static String getJSONContent(String ID) {
		String url = "http://europeana.eu/api/v2/record/" + ID + ".json?wskey=PmbkTtFZf&profile=full";
		
		// Create reader to read web content
	    BufferedReader r = null;
		try {
			r = new BufferedReader(new InputStreamReader( new URL(url).openStream()));
		} catch (IOException e1) {}

		// No internet connection or no JSON document with the ID
		if(r == null)
			return null;
		
		// Read web content and copy into a String
	    String line = null;
	    String content = "";
	    try {
	    	while((line=r.readLine())!=null){
	    		content += line;
	    	}
		} catch (IOException e) {}
	    
	    return content;
	}

	// Gets a JSON-document and a list of tags, extract content and creates xml tags
	private static String convertTags(String[][] regularExp, String content){
		String tags = "";
		for (int i = 0; i < regularExp.length; i++) {
			Matcher matcher = Pattern.compile(regularExp[i][0]).matcher(content);
			if (matcher.find()) {
				int pos = matcher.start() + Integer.valueOf(regularExp[i][1]);
				String value = "";
				while (content.charAt(pos) != '"'){
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
		if (JSONFileContent == null) //  Did not get content with the ID
			return null;
		
		// Tags we want: titel, #chars to content, new tagname
		String[][] regularExp= {
				{"dcTitle.*", "18", "titel"},
				{"dcType.*", "17", "type"},
				{"edmPreview*", "13", "preview"},
				{"dcDescription.*", "24", "description"},
				{"edmLanguage.*", "22", "language"},
				{"edmCountry.*", "21", "country"},
				{"edmLandingPage.*}", "17", "url"},
				{"edmDataProvider.*", "26", "provider"}};
		
		String tags = convertTags(regularExp, JSONFileContent); //create tags
		String xmlDoc = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
						+ "<object>\n" + tags + "</object>"; // create XML-document
				
		return xmlDoc;
	}
}
"dctermsMedium":{"def":["papier (vezelproduct)"]},
"proxyIn":["/aggregation/provider/2023008/5E5864A50273C9A7498067B377AD1568E6A6337D"],
"proxyFor":"/item/2023008/5E5864A50273C9A7498067B377AD1568E6A6337D",
"edmType":"IMAGE",
"europeanaProxy":false},
{"about":"/proxy/europeana/2023008/5E5864A50273C9A7498067B377AD1568E6A6337D",
"proxyIn":["/aggregation/europeana/2023008/5E5864A50273C9A7498067B377AD1568E6A6337D"],
"proxyFor":"/item/2023008/5E5864A50273C9A7498067B377AD1568E6A6337D",
"europeanaProxy":true}],
"aggregations":[{"about":"/aggregation/provider/2023008/5E5864A50273C9A7498067B377AD1568E6A6337D",
"edmDataProvider":{"def":["Provinciaal Centrum voor Cultureel Erfgoed"]},
"edmIsShownAt":"http://europeana.eu/api//3027/redirect?shownAt=http%3A%2F%2Fwww.erfgoedplus.be%2Ferfgoedplus%2Fdetail.jsp%3Fartefactid%3D71022A99.priref.4204%26bt%3Deuropeanaapi&provider=Erfgoedplus.be&id=http://www.europeana.eu/resolve/record/2023008/5E5864A50273C9A7498067B377AD1568E6A6337D&profile=full",
"edmObject":"http://arnold.limburg.be/alfresco/guestDownload/direct/workspace/SpacesStore/531907e5-ea29-4179-b46f-33336a7d6b32/531907e5-ea29-4179-b46f-33336a7d6b32_MEDIUM.png",
"edmProvider":{"def":["Erfgoedplus.be"]},
"edmRights":{"def":["http://www.europeana.eu/rights/rr-f/"]},
"webResources":[{"about":"http://www.erfgoedplus.be/erfgoedplus/detail.jsp?artefactid=71022A99.priref.4204"},
{"about":"http://arnold.limburg.be/alfresco/guestDownload/direct/workspace/SpacesStore/531907e5-ea29-4179-b46f-33336a7d6b32/531907e5-ea29-4179-b46f-33336a7d6b32_MEDIUM.png"}]}],
"europeanaCollectionName":["2023008_Ag_BE_Elocal_ProvincieLimburg"],
"europeanaCompleteness":6,
"providedCHOs":[{"about":"/item/2023008/5E5864A50273C9A7498067B377AD1568E6A6337D"}],
"europeanaAggregation":{"about":"/aggregation/europeana/2023008/5E5864A50273C9A7498067B377AD1568E6A6337D",
"edmLandingPage":"http://www.europeana.eu/portal/record/2023008/5E5864A50273C9A7498067B377AD1568E6A6337D.html",
"edmCountry":{"def":["belgium"]},
"edmLanguage":{"def":["mul"]},
"edmPreview":"http://europeanastatic.eu/api/image?uri=http://arnold.limburg.be/alfresco/guestDownload/direct/workspace/SpacesStore/531907e5-ea29-4179-b46f-33336a7d6b32/531907e5-ea29-4179-b46f-33336a7d6b32_MEDIUM.png&size=LARGE&type=TEXT"}}}
