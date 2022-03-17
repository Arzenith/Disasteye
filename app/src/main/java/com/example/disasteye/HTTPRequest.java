package com.example.disasteye;

import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

class HTTPRequest extends AsyncTask<String, Integer, String> {
    //AsyncTask: https://developer.android.com/reference/android/os/AsyncTask
    //<args, progress, result> needs 4 things to happen: onPreExecute(), doInBackground(param), onProgressUpdate(), and onPostExecute()

    //public void onPreExecute(){    }
    public String doInBackground(String... args){
        try{
            //Create link, open connection
            URL url = new URL(args[0]);
            HttpURLConnection urlConnection = (HttpURLConnection)  url.openConnection();

            //Waiting on data in the background:
            InputStream replied = urlConnection.getInputStream();

            //Parsing the JSON we receive...
            //1. Read data

            //2. Convert to JSON object

        }
        catch(Exception e){
            Log.e(null, "doInBackground error occurred: ", e);
        }
        return "testing" + "123" + "abc";
    }
    protected void onProgressUpdate(Integer... progress){
    }
    public void onPostExecute(String fromDoInBackground){
    }
}
