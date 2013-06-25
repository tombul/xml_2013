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
import fu.berlin.de.webdatabrowser.deep.vocabulary.Deeb;

public class Publication extends DeebResource {

    private String                        title;
    private Person                        creator;
    private Date                          date;
    private String                        description;
    private String                        publisher;
    private List<String>                  subjects;

    private static final DeebPropertyType PROPERTY_TYPE = DeebPropertyType.PUBLICATION;

    public Publication(String identifier) {
        super(identifier);
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
        if(getResource() != null) {
            getResource().removeAll(DC.date);
            getResource().addProperty(DC.date, SimpleDateFormat.getDateTimeInstance().format(date));
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

        if(resource.getProperty(DC.creator) != null) {
            Resource creatorResource = resource.getProperty(DC.creator).getResource();

            Person creator = new Person(creatorResource.getLocalName());
            creator.fromResource(creatorResource);
            this.creator = creator;
        }

        if(resource.getProperty(DC.date) != null) {
            try {
                this.date = SimpleDateFormat.getDateTimeInstance().parse(resource.getProperty(DC.date).getString());
            }
            catch(ParseException e) {
            }
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
        Resource resource = model.createResource(getIdentifier());
        model.remove(resource.listProperties());
        setResource(resource);

        if(getTitle() != null)
            resource.addProperty(DC.title, getTitle());
        if(getCreator() != null) {
            if(getCreator().getResource() == null)
                getCreator().saveInModel(model);
            resource.addProperty(DC.creator, getCreator().getResource());
        }
        if(getDate() != null)
            resource.addProperty(DC.date, SimpleDateFormat.getDateTimeInstance().format(getDate()));
        if(getDescription() != null)
            resource.addProperty(DC.description, getDescription());
        if(getPublisher() != null)
            resource.addProperty(DC.publisher, getPublisher());
        if(getSubjects() != null) {
            for(String subject : getSubjects()) {
                resource.addProperty(DC.subject, subject);
            }
        }
        resource.addProperty(Deeb.ResourceType, PROPERTY_TYPE.toString());
    }

    @Override
    public String getHtml() {
        // TODO Auto-generated method stub
        return null;
    }
}
