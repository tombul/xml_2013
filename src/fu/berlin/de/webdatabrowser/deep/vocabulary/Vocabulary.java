package fu.berlin.de.webdatabrowser.deep.vocabulary;

import com.hp.hpl.jena.rdf.model.Model;

import fu.berlin.de.webdatabrowser.deep.rdf.RdfStore;

public abstract class Vocabulary {

    protected static final Model getModel() {
        return RdfStore.getInstance().getModel();
    }

}
