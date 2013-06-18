/**
 * 
 */
package fu.berlin.de.webdatabrowser.webdataparser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

/**
 * @author tom
 * 
 *         This class is for providing general purpose methods that might be
 *         used in different parsers.
 * 
 */
public class BasicParser {
    private static final String LOG_TAG = "BasicParser";

    private AsyncTask<String, Void, String> asyncRequest(String url, String agent) {
        return new AsyncTask<String, Void, String>() {

            @Override
            protected String doInBackground(String... params) {
                return getHttpResponseString(params[0], params[1]);
            }

        }.execute(url, agent);
    }

    private String getHttpResponseString(String url, String agent) {
        String defaultAgent = "Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30";
        // AndroidHttpClient client = null;
        // if(!(agent == null || agent.length() == 0)) {
        // client = AndroidHttpClient.newInstance(agent);
        // }
        // else {
        // client = AndroidHttpClient.newInstance(defaultAgent);
        // }
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);
        String html = "";

        try {
            HttpResponse response = client.execute(request);
            // client.close();
            InputStream inputStream = response.getEntity().getContent();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(16);
            byte[] buffer = new byte[16];
            int bytesRead = 0;

            while((bytesRead = inputStream.read(buffer, 0, buffer.length)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            html = outputStream.toString();
            Log.v("asynkBlaHtml", html);
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

    protected String getAsyncResults(String url, String agent) {
        AsyncTask<String, Void, String> task = asyncRequest(url, agent);
        String content;
        try {
            content = task.get();
            return content;
        }
        catch(InterruptedException e) {
            Log.e(LOG_TAG, Log.getStackTraceString(e));
        }
        catch(ExecutionException e) {
            Log.e(LOG_TAG, Log.getStackTraceString(e));
        }
        return null;
    }

    protected void writeToFile(String input, String fileName) {
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)) {
            Log.d("Test", "sdcard mounted and writable");
            File myFile = new File(Environment.getExternalStorageDirectory().getPath() + "/" + fileName);
            Log.d("test-path", Environment.getExternalStorageDirectory().getPath() + "/" + fileName);
            try {
                myFile.createNewFile();
                FileOutputStream fOut = new FileOutputStream(myFile);
                OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
                myOutWriter.append(input);
                myOutWriter.close();
                fOut.close();
                Log.d("Test-done", "writen without errors");
            }
            catch(IOException e) {
                Log.e("Test", Log.getStackTraceString(e));
            }
        }
        else if(Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            Log.d("Test", "sdcard mounted readonly");
        }
        else {
            Log.d("Test", "sdcard state: " + state);
        }
    }

}
