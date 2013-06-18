package fu.berlin.de.webdatabrowser.ui;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import fu.berlin.de.webdatabrowser.R;
import fu.berlin.de.webdatabrowser.deep.rdf.DeebResource;
import fu.berlin.de.webdatabrowser.deep.rdf.RdfStore;
import fu.berlin.de.webdatabrowser.ui.widgets.MenuItem;
import fu.berlin.de.webdatabrowser.util.HttpRequestAsyncTask;
import fu.berlin.de.webdatabrowser.util.HttpResponseHandler;
import fu.berlin.de.webdatabrowser.webdataparser.WebDataParser;

public class WebDataBrowserActivity extends Activity implements HttpResponseHandler {
    public static final String EXTRA_PASSED_URL = "webdatabrowser.passed_url";

    private WebView            webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webdatabrowser);
        ((MenuItem) findViewById(R.id.webdatabrowser_menuitem_towebdatabrowser)).setHighlighted(true);
        webView = (WebView) findViewById(R.id.webdatabrowser_webview);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Handle what to do with a link, e.g. refering our
                // RDF-model
                return false;
            }
        });

        new HttpRequestAsyncTask(this).execute(getIntent().getExtras().getString(EXTRA_PASSED_URL));
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
    }

    public void onParsingResultAvailable(List<DeebResource> resources) {
        String source = "";

        if(resources.isEmpty()) {
            source = "<!DOCTYPE html><html>Nothing useful found.</html>";
        }

        for(DeebResource resource : resources) {
            RdfStore.getInstance().addResource(resource);
        }

        // TODO Get HTML-visualization for the resultset

        webView.loadDataWithBaseURL(null, source, "text/html", "UTF-8", null);
    }

    @Override
    public void onHttpResultAvailable(String source) {
        new WebDataParser(this).parse(source, getIntent().getStringExtra(EXTRA_PASSED_URL));
    }
}
