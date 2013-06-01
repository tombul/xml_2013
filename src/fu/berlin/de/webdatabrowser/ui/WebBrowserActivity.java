package fu.berlin.de.webdatabrowser.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import fu.berlin.de.webdatabrowser.R;
import fu.berlin.de.webdatabrowser.ui.widgets.MenuItem;

public class WebBrowserActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webbrowser);
        ((MenuItem) findViewById(R.id.webbrowser_menuitem_towebbrowser)).setHighlighted(true);
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