package fu.berlin.de.webdatabrowser.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import android.os.Environment;
import android.util.Log;

public class Debug {
    private static final String LOG_TAG = "Debug";

    public static void logLongString(byte[] stringBytes) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(stringBytes)));
        int lineNumber = 0;
        String line;

        try {
            while((line = reader.readLine()) != null)
                Log.d(LOG_TAG + " " + (++lineNumber), line);

            reader.close();
        }
        catch(IOException e) {
            Log.w(LOG_TAG, Log.getStackTraceString(e));
        }
    }

    public static void logLongString(String string) {
        try {
            logLongString(string.getBytes("UTF-8"));
        }
        catch(UnsupportedEncodingException e) {
            Log.w(LOG_TAG, Log.getStackTraceString(e));
        }
    }

    public static void writeFileToExternalStorage(byte[] bytes, String fileName) {
        String state = Environment.getExternalStorageState();

        if(state.equals(Environment.MEDIA_MOUNTED)) {
            File file = new File(Environment.getExternalStorageDirectory().getPath() + "/" + fileName);

            try {
                FileOutputStream outputStream = new FileOutputStream(file);
                outputStream.write(bytes);
                outputStream.close();
            }
            catch(IOException e) {
                Log.e(LOG_TAG, Log.getStackTraceString(e));
            }
        }
        else {
            Log.w(LOG_TAG, "invalid external storage state: " + state);
        }
    }

    public static void writeFileToExternalStorage(String content, String fileName) {
        try {
            writeFileToExternalStorage(content.getBytes("UTF-8"), fileName);
        }
        catch(UnsupportedEncodingException e) {
            Log.w(LOG_TAG, Log.getStackTraceString(e));
        }
    }
}
