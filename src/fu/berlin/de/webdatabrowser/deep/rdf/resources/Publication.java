package fu.berlin.de.webdatabrowser.deep.rdf.resources;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.vocabulary.DC;

import fu.berlin.de.webdatabrowser.deep.rdf.DeebResource;

public class Publication extends DeebResource {

    private String       title;
    private Person       creator;
    private Date         date;
    private String       description;
    private String       publisher;
    private List<String> subjects;

    public Publication(String identifier) {
        super(identifier);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Person getCreator() {
        return creator;
    }

    public void setCreator(Person creator) {
        this.creator = creator;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public List<String> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<String> subjects) {
        this.subjects = subjects;
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

        setTitle(resource.getProperty(DC.title).getString());

        Resource creatorResource = resource.getProperty(DC.creator).getResource();
        Person creator = new Person(creatorResource.getLocalName());
        creator.fromResource(creatorResource);
        setCreator(creator);

        try {
            setDate(SimpleDateFormat.getDateInstance().parse(resource.getProperty(DC.date).getString()));
        }
        catch(ParseException e) {
        }

        setDescription(resource.getProperty(DC.description).getString());
        setPublisher(resource.getProperty(DC.publisher).getString());

        List<String> subjects = new ArrayList<String>();
        subjects.add(resource.getProperty(DC.subject).getString());

        return this;
    }

    @Override
    public void saveInModel(Model model) {
        Resource resource = model.createResource(getIdentifier());

        resource.addProperty(DC.title, getTitle());
        resource.addProperty(DC.creator, getCreator().getResource());
        resource.addProperty(DC.date, SimpleDateFormat.getDateInstance().format(getDate()));
        resource.addProperty(DC.description, getDescription());
        resource.addProperty(DC.publisher, getPublisher());
        for(String subject : getSubjects()) {
            resource.addProperty(DC.subject, subject);
        }
    }

    @Override
    public String getHtml() {
        // TODO Auto-generated method stub
        return null;
    }
}
