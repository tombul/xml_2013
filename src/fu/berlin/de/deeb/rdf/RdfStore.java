package fu.berlin.de.deeb.rdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import android.content.Context;
import android.os.Environment;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

/**
 * Implementierung eines RDFStores
 * @author Jan-Christopher Pien
 * 27.05.2013
 *
 */
public class RdfStore implements DeebRdfStore {
	
	private static RdfStore instance;
	
	private Model rdfModel;
	
	private final String XML_NAME = "rdfStore.xml";
	private final String BASE_URI = "http://Example.org/Deeb";
	
	public static synchronized RdfStore getInstance() {
		return (instance == null ? new RdfStore() : instance);
	}

	public RdfStore() {
		rdfModel = ModelFactory.createDefaultModel();
	}

	@Override
	public void addResource(DeebResource resource) {
		rdfModel.begin();
		rdfModel.add(resource.getStatements());
		rdfModel.commit();
	}

	@Override
	public List<DeebResource> performQuery(String queryString, String... params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean loadStore(Context context) {
		String state = Environment.getExternalStorageState();
		
		if ((Environment.MEDIA_MOUNTED.equals(state)) || (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state))) {
		    File history = new File(context.getExternalFilesDir(null), XML_NAME);
		    if (history.exists()) {
				try {
					InputStream historyStream = new FileInputStream(history);
					rdfModel.read(historyStream, null);
					historyStream.close();
				} catch (IOException e) {
					return false;
				}
		    }
		    return false;
		} else {
		    return false;
		}
	}

	@Override
	public boolean saveStore(Context context) {
		String state = Environment.getExternalStorageState();
		
		if ((Environment.MEDIA_MOUNTED.equals(state)) || (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state))) {
		    File history = new File(context.getExternalFilesDir(null), XML_NAME);
		    if (history.exists()) {
				history.delete();
		    }
			try {
				OutputStream saveStream = new FileOutputStream(history);
				rdfModel.write(saveStream);
				saveStream.close();
				return true;
			} catch (IOException e) {
				return false;
			}
		} else {
		    return false;
		}
	}
	
}
