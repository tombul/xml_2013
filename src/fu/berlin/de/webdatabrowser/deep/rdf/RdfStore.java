package fu.berlin.de.webdatabrowser.deep.rdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.os.Environment;
import android.util.Log;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.shared.JenaException;

import fu.berlin.de.webdatabrowser.deep.vocabulary.Deeb;

/**
 * Implementierung eines RDFStores
 * 
 * @author Jan-Christopher Pien
 * @since 27.05.2013
 * 
 * 
 */
public class RdfStore implements DeebRdfStore {
    private static final String LOG_TAG  = "RdfStore";
    private static RdfStore     instance;

    private Model               rdfModel;

    private static final String XML_NAME = "rdfStore.xml";

    public static synchronized RdfStore getInstance() {
        if(instance == null) {
            instance = new RdfStore();
            instance.loadStore();
        }

        return instance;
    }

    public RdfStore() {
        rdfModel = ModelFactory.createDefaultModel();
    }

    @Override
    public void addResource(DeebResource resource) {
        resource.saveInModel(rdfModel);
    }

    @Override
    public synchronized List<Object> performQuery(String queryString) {
        Query query = QueryFactory.create(queryString);
        QueryExecution execution = QueryExecutionFactory.create(query, rdfModel);
        List<Object> resources = new ArrayList<Object>();
        try {
            ResultSet results = execution.execSelect();
            for(; results.hasNext();) {
                QuerySolution solution = results.nextSolution();
                for(String param : results.getResultVars()) {
                    try {
                        Resource solResource = solution.getResource(param);
                        if(solResource.getProperty(Deeb.ResourceType) != null) {
                            DeebResource result = DeebResource.createResource(solResource);
                            resources.add(result);
                        }
                    }
                    catch(ClassCastException e) {
                        resources.add(solution.getLiteral(param));
                    }
                }
            }
        }
        finally {
            execution.close();
        }
        return resources;
    }

    public synchronized boolean writeQueryFormattedResultToFile(String queryString, String path) {
        try {
            FileOutputStream outputStream = new FileOutputStream(new File(
                    Environment.getExternalStorageDirectory().getPath() + "/" + path));
            ResultSet result = QueryExecutionFactory.create(QueryFactory.create(queryString), rdfModel).execSelect();
            ResultSetFormatter.out(outputStream, result);
            outputStream.close();
            return true;
        }
        catch(IOException e) {
            Log.w(LOG_TAG, Log.getStackTraceString(e));
            return false;
        }
    }

    @Override
    public boolean loadStore() {
        String state = Environment.getExternalStorageState();

        if((Environment.MEDIA_MOUNTED.equals(state)) || (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state))) {
            File history = new File(Environment.getExternalStorageDirectory() + "/fu_deeb", XML_NAME);
            if(history.exists()) {
                try {
                    InputStream historyStream = new FileInputStream(history);
                    rdfModel.read(historyStream, null);
                    historyStream.close();
                }
                catch(IOException e) {
                    return false;
                }
                catch(JenaException e) {
                    return false;
                }
            }
            return false;
        }
        else {
            return false;
        }
    }

    @Override
    public boolean saveStore() {
        String state = Environment.getExternalStorageState();

        if((Environment.MEDIA_MOUNTED.equals(state)) || (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state))) {
            File history = new File(Environment.getExternalStorageDirectory() + "/fu_deeb", XML_NAME);
            history.mkdirs();
            if(history.exists()) {
                history.delete();
            }
            try {
                OutputStream saveStream = new FileOutputStream(history);
                rdfModel.write(saveStream);
                saveStream.close();
                return true;
            }
            catch(IOException e) {
                return false;
            }
        }
        else {
            return false;
        }
    }

    public void dumpStore(OutputStream stream) {
        rdfModel.write(stream);
    }

    @Override
    public void cleanStore() {
        rdfModel = ModelFactory.createDefaultModel();
    }

    @Override
    public Model getModel() {
        return rdfModel;
    }

}
