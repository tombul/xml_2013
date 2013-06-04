package fu.berlin.de.webdatabrowser.ui;

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
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import fu.berlin.de.webdatabrowser.R;
import fu.berlin.de.webdatabrowser.ui.widgets.MenuItem;

public class WebBrowserActivity extends Activity {
    private static final String HOME_URL = "http://hasthelargehadroncolliderdestroyedtheworldyet.com";

    private WebView             webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webbrowser);
        webView = (WebView) findViewById(R.id.webbrowser_webview);

        // Prevent the default handler from starting a new activity on every
        // link
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });

        ((MenuItem) findViewById(R.id.webbrowser_menuitem_towebbrowser)).setHighlighted(true);
        final EditText urlBar = (EditText) findViewById(R.id.webbrowser_controls_addressbar);
        urlBar.setText(HOME_URL);

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

        loadUrl(null);
    }

    public void loadUrl(View view) {
        webView.loadUrl(((EditText) findViewById(R.id.webbrowser_controls_addressbar)).getText().toString());
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
