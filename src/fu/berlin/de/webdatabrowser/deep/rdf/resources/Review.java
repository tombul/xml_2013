package fu.berlin.de.webdatabrowser.deep.rdf.resources;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;

import fu.berlin.de.webdatabrowser.deep.rdf.DeebResource;
import fu.berlin.de.webdatabrowser.deep.vocabulary.Schema;

public class Review extends DeebResource {

    private String                        about;
    private String                        url;
    private String                        description;
    private Date                          datePublished;
    private Person                        author;
    private Date                          dateModified;
    private Person                        editor;
    private List<UserComment>             comments;

    private static final DeebPropertyType PROPERTY_TYPE = DeebPropertyType.REVIEW;

    @Override
    protected DeebPropertyType getPropertyType() {
        return PROPERTY_TYPE;
    }

    public Review(String identifier) {
        super(identifier);
    }

    public Review(String about, String url, String description, Date datePublished, Person author, Date dateModified, Person editor, List<UserComment> comments) {
        super(url);
        this.about = about;
        this.url = url;
        this.description = description;
        this.datePublished = datePublished;
        this.author = author;
        this.dateModified = dateModified;
        this.editor = editor;
        this.comments = comments;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
        if(getResource() != null) {
            getResource().removeAll(Schema.about);
            getResource().addProperty(Schema.about, about);
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
        if(getResource() != null) {
            getResource().removeAll(Schema.URL);
            getResource().addProperty(Schema.URL, url);
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        if(getResource() != null) {
            getResource().removeAll(Schema.description);
            getResource().addProperty(Schema.description, description);
        }
    }

    public Date getDatePublished() {
        return datePublished;
    }

    public void setDatePublished(Date datePublished) {
        this.datePublished = datePublished;
        if(getResource() != null) {
            getResource().removeAll(Schema.datePublished);
            getResource().addProperty(Schema.datePublished, DateFormat.getDateTimeInstance().format(datePublished));
        }
    }

    public Date getDateModified() {
        return dateModified;
    }

    public void setDateModified(Date dateModified) {
        this.dateModified = dateModified;
        if(getResource() != null) {
            getResource().removeAll(Schema.dateModified);
            getResource().addProperty(Schema.dateModified, DateFormat.getDateTimeInstance().format(dateModified));
        }
    }

    public Person getAuthor() {
        return author;
    }

    public void setAuthor(Person author) {
        this.author = author;
        if(getResource() != null) {
            getResource().removeAll(Schema.author);
            if(author.getResource() == null)
                author.saveInModel(getResource().getModel());
            getResource().addProperty(Schema.author, author.getResource());
        }
    }

    public Person getEditor() {
        return editor;
    }

    public void setEditor(Person editor) {
        this.editor = editor;
        if(getResource() != null) {
            getResource().removeAll(Schema.editor);
            if(editor.getResource() == null)
                editor.saveInModel(getResource().getModel());
            getResource().addProperty(Schema.editor, editor.getResource());
        }
    }

    public List<UserComment> getComments() {
        return comments;
    }

    public void setComments(List<UserComment> comments) {
        this.comments = comments;
        if(getResource() != null) {
            getResource().removeAll(Schema.comment);
            for(UserComment comment : comments) {
                if(comment.getResource() == null)
                    comment.saveInModel(getResource().getModel());
                getResource().addProperty(Schema.comment, comment.getResource());
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

        if(resource.getProperty(Schema.about) != null)
            this.about = resource.getProperty(Schema.about).getString();
        if(resource.getProperty(Schema.URL) != null)
            this.url = resource.getProperty(Schema.URL).getString();
        if(resource.getProperty(Schema.datePublished) != null)
            try {
                this.datePublished = DateFormat.getDateTimeInstance().parse(resource.getProperty(Schema.datePublished).getString());
            }
            catch(ParseException e) {
            }
        if(resource.getProperty(Schema.author) != null)
            this.author = (Person) DeebResource.createResource(resource.getProperty(Schema.author).getResource());
        if(resource.getProperty(Schema.dateModified) != null)
            try {
                this.dateModified = DateFormat.getDateTimeInstance().parse(resource.getProperty(Schema.dateModified).getString());
            }
            catch(ParseException e) {
            }
        if(resource.getProperty(Schema.editor) != null)
            this.editor = (Person) DeebResource.createResource(resource.getProperty(Schema.editor).getResource());
        if(resource.getProperty(Schema.comment) != null) {
            List<UserComment> comments = new ArrayList<UserComment>();
            for(Statement comment : resource.listProperties(Schema.comment).toList()) {
                comments.add((UserComment) DeebResource.createResource(comment.getResource()));
            }
        }
        if(resource.getProperty(Schema.review) != null) {
            List<Review> reviews = new ArrayList<Review>();
            for(Statement review : resource.listProperties(Schema.review).toList()) {
                reviews.add((Review) DeebResource.createResource(review.getResource()));
            }
        }

        return this;
    }

    @Override
    public void saveInModel(Model model) {
        super.saveInModel(model);

        if(getAbout() != null)
            getResource().addProperty(Schema.about, getAbout());
        if(getUrl() != null)
            getResource().addProperty(Schema.URL, getUrl());
        if(getDescription() != null)
            getResource().addProperty(Schema.description, getDescription());
        if(getDatePublished() != null)
            getResource().addProperty(Schema.datePublished, DateFormat.getDateTimeInstance().format(getDatePublished()));
        if(getAuthor() != null) {
            if(getAuthor().getResource() == null)
                getAuthor().saveInModel(model);
            getResource().addProperty(Schema.author, getAuthor().getResource());
        }
        if(getDateModified() != null)
            getResource().addProperty(Schema.dateModified, DateFormat.getDateTimeInstance().format(getDateModified()));
        if(getEditor() != null) {
            if(getEditor().getResource() == null)
                getEditor().saveInModel(model);
            getResource().addProperty(Schema.editor, getEditor().getResource());
        }
        if(getEditor() != null) {
            if(getEditor().getResource() == null)
                getEditor().saveInModel(model);
            getResource().addProperty(Schema.editor, getEditor().getResource());
        }
        if(getComments() != null) {
            for(UserComment comment : getComments()) {
                if(comment.getResource() == null)
                    comment.saveInModel(model);
                getResource().addProperty(Schema.comment, comment.getResource());
            }
        }

    }

    @Override
    public String getHtml() {
        // TODO Auto-generated method stub
        return null;
    }
}
