package fu.berlin.de.webdatabrowser.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;

public class HttpRequestAsyncTask extends AsyncTask<String, Void, String> {
    private static final String       LOG_TAG = "HttpRequest";
    private final HttpResponseHandler responseHandler;

    public HttpRequestAsyncTask(HttpResponseHandler responseHandler) {
        super();
        this.responseHandler = responseHandler;
    }

    @Override
    protected String doInBackground(String... params) {
        if(params.length == 0)
            return null;

        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(params[0]);
        String html = "";

        try {
            HttpResponse response = client.execute(request);
            InputStream inputStream = response.getEntity().getContent();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(32);
            byte[] buffer = new byte[32];
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

    @Override
    protected void onPostExecute(String result) {
        responseHandler.onHttpResultAvailable(result);
    }
}
