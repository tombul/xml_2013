package fu.berlin.de.webdatabrowser.ui;

import android.app.Activity;
import android.os.Bundle;
import fu.berlin.de.webdatabrowser.R;

public class WebDataBrowserActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webdatabrowser);
    }
}
