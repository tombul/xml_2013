package fu.berlin.de.webdatabrowser.deep.rdf.resources;

import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.vocabulary.DC;

import fu.berlin.de.webdatabrowser.deep.rdf.DeebResource;
import fu.berlin.de.webdatabrowser.deep.vocabulary.Deeb;
import fu.berlin.de.webdatabrowser.deep.vocabulary.Europeana;

public class HistoricalObject extends DeebResource {

    private String                        title;
    private List<String>                  type;
    private String                        description;
    private String                        language;
    private String                        country;
    private String                        uri;
    private String                        provider;
    private String                        preview;

    private static final DeebPropertyType PROPERTY_TYPE = DeebPropertyType.HISTORICAL_OBJECT;

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

    public List<String> getType() {
        return type;
    }

    public void setType(List<String> type) {
        this.type = type;
        if(getResource() != null) {
            getResource().removeAll(DC.type);
            for(String typeString : type)
                getResource().addProperty(DC.type, typeString);
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
        Resource resource = model.createResource(getIdentifier());
        model.remove(resource.listProperties());
        setResource(resource);

        if(getTitle() != null)
            resource.addProperty(DC.title, getTitle());
        if(getType() != null) {
            for(String type : getType()) {
                resource.addProperty(DC.type, type);
            }
        }
        if(getDescription() != null)
            resource.addProperty(DC.description, getDescription());
        if(getLanguage() != null)
            resource.addProperty(Europeana.language, getLanguage());
        if(getCountry() != null)
            resource.addProperty(Europeana.country, getCountry());
        if(getUri() != null)
            resource.addProperty(Europeana.URI, getUri());
        if(getProvider() != null)
            resource.addProperty(Europeana.provider, getProvider());
        if(getPreview() != null)
            resource.addProperty(Europeana.preview, getPreview());

        resource.addProperty(Deeb.ResourceType, PROPERTY_TYPE.toString());
    }

    @Override
    public String getHtml() {
        // TODO Auto-generated method stub
        return null;
    }

}
