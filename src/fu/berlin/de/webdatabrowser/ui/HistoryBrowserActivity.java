package fu.berlin.de.webdatabrowser.ui;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import fu.berlin.de.webdatabrowser.R;
import fu.berlin.de.webdatabrowser.deep.rdf.DeebResource;
import fu.berlin.de.webdatabrowser.deep.rdf.RdfStore;
import fu.berlin.de.webdatabrowser.deep.rdf.resources.Person;
import fu.berlin.de.webdatabrowser.ui.widgets.MenuItem;

public class HistoryBrowserActivity extends Activity {

    private RdfStore rdfStore;
    private int      i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historybrowser);
        ((MenuItem) findViewById(R.id.historybrowser_menuitem_tohistorybrowser)).setHighlighted(true);
        rdfStore = RdfStore.getInstance();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void loadStore(View v) {
        rdfStore.loadStore();
    }

    public void exampleStore(View v) {
        Person testPerson = new Person("http://Example.org/deeb/JCPien" + i);
        testPerson.setGivenName("JC" + i);
        testPerson.setLastName("Pien");
        i++;

        rdfStore.addResource(testPerson);
    }

    public void deleteStore(View v) {
        rdfStore.cleanStore();
    }

    public void performQuery(View v) {
        System.out.println("Perform Query");
        List<DeebResource> results = rdfStore.performQuery("SELECT ?x WHERE { ?x  <http://www.w3.org/2001/vcard-rdf/3.0#NAME> ?y }", "x");
        String result = "";
        for(DeebResource resource : results) {
            Person person = (Person) resource;
            result += person.getGivenName() + " " + person.getLastName() + ";";
        }
        ((TextView) findViewById(R.id.textView1)).setText(result);
    }

    public void saveStore(View v) {
        rdfStore.saveStore();
    }

    public void toHistoryBrowser(View view) {
        startActivity(new Intent(this, HistoryBrowserActivity.class));
        finish();
    }

    public void toWebBrowser(View view) {
        startActivity(new Intent(this, WebBrowserActivity.class));
        finish();
    }

    public void toWebDataBrowser(View view) {
        startActivity(new Intent(this, WebDataBrowserActivity.class));
        finish();
    }
}
