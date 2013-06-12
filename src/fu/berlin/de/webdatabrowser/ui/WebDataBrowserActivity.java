package fu.berlin.de.webdatabrowser.ui;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import fu.berlin.de.webdatabrowser.R;
import fu.berlin.de.webdatabrowser.deep.rdf.DeebResource;
import fu.berlin.de.webdatabrowser.deep.rdf.RdfStore;
import fu.berlin.de.webdatabrowser.ui.widgets.MenuItem;
import fu.berlin.de.webdatabrowser.webdataparser.WebDataParser;

public class WebDataBrowserActivity extends Activity {
    public static final String  EXTRA_PASSED_URL = "webdatabrowser.passed_url";
    private static final String LOG_TAG          = "WebDataBrowser";

    private WebView             webView;

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

        new AsyncTask<String, Void, String>() {

            @Override
            protected String doInBackground(String... params) {
                return getHttpResponseString(params[0]);
            }

            @Override
            protected void onPostExecute(String result) {
                onHttpRequestFinished(result);
            }
        }.execute(getIntent().getExtras().getString(EXTRA_PASSED_URL));
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

    protected void onHttpRequestFinished(String html) {
        List<DeebResource> resources = WebDataParser.parse(
                html, getIntent().getStringExtra(EXTRA_PASSED_URL), this);

        if(resources.isEmpty())
            html = "<!DOCTYPE html><html>Nothing useful found.</html>";

        for(DeebResource resource : resources)
            RdfStore.getInstance().addResource(resource);

        // TODO Get HTML-visualization for the resultset

        webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
    }

    protected String getHttpResponseString(String url) {
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);
        String html = "";

        try {
            HttpResponse response = client.execute(request);
            InputStream inputStream = response.getEntity().getContent();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(16);
            byte[] buffer = new byte[16];
            int bytesRead = 0;

            while((bytesRead = inputStream.read(buffer, 0, buffer.length)) != -1)
                outputStream.write(buffer, 0, bytesRead);

            html = outputStream.toString();
            inputStream.close();
            outputStream.close();
        }
        catch(ClientProtocolException e) {
            Log.e(LOG_TAG, Log.getStackTraceString(e));
        }
        catch(IOException e) {
            Log.e(LOG_TAG, Log.getStackTraceString(e));
        }

        return html;
    }
}
