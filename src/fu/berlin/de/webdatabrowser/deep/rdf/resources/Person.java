package fu.berlin.de.webdatabrowser.deep.rdf.resources;

import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.vocabulary.VCARD;

import fu.berlin.de.webdatabrowser.deep.rdf.DeebResource;

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

    private String givenName = "";
    private String lastName  = "";
    private String url       = "";
    private String image     = "";
    private String award     = "";

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
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAward() {
        return award;
    }

    public void setAward(String award) {
        this.award = award;
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

        String givenName = resource.getProperty(VCARD.Given).getString();
        String lastName = resource.getProperty(VCARD.NAME).getString();

        this.givenName = givenName;
        this.lastName = lastName;

        return this;
    }

    @Override
    public void saveInModel(Model model) {
        Resource resource = model.createResource(getIdentifier());

        resource.addProperty(VCARD.Given, getGivenName());
        resource.addProperty(VCARD.NAME, getLastName());
    }

    @Override
    public String getHtml() {
        // TODO Auto-generated method stub
        return null;
    }

}
