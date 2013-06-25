package fu.berlin.de.webdatabrowser.deep.rdf.resources;

import java.util.List;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;

import fu.berlin.de.webdatabrowser.deep.rdf.DeebResource;

public class City extends DeebResource {

    private double       populationTotal = 0;
    private Person       leader          = null;
    private Location     location        = null;
    private double       areaTotal       = 0;
    private List<Person> birthPlace      = null;
    private List<Person> hometown        = null;

    public double getPopulationTotal() {
        return populationTotal;
    }

    public void setPopulationTotal(double populationTotal) {
        this.populationTotal = populationTotal;
    }

    public Person getLeader() {
        return leader;
    }

    public void setLeader(Person leader) {
        this.leader = leader;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public double getAreaTotal() {
        return areaTotal;
    }

    public void setAreaTotal(double areaTotal) {
        this.areaTotal = areaTotal;
    }

    public List<Person> getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(List<Person> birthPlace) {
        this.birthPlace = birthPlace;
    }

    public List<Person> getHometown() {
        return hometown;
    }

    public void setHometown(List<Person> hometown) {
        this.hometown = hometown;
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
        // TODO Auto-generated method stub

    }

    @Override
    public String getHtml() {
        // TODO Auto-generated method stub
        return null;
    }

}
