package fu.berlin.de.webdatabrowser.deep.rdf.resources;

import java.util.Date;
import java.util.List;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;

import fu.berlin.de.webdatabrowser.deep.rdf.DeebResource;

public class Review extends DeebResource {

    private Article           about;
    private String            url;
    private String            description;
    private Date              datePublished;
    private Person            author;
    private Date              dateModified;
    private Date              editor;
    private List<UserComment> comments;

    public Article getAbout() {
        return about;
    }

    public void setAbout(Article about) {
        this.about = about;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDatePublished() {
        return datePublished;
    }

    public void setDatePublished(Date datePublished) {
        this.datePublished = datePublished;
    }

    public Person getAuthor() {
        return author;
    }

    public void setAuthor(Person author) {
        this.author = author;
    }

    public Date getDateModified() {
        return dateModified;
    }

    public void setDateModified(Date dateModified) {
        this.dateModified = dateModified;
    }

    public Date getEditor() {
        return editor;
    }

    public void setEditor(Date editor) {
        this.editor = editor;
    }

    public List<UserComment> getComments() {
        return comments;
    }

    public void setComments(List<UserComment> comments) {
        this.comments = comments;
    }

    public Review(String identifier) {
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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void saveInModel(Model model) {
        // TODO Auto-generated method stub

    }

}
