package fu.berlin.de.webdatabrowser.deep.rdf.resources;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;

import fu.berlin.de.webdatabrowser.deep.rdf.DeebResource;
import fu.berlin.de.webdatabrowser.deep.vocabulary.Schema;

public class UserComment extends DeebResource {

    private String                        commentText;
    private Date                          commentTime;
    private Article                       discusses;
    private Person                        creator;

    private static final DeebPropertyType PROPERTY_TYPE = DeebPropertyType.USER_COMMENT;

    @Override
    protected DeebPropertyType getPropertyType() {
        return PROPERTY_TYPE;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
        if(getResource() != null) {
            getResource().removeAll(Schema.commentText);
            getResource().addProperty(Schema.commentText, commentText);
        }
    }

    public Date getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(Date commentTime) {
        this.commentTime = commentTime;
        if(getResource() != null) {
            getResource().removeAll(Schema.commentTime);
            getResource().addProperty(Schema.commentTime, DateFormat.getDateTimeInstance().format(commentTime));
        }
    }

    public Article getDiscusses() {
        return discusses;
    }

    public void setDiscusses(Article discusses) {
        this.discusses = discusses;
        if(getResource() != null) {
            getResource().removeAll(Schema.discusses);
            if(discusses.getResource() == null)
                discusses.saveInModel(getResource().getModel());
            getResource().addProperty(Schema.discusses, discusses.getResource());
        }
    }

    public Person getCreator() {
        return creator;
    }

    public void setCreator(Person creator) {
        this.creator = creator;
        if(getResource() != null) {
            getResource().removeAll(Schema.creator);
            if(creator.getResource() == null)
                creator.saveInModel(getResource().getModel());
            getResource().addProperty(Schema.creator, creator.getResource());
        }
    }

    public UserComment(String identifier) {
        super(identifier);
        // TODO Auto-generated constructor stub
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

        if(resource.getProperty(Schema.commentText) != null)
            this.commentText = resource.getProperty(Schema.commentText).getString();
        if(resource.getProperty(Schema.commentTime) != null)
            try {
                this.commentTime = DateFormat.getDateTimeInstance().parse(resource.getProperty(Schema.datePublished).getString());
            }
            catch(ParseException e) {
            }
        if(resource.getProperty(Schema.creator) != null)
            this.creator = (Person) DeebResource.createResource(resource.getProperty(Schema.creator).getResource());
        if(resource.getProperty(Schema.discusses) != null)
            this.discusses = (Article) DeebResource.createResource(resource.getProperty(Schema.discusses).getResource());

        return this;
    }

    @Override
    public void saveInModel(Model model) {
        super.saveInModel(model);

        if(getCommentText() != null)
            getResource().addProperty(Schema.commentText, getCommentText());
        if(getCommentTime() != null)
            getResource().addProperty(Schema.commentTime, DateFormat.getDateTimeInstance().format(getCommentTime()));
        if(getCreator() != null) {
            if(getCreator().getResource() == null)
                getCreator().saveInModel(model);
            getResource().addProperty(Schema.creator, getCreator().getResource());
        }
        if(getDiscusses() != null) {
            if(getDiscusses().getResource() == null)
                getDiscusses().saveInModel(model);
            getResource().addProperty(Schema.discusses, getDiscusses().getResource());
        }

    }

    @Override
    public String getHtml() {
        // TODO Auto-generated method stub
        return null;
    }

}
