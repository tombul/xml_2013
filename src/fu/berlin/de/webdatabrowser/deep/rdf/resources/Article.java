package fu.berlin.de.webdatabrowser.deep.rdf.resources;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;

import fu.berlin.de.webdatabrowser.deep.rdf.DeebResource;
import fu.berlin.de.webdatabrowser.deep.vocabulary.Deeb;
import fu.berlin.de.webdatabrowser.deep.vocabulary.Schema;

public class Article extends DeebResource {

    private String                        name;
    private String                        url;
    private String                        description;
    private String                        keywords;
    private Date                          datePublished;
    private Person                        author;
    private List<UserComment>             comments;
    private List<Review>                  reviews;

    private static final DeebPropertyType PROPERTY_TYPE = DeebPropertyType.ARTICLE;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        if(getResource() != null) {
            getResource().removeAll(Schema.name);
            getResource().addProperty(Schema.name, name);
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

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
        if(getResource() != null) {
            getResource().removeAll(Schema.keywords);
            getResource().addProperty(Schema.keywords, keywords);
        }
    }

    public Date getDatePublished() {
        return datePublished;
    }

    public void setDatePublished(Date datePublished) {
        this.datePublished = datePublished;
        if(getResource() != null) {
            getResource().removeAll(Schema.datePublished);
            getResource().addProperty(Schema.datePublished, SimpleDateFormat.getDateTimeInstance().format(datePublished));
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

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
        if(getResource() != null) {
            getResource().removeAll(Schema.review);
            for(Review review : reviews) {
                if(review.getResource() == null)
                    review.saveInModel(getResource().getModel());
                getResource().addProperty(Schema.review, review.getResource());
            }
        }
    }

    public Article(String identifier) {
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

        if(resource.getProperty(Schema.name) != null)
            this.name = resource.getProperty(Schema.name).getString();
        if(resource.getProperty(Schema.URL) != null)
            this.url = resource.getProperty(Schema.URL).getString();
        if(resource.getProperty(Schema.datePublished) != null)
            try {
                this.datePublished = SimpleDateFormat.getDateTimeInstance().parse(resource.getProperty(Schema.datePublished).getString());
            }
            catch(ParseException e) {
            }
        if(resource.getProperty(Schema.author) != null)
            this.author = (Person) DeebResource.createResource(resource.getProperty(Schema.author).getResource());
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
        Resource resource = model.createResource(getIdentifier());
        model.remove(resource.listProperties());
        setResource(resource);

        if(getName() != null)
            resource.addProperty(Schema.name, getName());
        if(getUrl() != null)
            resource.addProperty(Schema.URL, getUrl());
        if(getDescription() != null)
            resource.addProperty(Schema.description, getDescription());
        if(getDatePublished() != null)
            resource.addProperty(Schema.datePublished, SimpleDateFormat.getDateTimeInstance().format(getDatePublished()));
        if(getAuthor() != null) {
            if(getAuthor().getResource() == null)
                getAuthor().saveInModel(model);
            resource.addProperty(Schema.author, getAuthor().getResource());
        }
        if(getComments() != null) {
            for(UserComment comment : getComments()) {
                if(comment.getResource() == null)
                    comment.saveInModel(model);
                resource.addProperty(Schema.comment, comment.getResource());
            }
        }
        if(getReviews() != null) {
            for(Review review : getReviews()) {
                if(review.getResource() == null)
                    review.saveInModel(model);
                resource.addProperty(Schema.review, review.getResource());
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
