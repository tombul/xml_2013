package fu.berlin.de.webdatabrowser.deep.rdf.resources;

import java.util.List;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;

import fu.berlin.de.webdatabrowser.deep.rdf.DeebResource;
import fu.berlin.de.webdatabrowser.deep.vocabulary.DBPediaOwl;
import fu.berlin.de.webdatabrowser.deep.vocabulary.Pos;

public class City extends DeebResource {

    private String                        name;
    private double                        populationTotal;
    private Person                        leader;
    private Location                      location;
    private double                        areaTotal;
    private List<Person>                  birthPlace;
    private List<Person>                  hometown;

    private static final DeebPropertyType PROPERTY_TYPE = DeebPropertyType.CITY;

    @Override
    protected DeebPropertyType getPropertyType() {
        return PROPERTY_TYPE;
    }

    public double getPopulationTotal() {
        return populationTotal;
    }

    public void setPopulationTotal(double populationTotal) {
        this.populationTotal = populationTotal;
        if(getResource() != null) {
            getResource().removeAll(DBPediaOwl.populationTotal);
            getResource().addLiteral(DBPediaOwl.populationTotal, populationTotal);
        }
    }

    public Person getLeader() {
        return leader;
    }

    public void setLeader(Person leader) {
        this.leader = leader;
        if(getResource() != null) {
            getResource().removeAll(DBPediaOwl.leader);
            if(leader.getResource() == null)
                leader.saveInModel(getResource().getModel());
            getResource().addLiteral(DBPediaOwl.leader, leader.getResource());
        }
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
        if(getResource() != null) {
            getResource().removeAll(Pos.location);
            if(location.getResource() == null)
                location.saveInModel(getResource().getModel());
            getResource().addLiteral(Pos.location, location.getResource());
        }
    }

    public double getAreaTotal() {
        return areaTotal;
    }

    public void setAreaTotal(double areaTotal) {
        this.areaTotal = areaTotal;
        if(getResource() != null) {
            getResource().removeAll(DBPediaOwl.areaTotal);
            getResource().addLiteral(DBPediaOwl.areaTotal, areaTotal);
        }
    }

    public List<Person> getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(List<Person> birthPlace) {
        this.birthPlace = birthPlace;
        if(getResource() != null) {
            getResource().removeAll(DBPediaOwl.birthPlace);
            for(Person person : birthPlace) {
                if(person.getResource() == null)
                    person.saveInModel(getResource().getModel());
                getResource().addProperty(DBPediaOwl.birthPlace, person.getResource());
            }

        }
    }

    public List<Person> getHometown() {
        return hometown;
    }

    public void setHometown(List<Person> hometown) {
        this.hometown = hometown;
        if(getResource() != null) {
            getResource().removeAll(DBPediaOwl.hometown);
            for(Person person : hometown) {
                if(person.getResource() == null)
                    person.saveInModel(getResource().getModel());
                getResource().addProperty(DBPediaOwl.hometown, person.getResource());
            }
        }
    }

    public City(String identifier) {
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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void saveInModel(Model model) {
        super.saveInModel(model);

        if(getPopulationTotal() != 0)
            getResource().addLiteral(DBPediaOwl.populationTotal, getPopulationTotal());
        if(getLeader() != null) {
            if(getLeader().getResource() == null)
                getLeader().saveInModel(model);
            getResource().addProperty(DBPediaOwl.leader, getLeader().getResource());
        }
        if(getLocation() != null) {
            if(getLocation().getResource() == null)
                getLocation().saveInModel(model);
            getResource().addProperty(Pos.location, getLocation().getResource());
        }
        if(getAreaTotal() != 0)
            getResource().addLiteral(DBPediaOwl.areaTotal, getAreaTotal());
        if(getBirthPlace() != null) {
            for(Person person : getBirthPlace()) {
                if(person.getResource() == null)
                    person.saveInModel(model);
                getResource().addProperty(DBPediaOwl.birthPlace, person.getResource());
            }
        }
        if(getHometown() != null) {
            for(Person person : getHometown()) {
                if(person.getResource() == null)
                    person.saveInModel(model);
                getResource().addProperty(DBPediaOwl.hometown, person.getResource());
            }
        }

    }

    @Override
    public String getHtml() {
        // Table BEGIN
        String html = "<table style=\"width:100%px; font-size:1.3em;" +
                "border-collapse:collapse;\">";

        // Title
        html += "<tr><td style=\"text-align:" +
                "center; background-color:#FF9; padding:2px 0 2px 0; " +
                "font-size:1.7em\"><strong>";
        html += name;
        html += "</strong></td></tr>";

        // Bürgermeister
        html += "<tr><td style=\"background-color:#FFC; padding:4px;" +
                "word-break:break-all; word-wrap:break-word;\"><strong>" +
                "Bürgermeister:</strong><br />";
        if(leader != null)
            html += leader.getGivenName() + " " + leader.getLastName();
        html += "</td></tr>";
        // Einwohnerzahl
        html += "<tr><td style=\"background-color:#FFC; padding:4px;" +
                "word-break:break-all; word-wrap:break-word;\"><strong>" +
                "Einwohnerzahl:</strong><br />";
        html += String.valueOf(populationTotal);
        html += "</td></tr>";
        // Gesamtfläche
        html += "<tr><td style=\"background-color:#FFC; padding:4px;" +
                "word-break:break-all; word-wrap:break-word;\"><strong>" +
                "Gesamtfläche:</strong><br />";
        html += String.valueOf(areaTotal);
        html += "</td></tr>";
        // Standort
        html += location.getHtml();
        // Geburtsort von
        html += "<tr><td style=\"background-color:#FFC; padding:4px;" +
                "word-break:break-all; word-wrap:break-word;\"><strong>" +
                "Geburtsort von:</strong><br />";
        html += "</td></tr>";
        int i = 0;
        for(Person person : birthPlace) {
            html += "<tr><td style=\"background-color:#FCC; padding:4px;" +
                    "word-break:break-all; word-wrap:break-word;\">" + person.getHtml() + "</td></tr>";
            i++;
            if(i > 50)
                break;
        }

        // Heimatort von
        html += "<tr><td style=\"background-color:#FCC; padding:4px;" +
                "word-break:break-all; word-wrap:break-word;\"><strong>" +
                "Heimatstadt von:</strong><br />";
        html += "</td></tr>";
        i = 0;
        for(Person person : hometown) {
            html += "<tr><td style=\"background-color:#FCC; padding:4px;" +
                    "word-break:break-all; word-wrap:break-word;\">" + person.getHtml() + "</td></tr>";
            i++;
            if(i > 50)
                break;
        }
        // Description
        // html += "<tr><td style=\"background-color:#FFC; padding:4px;" +
        // "word-break:break-all; word-wrap:break-word;\"><strong>" +
        // "Beschreibung:</strong><br />";
        // html += description;
        // html += "</td></tr>";
        //
        // // Country
        // html += "<tr><td style=\"background-color:#FAF7BA; padding:4px" +
        // ";\"><strong>Land: </strong>";
        // html += country;
        // html += "</td></tr>";
        //
        // // Language
        // html += "<tr><td style=\"padding:4px; background-color:#FFC;\"" +
        // "><strong>Sprache: </strong>";
        // html += language;
        // html += "</td></tr>";
        //
        //

        // Table END
        html += "</table>";
        return html;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
