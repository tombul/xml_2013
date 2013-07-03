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

public class Article extends DeebResource {

    private String                        name;
    private String                        url;
    private String                        description;
    private String                        keywords;
    private Date                          datePublished;
    private Person                        author;
    private Date                          dateModified;
    private Person                        editor;
    private List<UserComment>             comments;
    private List<Review>                  reviews;

    private static final DeebPropertyType PROPERTY_TYPE = DeebPropertyType.ARTICLE;

    @Override
    protected DeebPropertyType getPropertyType() {
        return PROPERTY_TYPE;
    }

    public Article(String identifier) {
        super(identifier);
    }

    public Article(String name, String url, String description, String keywords, Date datePublished, Person author) {
        super(url);
        this.name = name;
        this.url = url;
        this.description = description;
        this.keywords = keywords;
        this.datePublished = datePublished;
        this.author = author;
    }

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
            getResource().addProperty(Schema.datePublished, DateFormat.getDateTimeInstance().format(datePublished));
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

        if(getName() != null)
            getResource().addProperty(Schema.name, getName());
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
        if(getComments() != null) {
            for(UserComment comment : getComments()) {
                if(comment.getResource() == null)
                    comment.saveInModel(model);
                getResource().addProperty(Schema.comment, comment.getResource());
            }
        }
        if(getReviews() != null) {
            for(Review review : getReviews()) {
                if(review.getResource() == null)
                    review.saveInModel(model);
                getResource().addProperty(Schema.review, review.getResource());
            }
        }

    }

    @Override
    public String getHeaderHtml() {
        String cssInclude = "<head><link href=\"http://cdn.sstatic.net/stackoverflow/all.css?v=db16ef7a3fac\" type=\"text/css\" rel=\"stylesheet\"></link></head>";
        return cssInclude;
    }

    @Override
    public String getHtml() {
        String html = "<body style=\"width:100%;\">";
        html += "<div style=\"padding:10px;width:95%;\"><div style=\"background-color:#036;font-weight:bold;font-size:15px;color:#FFF;margin-bottom:25px;\">" + name + "</div>";
        html += "<div style=\"background-color:#06C;display:inline-block;\">" + description + "</div>";
        html += "<div>";
        if(editor != null) {
            html += "<div style=\"display:inline-block;float:left;\">" + editor.getHtml();
        }
        if(dateModified != null) {
            html += "<div>edited at: " + dateModified + "</div></div>";
        }
        html += "<div style=\"display:inline-block\"><a href=\"" + url + "\" title=\"Link to this question\">Link to this site</a></div>";
        html += "<div style=\"display:inline-block\">" + keywords + "</div>";
        html += author.getHtml();
        html += "<div style=\"display:inline-block\"><div style=\"display:inline-block;margin-right:15px;\">asked at:</div><div style=\"display:inline-block\">" + datePublished + "</div></div";
        html += "</div>";
        if(comments != null && !comments.isEmpty()) {
            for(UserComment comment : comments) {
                html += comment.getHtml();
            }
        }
        if(reviews != null && !reviews.isEmpty()) {
            for(Review review : reviews) {
                html += review.getHtml();
            }
        }
        html += "</div></body>";
        return html;
    }

}
