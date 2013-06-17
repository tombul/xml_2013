package fu.berlin.de.webdatabrowser.ui;

import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import fu.berlin.de.webdatabrowser.R;
import fu.berlin.de.webdatabrowser.ui.widgets.MenuItem;

public class WebBrowserActivity extends Activity {
    private static String[] PRESET_URLS = { "http://www.google.de",
                                        // TODO change to satisfy your needs
                                        "http://www.europeana.eu",
                                             // TODO change to satisfy your
                                             // needs
                                             "http://stackoverflow.com",
                                             // TODO change to satisfy your
                                             // needs
                                             "http://dbpedia.org/data/Berlin.rdf",
                                             // TODO change to satisfy your
                                             // needs
                                             "http://www.openarchives.org" };

    protected EditText      urlBar;
    private WebView         webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webbrowser);
        webView = (WebView) findViewById(R.id.webbrowser_webview);

        // Prevent the default handler from starting a new activity on every
        // link and set text of the urlbar
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                urlBar.setText(url);
                return false;
            }
        });

        ((MenuItem) findViewById(R.id.webbrowser_menuitem_towebbrowser)).setHighlighted(true);
        urlBar = (EditText) findViewById(R.id.webbrowser_controls_addressbar);

        // Hide menu while soft-keyboard is visible
        urlBar.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    ((RelativeLayout) findViewById(R.id.webbrowser_menu)).setVisibility(View.GONE);
                else {
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).
                            hideSoftInputFromWindow(urlBar.getWindowToken(), 0);
                    ((RelativeLayout) findViewById(R.id.webbrowser_menu)).setVisibility(View.VISIBLE);
                }
            }
        });

        // Treat hitting 'done' or 'next' as hitting the 'loadUrl'-button
        urlBar.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch(actionId) {
                    case EditorInfo.IME_ACTION_DONE:
                    case EditorInfo.IME_ACTION_NEXT:
                        loadUrl(null);
                        return true;
                    default:
                        return false;
                }
            }
        });

        // HACK to avoid writing a full new adapter-class (due to lazynes), the
        // unwanted text of the spinner has been made invisible and button /
        // spinner have fixed dimensions
        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, PRESET_URLS);
        final Spinner spinner = (Spinner) findViewById(R.id.webbrowser_controls_spinner);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                urlBar.setText(spinnerAdapter.getItem(position));
                loadUrl(null);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        loadUrl(null);
    }

    public void loadUrl(View view) {
        String url = urlBar.getText().toString();

        if(!url.toUpperCase(Locale.getDefault()).startsWith("HTTP://")) {
            url = "http://" + url;
            urlBar.setText(url);
        }

        webView.loadUrl(url);
        webView.requestFocus();
    }

    public void toHistoryBrowser(View view) {
        startActivity(new Intent(this, HistoryBrowserActivity.class));
        finish();
    }

    public void toWebBrowser(View view) {
    }

    public void toWebDataBrowser(View view) {
        startActivity(new Intent(this, WebDataBrowserActivity.class).
                putExtra(WebDataBrowserActivity.EXTRA_PASSED_URL, webView.getUrl()));
        finish();
    }
}
