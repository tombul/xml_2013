package fu.berlin.de.webdatabrowser.deep.rdf.resources;

import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.vocabulary.DC;

import fu.berlin.de.webdatabrowser.deep.rdf.DeebResource;

public class Publication extends DeebResource {

    private String                        title;
    private String                        identifier;
    private Person                        creator;
    private String                        date;
    private String                        description;
    private String                        publisher;
    private List<String>                  subjects;

    private static final DeebPropertyType PROPERTY_TYPE = DeebPropertyType.PUBLICATION;

    @Override
    protected DeebPropertyType getPropertyType() {
        return PROPERTY_TYPE;
    }

    public Publication(String identifier) {
        super(identifier);
    }

    public Publication(String title, String identifier, Person creator, String date, String description, String publisher, List<String> subjects) {
        super(identifier);
        this.title = title;
        this.identifier = identifier;
        this.creator = creator;
        this.date = date;
        this.description = description;
        this.publisher = publisher;
        this.subjects = subjects;
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

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
        if(getResource() != null) {
            getResource().removeAll(DC.identifier);
            getResource().addProperty(DC.identifier, identifier);
        }
    }

    public Person getCreator() {
        return creator;
    }

    public void setCreator(Person creator) {
        this.creator = creator;
        if(getResource() != null) {
            getResource().removeAll(DC.creator);
            creator.saveInModel(getResource().getModel());
            getResource().addProperty(DC.creator, creator.getResource());
        }
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
        if(getResource() != null) {
            getResource().removeAll(DC.date);
            getResource().addProperty(DC.date, date);
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

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
        if(getResource() != null) {
            getResource().removeAll(DC.publisher);
            getResource().addProperty(DC.publisher, publisher);
        }
    }

    public List<String> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<String> subjects) {
        this.subjects = subjects;
        if(getResource() != null) {
            getResource().removeAll(DC.subject);
            for(String subject : subjects) {
                getResource().addProperty(DC.subject, subject);
            }
        }
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

        if(resource.getProperty(DC.identifier) != null)
            this.identifier = resource.getProperty(DC.identifier).getString();

        if(resource.getProperty(DC.creator) != null) {
            Resource creatorResource = resource.getProperty(DC.creator).getResource();

            Person creator = new Person(creatorResource.getLocalName());
            creator.fromResource(creatorResource);
            this.creator = creator;
        }

        if(resource.getProperty(DC.date) != null) {

            this.date = resource.getProperty(DC.date).getString();
        }

        if(resource.getProperty(DC.description) != null)
            this.description = resource.getProperty(DC.description).getString();
        if(resource.getProperty(DC.publisher) != null)
            this.publisher = resource.getProperty(DC.publisher).getString();

        List<String> subjects = new ArrayList<String>();
        if(resource.getProperty(DC.subject) != null)
            for(Statement subjectProperty : resource.listProperties(DC.subject).toList()) {
                subjects.add(subjectProperty.getString());
            }

        this.subjects = subjects;
        return this;
    }

    @Override
    public void saveInModel(Model model) {
        super.saveInModel(model);

        if(getTitle() != null)
            getResource().addProperty(DC.title, getTitle());
        if(getIdentifier() != null)
            getResource().addProperty(DC.identifier, getIdentifier());
        if(getCreator() != null) {
            if(getCreator().getResource() == null)
                getCreator().saveInModel(model);
            getResource().addProperty(DC.creator, getCreator().getResource());
        }
        if(getDate() != null)
            getResource().addProperty(DC.date, getDate());
        if(getDescription() != null)
            getResource().addProperty(DC.description, getDescription());
        if(getPublisher() != null)
            getResource().addProperty(DC.publisher, getPublisher());
        if(getSubjects() != null) {
            for(String subject : getSubjects()) {
                getResource().addProperty(DC.subject, subject);
            }
        }
    }

    @Override
    public String getHtml() {
        String html = "<div style=\"width:90%;padding:10px;\">" +
                "<table border=\"2\">" +
                "<tr>" +
                "<td>" +
                "Title" +
                "</td>" +
                "<td>" +
                this.title +
                "</td>" +
                "<tr>" +
                "<td>" +
                "Identifier" +
                "</td>" +
                "<td>" +
                "<a href=\"" + this.identifier + "\">" + this.identifier + "</a>" +
                "</td>" +
                "<tr>" +
                "<td>" +
                "Creator" +
                "</td>" +
                "<td>" +
                this.creator.getHtml() +
                "</td>" +
                "<tr>" +
                "<td>" +
                "Description" +
                "</td>" +
                "<td>" +
                this.description +
                "</td>" +
                "<tr>" +
                "<td>" +
                "Publisher" +
                "</td>" +
                "<td>" +
                this.publisher +
                "</td>" +
                "<tr>" +
                "<td>" +
                "Date" +
                "</td>" +
                "<td>" +
                this.date +
                "</td>" +
                "<tr>" +
                "<td>" +
                "Subjects" +
                "</td>" +
                "<td>";
        for(String subject : subjects) {
            html += subject + "<br/>";

        }
        html += "</td></table></div>";
        return html;
    }
}
