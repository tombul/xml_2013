/**
 * 
 */
package fu.berlin.de.webdatabrowser.webdataparser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import android.content.Context;
import android.util.Log;
import fu.berlin.de.webdatabrowser.R;
import fu.berlin.de.webdatabrowser.deep.rdf.DeebResource;
import fu.berlin.de.webdatabrowser.deep.rdf.resources.Article;
import fu.berlin.de.webdatabrowser.deep.rdf.resources.Person;
import fu.berlin.de.webdatabrowser.deep.rdf.resources.Review;
import fu.berlin.de.webdatabrowser.deep.rdf.resources.UserComment;
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
            stream = new ByteArrayInputStream(xmlSource.getBytes("UTF-8"));
            ByteArrayOutputStream out = WebDataParser.applyXSL(context, stream, microDataID, false);

            List<DeebResource> objects = getResourceFromXML(new ByteArrayInputStream(out.toByteArray()));
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

    private List<DeebResource> getResourceFromXML(ByteArrayInputStream inputSource) {
        try {
            LinkedList<DeebResource> objects = new LinkedList<DeebResource>();
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(false);
            Document sourceDocument = dbf.newDocumentBuilder().parse(inputSource);
            Node parent = sourceDocument.getDocumentElement();
            objects.add(getArticleFromNode(parent.getFirstChild()));
            Debug.logLongString(Integer.toString(objects.size()));
            return objects;
        }
        catch(SAXException e) {
            e.printStackTrace();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        catch(ParserConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }

    private DeebResource getArticleFromNode(Node node) {
        try {
            Node activeNode = node;
            String name = null;
            String url = null;
            String description = null;
            String keywords = null;
            Date datePublished = null;
            Person author = null;
            Date dateModified = null;
            Person editor = null;
            List<UserComment> comments = new LinkedList<UserComment>();
            List<Review> reviews = new LinkedList<Review>();
            do {
                if(Node.ELEMENT_NODE == activeNode.getNodeType()) {
                    String field = activeNode.getNodeName();
                    String value = activeNode.getTextContent();
                    if(field.equals("name")) {
                        name = value;
                    }
                    else if(field.equals("url")) {
                        url = value;
                    }
                    else if(field.equals("description")) {
                        description = getNodeContent(activeNode);
                    }
                    else if(field.equals("keywords")) {
                        keywords = getNodeContent(activeNode);
                    }
                    else if(field.equals("datePublished")) {
                        datePublished = parseStringToDate(value);
                    }
                    else if(field.equals("author")) {
                        author = getPersonContent(activeNode);
                    }
                    else if(field.equals("dateModified")) {
                        datePublished = parseStringToDate(value);
                    }
                    else if(field.equals("editor")) {
                        author = getPersonContent(activeNode);
                    }
                    else if(field.equals("comment")) {
                        comments.add(getCommentContent(activeNode));
                    }
                    else if(field.equals("review")) {
                        reviews.add(getReviewContent(activeNode));
                    }
                }
            }
            while((activeNode = activeNode.getNextSibling()) != null);
            return new Article(name, url, description, keywords, datePublished, author, dateModified, editor, comments, reviews);
        }
        catch(ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getNodeContent(Node node) {
        try {
            StringWriter writer = new StringWriter();
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(new DOMSource(node), new StreamResult(writer));
            String result = writer.toString();
            return result;
        }
        catch(TransformerConfigurationException e) {
            e.printStackTrace();
        }
        catch(TransformerFactoryConfigurationError e) {
            e.printStackTrace();
        }
        catch(TransformerException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Person getPersonContent(Node node) {
        Node activeNode = node;
        String givenName = null;
        String lastName = null;
        String url = null;
        String image = null;
        String award = null;
        do {
            if(Node.ELEMENT_NODE == activeNode.getNodeType()) {
                String field = activeNode.getNodeName();
                String value = activeNode.getTextContent();
                if(field.equals("name")) {
                    List<String> name = new LinkedList<String>(Arrays.asList(value.split(" ")));
                    givenName = name.remove(0);
                    if(value.split(" ").length >= 2) {
                        for(String val : name) {
                            lastName += val;
                        }
                    }
                }
                else if(field.equals("url")) {
                    url = value;
                }
                else if(field.equals("image")) {
                    image = getNodeContent(activeNode);
                }
                else if(field.equals("awards")) {
                    award = getNodeContent(activeNode);
                }
            }
        }
        while((activeNode = activeNode.getNextSibling()) != null);
        return new Person(givenName, lastName, url, image, award);
    }

    private UserComment getCommentContent(Node node) {
        try {
            Node activeNode = node;
            String commentText = null;
            Date commentTime = null;
            String discusses = null;
            Person creator = null;
            do {
                if(Node.ELEMENT_NODE == activeNode.getNodeType()) {
                    String field = activeNode.getNodeName();
                    String value = activeNode.getTextContent();
                    if(field.equals("commentText")) {
                        commentText = value;
                    }
                    else if(field.equals("commentTime")) {
                        commentTime = parseStringToDate(value);
                    }
                    else if(field.equals("discusses")) {
                        discusses = getNodeContent(activeNode);
                    }
                    else if(field.equals("creator")) {
                        creator = getPersonContent(activeNode);
                    }
                }
            }
            while((activeNode = activeNode.getNextSibling()) != null);
            return new UserComment(commentText, commentTime, discusses, creator);
        }
        catch(ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Review getReviewContent(Node node) {
        try {
            Node activeNode = node;
            String about = null;
            String url = null;
            String description = null;
            Date datePublished = null;
            Person author = null;
            Date dateModified = null;
            Person editor = null;
            List<UserComment> comments = new LinkedList<UserComment>();
            do {
                if(Node.ELEMENT_NODE == activeNode.getNodeType()) {
                    String field = activeNode.getNodeName();
                    String value = activeNode.getTextContent();
                    if(field.equals("about")) {
                        about = value;
                    }
                    else if(field.equals("url")) {
                        url = value;
                    }
                    else if(field.equals("description")) {
                        description = getNodeContent(activeNode);
                    }
                    else if(field.equals("datePublished")) {
                        datePublished = parseStringToDate(value);
                    }
                    else if(field.equals("author")) {
                        author = getPersonContent(activeNode);
                    }
                    else if(field.equals("dateModified")) {
                        datePublished = parseStringToDate(value);
                    }
                    else if(field.equals("editor")) {
                        author = getPersonContent(activeNode);
                    }
                    else if(field.equals("comments")) {
                        comments.add(getCommentContent(activeNode));
                    }
                }
            }
            while((activeNode = activeNode.getNextSibling()) != null);
            return new Review(about, url, description, datePublished, author, dateModified, editor, comments);
        }
        catch(ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Date parseStringToDate(String date) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        return df.parse(date.substring(0, date.length() - 1));
    }

}
