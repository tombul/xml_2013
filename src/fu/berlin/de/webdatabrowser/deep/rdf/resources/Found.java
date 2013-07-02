package fu.berlin.de.webdatabrowser.deep.rdf.resources;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;

import fu.berlin.de.webdatabrowser.deep.rdf.DeebResource;
import fu.berlin.de.webdatabrowser.deep.vocabulary.Deeb;

public class Found extends DeebResource {

    private String foundWhere;
    private Date   foundWhen;

    @Override
    protected DeebPropertyType getPropertyType() {
        return PROPERTY_TYPE;
    }

    public String getFoundWhere() {
        return foundWhere;
    }

    public void setFoundWhere(String foundWhere) {
        this.foundWhere = foundWhere;
        if(getResource() != null) {
            getResource().removeAll(Deeb.FoundWhere);
            getResource().addProperty(Deeb.FoundWhere, foundWhere);
        }
    }

    public Date getFoundWhen() {
        return foundWhen;
    }

    public void setFoundWhen(Date foundWhen) {
        this.foundWhen = foundWhen;
        if(getResource() != null) {
            getResource().removeAll(Deeb.FoundWhen);
            getResource().addProperty(Deeb.FoundWhen, DateFormat.getDateTimeInstance().format(foundWhen));
        }
    }

    private static final DeebPropertyType PROPERTY_TYPE = DeebPropertyType.FOUND;

    public Found(String identifier) {
        super(identifier);
    }

    @Override
    public List<Statement> getStatements() {
        return null;
    }

    @Override
    public DeebResource fromStatements(List<Statement> typeStatements, Resource resource) {
        return null;
    }

    @Override
    protected DeebResource fromResource(Resource resource) {
        setResource(resource);

        if(resource.getProperty(Deeb.FoundWhere) != null) {
            this.foundWhere = resource.getProperty(Deeb.FoundWhere).getString();
        }
        if(resource.getProperty(Deeb.FoundWhen) != null) {
            try {
                this.foundWhen = DateFormat.getDateTimeInstance().parse(resource.getProperty(Deeb.FoundWhen).getString());
            }
            catch(ParseException e) {
            }
        }

        return this;
    }

    @Override
    public void saveInModel(Model model) {
        super.saveInModel(model);

        if(getFoundWhere() != null) {
            getResource().addProperty(Deeb.FoundWhere, getFoundWhere());
        }
        if(getFoundWhen() != null) {
            getResource().addProperty(Deeb.FoundWhen, DateFormat.getDateTimeInstance().format(getFoundWhen()));
        }

    }

    @Override
    public String getHtml() {
        return null;
    }

}
