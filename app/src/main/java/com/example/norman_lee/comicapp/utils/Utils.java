package com.example.norman_lee.comicapp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class Utils {

    public static final String UTILS_TAG = "UtilsTag";

    /**
     * This method establishes an HTTP connection from a URL object
     * @param url a URL object
     * @return an InputStream object
     */
    public static InputStream getInputStream(URL url){

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        InputStream inputStream = null;

        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.setUseCaches(false);
            Log.i("getInputStream", "Connecting...");
            urlConnection.connect();
            Log.i("getInputStream", "Connected");
            inputStream = urlConnection.getInputStream();
        }catch(IOException e) {
            e.printStackTrace();
            inputStream = null;
        }

        return inputStream;

    }

    /**
     * This is a wrapper method to obtain a JSON string from a URL object
     * This is used to query an API for a JSON Response
     * @param url a URL object
     * @return a String containing JSON
     */
    public static String getJson(URL url){
        return convertStreamToString(getInputStream(url));
    }

    /**
     * Read the String that is returned from an InputStream created by an HTTP connection
     * @param inputStream an InputStream object
     * @return a String
     */
    public static String convertStreamToString(InputStream inputStream){

        BufferedReader reader = null;
        String outString;

        StringBuffer buffer = new StringBuffer();
        if (inputStream == null) {
            // Nothing to do.
            return null;
        }
        reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;

        try{
            while ((line = reader.readLine()) != null) {
        /* Since it's JSON, adding a newline isn't necessary (it won't affect
        parsing) but it does make debugging a *lot* easier if you print out the
        completed buffer for debugging. */
            buffer.append(line + "\n");
            }

        } catch( IOException e){
            e.printStackTrace();

        }
        if (buffer.length() == 0) {
            // Stream was empty.  No point in parsing.
            return null;
        }
        outString = buffer.toString();
        Log.i(UTILS_TAG,outString);
        return outString;

    }

    /**
     * This method checks if an Activity has a network connection
     * @param  context a Context object (Context is the superclass of AppCompatActivity
     * @return a boolean object
     */
    public static boolean isNetworkAvailable(Context context) {

        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean haveNetwork = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        Log.i(UTILS_TAG, "Active Network: " + haveNetwork);
        return haveNetwork;
    }

    /**
     * This method gets a Bitmap from an InputStream HTTP connection
     * @param  inputStream object
     * @return a Bitmap
     */
    public static Bitmap convertStreamToBitmap (InputStream inputStream){

        return BitmapFactory.decodeStream(inputStream);

    }

    /**
     * This method gets a Bitmap image from a URL object
     * @param  url a URL object
     * @return a Bitmap object
     */
    public static Bitmap getBitmap(URL url){
        return convertStreamToBitmap(getInputStream(url));
    }

    /**
     * This method builds a URL object to call the xkcd API from the comic No
     * @param  comicNo a string containing a comic Number
     * @return a URL object
     */
    public static URL buildURL(String comicNo){
        String scheme = "https";
        final String authority = "xkcd.com";
        final String back = "info.0.json";
        Uri.Builder builder = new Uri.Builder();
        URL url = null;

        Log.i(UTILS_TAG, "before append");

        if( comicNo.equals("")){
            builder.scheme(scheme)
                    .authority(authority)
                    .appendPath(back);
        } else{
            builder.scheme(scheme)
                    .authority(authority)
                    .appendPath(comicNo+"/"+back);
        }

        Log.i(UTILS_TAG, "before build URL");
        Uri uri = builder.build();

        try{
            url = new URL(uri.toString());
            Log.i(UTILS_TAG,"URL OK: " + url.toString());
        }catch(MalformedURLException e) {
            Log.i(UTILS_TAG, "malformed URL: " + url.toString());
        }

        return url;
    }

}
