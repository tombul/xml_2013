package fu.berlin.de.webdatabrowser.deep.rdf.resources;

import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.vocabulary.VCARD;

import fu.berlin.de.webdatabrowser.deep.rdf.DeebResource;
import fu.berlin.de.webdatabrowser.deep.vocabulary.Schema;

/**
 * Beispielhafte Implementierung einer {@link DeebResource} am Beispiel einer
 * Person.
 * 
 * @author Jan-Christopher Pien
 *         27.05.2013
 * 
 */
public class Person extends DeebResource {

    public Person(String identifier) {
        super(identifier);
    }

    private String                        givenName;
    private String                        lastName;
    private String                        url;
    private String                        image;
    private String                        award;

    private static final DeebPropertyType PROPERTY_TYPE = DeebPropertyType.PERSON;

    @Override
    protected DeebPropertyType getPropertyType() {
        return PROPERTY_TYPE;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
        if(getResource() != null) {
            getResource().addProperty(VCARD.Given, givenName);
        }
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        if(getResource() != null) {
            getResource().addProperty(VCARD.NAME, lastName);
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
        if(getResource() != null) {
            getResource().addProperty(Schema.URL, url);
        }
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
        if(getResource() != null) {
            getResource().addProperty(Schema.image, image);
        }
    }

    public String getAward() {
        return award;
    }

    public void setAward(String award) {
        this.award = award;
        if(getResource() != null) {
            getResource().addProperty(Schema.award, award);
        }
    }

    @Override
    public DeebResource fromStatements(List<Statement> typeStatements,
            Resource resource) {
        Person result = new Person(resource.getURI());

        return result;
    }

    @Override
    public List<Statement> getStatements() {
        Statement givenStatement = getResource().getProperty(VCARD.Given);
        Statement lastStatement = getResource().getProperty(VCARD.NAME);
        List<Statement> statementList = new ArrayList<Statement>();
        statementList.add(givenStatement);
        statementList.add(lastStatement);
        return statementList;
    }

    @Override
    public DeebResource fromResource(Resource resource) {
        setResource(resource);

        if(resource.getProperty(VCARD.Given) != null)
            this.givenName = resource.getProperty(VCARD.Given).getString();
        if(resource.getProperty(VCARD.NAME) != null)
            this.lastName = resource.getProperty(VCARD.NAME).getString();
        if(resource.getProperty(Schema.URL) != null)
            this.url = resource.getProperty(Schema.URL).getString();
        if(resource.getProperty(Schema.image) != null)
            this.image = resource.getProperty(Schema.image).getString();
        if(resource.getProperty(Schema.award) != null)
            this.award = resource.getProperty(Schema.award).getString();

        return this;
    }

    @Override
    public void saveInModel(Model model) {
        super.saveInModel(model);

        if(getGivenName() != null)
            getResource().addProperty(VCARD.Given, getGivenName());
        if(getLastName() != null)
            getResource().addProperty(VCARD.NAME, getLastName());
        if(getUrl() != null)
            getResource().addProperty(Schema.URL, getUrl());
        if(getImage() != null)
            getResource().addProperty(Schema.image, getImage());
        if(getAward() != null)
            getResource().addProperty(Schema.award, getAward());
    }

    @Override
    public String getHtml() {
        // TODO Auto-generated method stub
        String html = "<div style=\"background-color:#FAC; padding:4px;" + "word-break:break-all; word-wrap:break-word;\">";
        if(givenName == null) {
            html += lastName;
        }
        else {
            html += givenName + " " + lastName;
        }
        html += "</div>";
        return html;
    }

}
