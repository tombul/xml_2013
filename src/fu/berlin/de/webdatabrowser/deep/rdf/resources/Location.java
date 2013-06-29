package fu.berlin.de.webdatabrowser.deep.rdf.resources;

import java.util.List;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;

import fu.berlin.de.webdatabrowser.deep.rdf.DeebResource;
import fu.berlin.de.webdatabrowser.deep.vocabulary.Deeb;
import fu.berlin.de.webdatabrowser.deep.vocabulary.Pos;

public class Location extends DeebResource {

    private double                        alt;
    private double                        lon;
    private double                        lat;

    private static final DeebPropertyType PROPERTY_TYPE = DeebPropertyType.LOCATION;

    public double getAlt() {
        return alt;
    }

    public void setAlt(double alt) {
        this.alt = alt;
        if(getResource() != null) {
            getResource().removeAll(Pos.alt);
            getResource().addLiteral(Pos.alt, alt);
        }
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
        if(getResource() != null) {
            getResource().removeAll(Pos.long_);
            getResource().addLiteral(Pos.long_, lon);
        }
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
        if(getResource() != null) {
            getResource().removeAll(Pos.lat);
            getResource().addLiteral(Pos.lat, lat);
        }
    }

    public Location(String identifier) {
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

        if(resource.getProperty(Pos.alt) != null)
            this.alt = resource.getProperty(Pos.alt).getDouble();
        if(resource.getProperty(Pos.lat) != null)
            this.lat = resource.getProperty(Pos.lat).getDouble();
        if(resource.getProperty(Pos.long_) != null)
            this.lon = resource.getProperty(Pos.long_).getDouble();

        return this;
    }

    @Override
    public void saveInModel(Model model) {
        Resource resource = model.createResource(getIdentifier());
        model.remove(resource.listProperties());
        setResource(resource);

        if(getLon() != 0)
            resource.addLiteral(Pos.long_, getLon());
        if(getLat() != 0)
            resource.addLiteral(Pos.lat, getLat());
        if(getAlt() != 0)
            resource.addLiteral(Pos.alt, getAlt());

        resource.addProperty(Deeb.ResourceType, PROPERTY_TYPE.toString());
    }

    @Override
    public String getHtml() {
        // TODO Auto-generated method stub

        String html = "<tr><td style=\"background-color:#FFC; padding:4px;" +
                "word-break:break-all; word-wrap:break-word;\"><strong>" +
                "Standort:</strong><br />";
        html += "lat: " + String.valueOf(lat) + " lon: " + String.valueOf(lon);
        html += "</td></tr>";

        return html;
    }

}
