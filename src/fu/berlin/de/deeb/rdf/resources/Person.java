package fu.berlin.de.deeb.rdf.resources;

import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.vocabulary.VCARD;

import fu.berlin.de.deeb.rdf.DeebResource;

/**
 * Beispielhafte Implementierung einer {@link DeebResource} am Beispiel einer Person.
 * @author Jan-Christopher Pien
 * 27.05.2013
 *
 */
public class Person extends DeebResource {
	
	private String givenName;
	private String lastName;

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
		getResource().removeAll(VCARD.Given);
		getResource().addProperty(VCARD.Given, givenName);
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
		getResource().removeAll(VCARD.NAME);
		getResource().addProperty(VCARD.NAME, lastName);
	}

	@Override
	public DeebResource fromStatements(List<Statement> typeStatements,
			Resource resource) {
		Person result = new Person();

		return result;
	}

	@Override
	public List<Statement> getStatements() {
		Statement givenStatement = getResource().getProperty(VCARD.Given);
		Statement lastStatement = getResource().getProperty(VCARD.NAME);
		List<Statement> statementList = new ArrayList<Statement>();
		statementList.add(givenStatement);
		statementList.add(lastStatement);
		return statementList;
	}

	@Override
	public DeebResource fromResource(Resource resource) {
		setResource(resource);
		Person person = new Person();
		
		String givenName = resource.getProperty(VCARD.Given).getString();
		String lastName = resource.getProperty(VCARD.NAME).getString();
		
		setGivenName(givenName);
		setLastName(lastName);
		
		return person;
	}
	
}
