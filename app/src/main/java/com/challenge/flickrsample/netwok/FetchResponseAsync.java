package com.challenge.flickrsample.netwok;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.challenge.flickrsample.utils.Constants;
import com.challenge.flickrsample.utils.JsonParser;

import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Class that handles background API calls
 */

public class FetchResponseAsync extends AsyncTask<String, Void, Object> {

    private ResponseListener mResponseListener;

    public FetchResponseAsync(ResponseListener responseListener) {
        mResponseListener = responseListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mResponseListener.onRequestStart();
    }

    @Override
    protected Object doInBackground(String... params) {

        HttpURLConnection urlConnection = null;

        try {
            URL url = new URL(params[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String response = readStream(in);

            if (!TextUtils.isEmpty(response)) {
                return JsonParser.parseJson(response);
            }

        } catch (MalformedURLException e) {
            return e;
        } catch (IOException e) {
            return e;
        } catch (JSONException e) {
            return e;
        } finally {
            if (null != urlConnection)
                urlConnection.disconnect();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Object object) {
        super.onPostExecute(object);
        if (object instanceof Exception) {
            mResponseListener.onError(HandleError((Exception) object));
        } else {
            mResponseListener.onResponse(object);
        }
    }

    /**
     * Returns response a string
     *
     * @param in input stream
     * @return response
     */
    private String readStream(InputStream in) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(in));
        StringBuilder total = new StringBuilder();
        String line;

        while ((line = r.readLine()) != null) {
            total.append(line).append('\n');
        }

        return total.toString();
    }

    /**
     * Handles error
     *
     * @param exception - exception
     * @return string message
     */
    private String HandleError(Exception exception) {

        if (exception instanceof MalformedURLException) {
            return Constants.URL_ERROR;
        } else if (exception instanceof IOException) {
            return Constants.ERROR;
        } else if (exception instanceof JSONException) {
            return Constants.JSON_ERROR;
        } else {
            return Constants.ERROR;
        }
    }
}
