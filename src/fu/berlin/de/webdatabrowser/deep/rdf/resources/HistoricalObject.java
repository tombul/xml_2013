package fu.berlin.de.webdatabrowser.deep.rdf.resources;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.vocabulary.DC;

import fu.berlin.de.webdatabrowser.deep.rdf.DeebResource;
import fu.berlin.de.webdatabrowser.deep.vocabulary.Europeana;

public class HistoricalObject extends DeebResource {

    private String                        title;
    private String                        type;
    private String                        description;
    private String                        language;
    private String                        country;
    private String                        uri;
    private String                        provider;
    private String                        preview;

    private static final DeebPropertyType PROPERTY_TYPE = DeebPropertyType.HISTORICAL_OBJECT;

    @Override
    protected DeebPropertyType getPropertyType() {
        return PROPERTY_TYPE;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        if(getResource() != null) {
            getResource().removeAll(DC.title);
            getResource().addProperty(DC.title, title);
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
        if(getResource() != null) {
            getResource().removeAll(DC.type);
            getResource().addProperty(DC.type, type);
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        if(getResource() != null) {
            getResource().removeAll(DC.description);
            getResource().addProperty(DC.description, description);
        }
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
        if(getResource() != null) {
            getResource().removeAll(DC.language);
            getResource().addProperty(DC.language, language);
        }
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
        if(getResource() != null) {
            getResource().removeAll(Europeana.country);
            getResource().addProperty(Europeana.country, country);
        }
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
        if(getResource() != null) {
            getResource().removeAll(Europeana.URI);
            getResource().addProperty(Europeana.URI, uri);
        }
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
        if(getResource() != null) {
            getResource().removeAll(Europeana.provider);
            getResource().addProperty(Europeana.provider, provider);
        }
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
        if(getResource() != null) {
            getResource().removeAll(Europeana.preview);
            getResource().addProperty(Europeana.preview, preview);
        }
    }

    public HistoricalObject(String identifier) {
        super(identifier);
    }

    public HistoricalObject(String identifier, String title, String type, String description, String language, String country, String uri, String provider, String preview) {
        super(identifier);
        this.title = title;
        this.type = type;
        this.description = description;
        this.language = language;
        this.country = country;
        this.uri = uri;
        this.provider = provider;
        this.preview = preview;
    }

    @Override
    public List<Statement> getStatements() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public DeebResource fromStatements(List<Statement> typeStatements, Resource resource) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public DeebResource fromResource(Resource resource) {
        setResource(resource);

        if(resource.getProperty(DC.title) != null)
            this.title = resource.getProperty(DC.title).getString();
        if(resource.getProperty(DC.type) != null) {
            List<String> types = new ArrayList<String>();
            for(Statement type : resource.listProperties(DC.type).toList()) {
                types.add(type.getString());
            }
        }
        if(resource.getProperty(DC.description) != null)
            this.description = resource.getProperty(DC.description).getString();
        if(resource.getProperty(Europeana.language) != null)
            this.language = resource.getProperty(Europeana.language).getString();
        if(resource.getProperty(Europeana.country) != null)
            this.language = resource.getProperty(Europeana.country).getString();
        if(resource.getProperty(Europeana.URI) != null)
            this.uri = resource.getProperty(Europeana.URI).getString();
        if(resource.getProperty(Europeana.provider) != null)
            this.provider = resource.getProperty(Europeana.provider).getString();
        if(resource.getProperty(Europeana.preview) != null)
            this.preview = resource.getProperty(Europeana.preview).getString();

        return this;
    }

    @Override
    public void saveInModel(Model model) {
        super.saveInModel(model);

        if(getTitle() != null)
            getResource().addProperty(DC.title, getTitle());
        if(getType() != null) {
            getResource().addProperty(DC.type, type);
        }
        if(getDescription() != null)
            getResource().addProperty(DC.description, getDescription());
        if(getLanguage() != null)
            getResource().addProperty(Europeana.language, getLanguage());
        if(getCountry() != null)
            getResource().addProperty(Europeana.country, getCountry());
        if(getUri() != null)
            getResource().addProperty(Europeana.URI, getUri());
        if(getProvider() != null)
            getResource().addProperty(Europeana.provider, getProvider());
        if(getPreview() != null)
            getResource().addProperty(Europeana.preview, getPreview());

    }

    @Override
    public String getHtml() {
        // Table BEGIN
        String html = "<table style=\"width:100%px; font-size:1.3em;" +
                "border-collapse:collapse;\">";

        // Title
        html += "<tr><td style=\"text-align:" +
                "center; background-color:#FF9; padding:2px 0 2px 0; " +
                "font-size:1.7em\"><strong>";
        html += title;
        html += "</strong></td></tr>";

        // Preview
        html += "<tr><td><img src=\"";
        preview = preview.substring(40);
        html += preview;
        html += "\" style=\"width:100%\"/></td></tr>";

        // Type
        html += "<tr><td style=\"padding:4px; background-color" +
                ":#FAF7BA;\"><strong>Typ: </strong>";
        html += type;
        html += "</td></tr>";

        // Description
        html += "<tr><td style=\"background-color:#FFC; padding:4px;" +
                "word-break:break-all; word-wrap:break-word;\"><strong>" +
                "Beschreibung:</strong><br />";
        html += description;
        html += "</td></tr>";

        // Country
        html += "<tr><td style=\"background-color:#FAF7BA; padding:4px" +
                ";\"><strong>Land: </strong>";
        html += country;
        html += "</td></tr>";

        // Language
        html += "<tr><td style=\"padding:4px; background-color:#FFC;\"" +
                "><strong>Sprache: </strong>";
        html += language;
        html += "</td></tr>";

        // URL
        html += "<tr><td style=\"width:300px; word-break:break-all;" +
                "word-wrap:break-word; background-color:#FAF7BA;" +
                "padding:4px;\"><strong>URL:<br /></strong><a href=\"";
        html += uri;
        html += "\" style=\"text-decoration:none; color:black; font-" +
                "style:italic;\">";
        try {
            html += URLDecoder.decode(uri, "UTF-8");
        }
        catch(UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        html += "</a></td></tr>";

        // Provider
        html += "<tr><td style=\"padding:4px; background-color:#FFC;\"" +
                "><strong>Anbieter: </strong><br />";
        html += provider;
        html += "</td></tr>";

        // Table END
        html += "</table>";

        return html;
    }

}
