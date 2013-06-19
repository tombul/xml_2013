package fu.berlin.de.webdatabrowser.deep.rdf.resources;

import java.util.List;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;

import fu.berlin.de.webdatabrowser.deep.rdf.DeebResource;

public class City extends DeebResource {

    private double   populationTotal = 0;
    private Person   leader          = null;
    private Location location        = null;
    private double   areaTotal       = 0;

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

}
