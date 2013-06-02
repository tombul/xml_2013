package fu.berlin.de.webdatabrowser.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import fu.berlin.de.webdatabrowser.R;
import fu.berlin.de.webdatabrowser.ui.widgets.MenuItem;

public class WebBrowserActivity extends Activity {
    private static final String HOME_URL = "http://hasthelargehadroncolliderdestroyedtheworldyet.com";

    private WebView             webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webbrowser);
        ((MenuItem) findViewById(R.id.webbrowser_menuitem_towebbrowser)).setHighlighted(true);
        webView = (WebView) findViewById(R.id.webbrowser_webview);
        webView.loadUrl(HOME_URL);

        // Prevent the default handler from starting a new activity on every
        // link
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
    }

    public void toHistoryBrowser(View view) {
        startActivity(new Intent(this, HistoryBrowserActivity.class));
        finish();
    }

    public void toWebBrowser(View view) {
    }

    public void toWebDataBrowser(View view) {
        startActivity(new Intent(this, WebDataBrowserActivity.class));
        finish();
    }
}
