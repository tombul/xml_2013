package fu.berlin.de.webdatabrowser.ui;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.hp.hpl.jena.rdf.model.Literal;

import fu.berlin.de.webdatabrowser.R;
import fu.berlin.de.webdatabrowser.deep.rdf.DeebResource;
import fu.berlin.de.webdatabrowser.deep.rdf.RdfStore;
import fu.berlin.de.webdatabrowser.ui.widgets.MenuItem;

public class HistoryBrowserActivity extends Activity {
    protected static final String[] PRESET_QUERIES             = new String[] {
                                                               "SELECT ?subject ?predicate ?object WHERE { ?subject ?predicate ?object }",
                                                               "SELECT DISTINCT ?object WHERE { ?subject <http://Schema.org/author> ?object }",
                                                               "SELECT DISTINCT ?object WHERE { ?subject <http://purl.org/dc/elements/1.1/subject> ?object }" };
    private static final String[]   PRESET_QUERIES_DESCRIPTION = new String[] {
                                                               "everything (file)",
                                                               "authors of something",
                                                               "all book subjects" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historybrowser);
        ((MenuItem) findViewById(R.id.historybrowser_menuitem_tohistorybrowser)).setHighlighted(true);
        final RdfStore rdfStore = RdfStore.getInstance();
        final WebView webView = (WebView) findViewById(R.id.historybrowser_webview);
        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, PRESET_QUERIES_DESCRIPTION);
        final Spinner spinner = (Spinner) findViewById(R.id.historybrowser_spinner);
        final ProgressDialog p = new ProgressDialog(this);
        p.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        p.setMessage("processing ...");
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            int lastSelected = 0;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                if(lastSelected == position)
                    return;

                lastSelected = position;
                p.show();

                if(position == 0) {
                    new AsyncTask<String, Void, Boolean>() {

                        @Override
                        protected Boolean doInBackground(String... params) {
                            boolean result = rdfStore.writeQueryFormattedResultToFile(PRESET_QUERIES[0], "rdfstore_dump.txt");
                            p.dismiss();
                            return result;
                        }
                    }.execute();

                    return;
                }

                new AsyncTask<String, Void, Void>() {

                    @Override
                    protected Void doInBackground(String... params) {
                        List<Object> resources = rdfStore.performQuery(PRESET_QUERIES[position]);
                        String source = "<!DOCTYPE html><html>";

                        if(resources.isEmpty()) {
                            source += "Nothing useful found.";
                        }
                        else if(resources.get(0) != null && !(resources.get(0) instanceof Literal)) {
                            source += ((DeebResource) resources.get(0)).getHeaderHtml();
                        }

                        for(Object resource : resources)
                            if(resource instanceof Literal)
                                source += ((Literal) resource).getLexicalForm() + "<p/>";
                            else
                                source += ((DeebResource) resource).getHtml() + "<p/>";

                        source += "</html>";
                        p.dismiss();
                        webView.loadDataWithBaseURL(null, source, "text/html", "UTF-8", null);
                        return null;
                    }
                }.execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void toHistoryBrowser(View view) {
    }

    public void toWebBrowser(View view) {
        startActivity(new Intent(this, WebBrowserActivity.class));
        finish();
    }
}
