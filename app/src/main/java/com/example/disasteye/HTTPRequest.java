package com.example.disasteye;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import javax.net.ssl.HttpsURLConnection;

class HTTPRequest extends AsyncTask<String, Integer, String> {
    //AsyncTask: https://developer.android.com/reference/android/os/AsyncTask
    //<args, progress, result> needs 4 things to happen: onPreExecute(), doInBackground(param), onProgressUpdate(), and onPostExecute()
    private static HttpsURLConnection connection;

    //public void onPreExecute(){    }
    public String doInBackground(String... args){
        Log.d("HERE!!!", "Beginning of doInBackground");
        //ETHANS IMPLEMENTATION
//        try{
//            //Create link, open connection
//            URL url = new URL(args[0]);
//            HttpURLConnection urlConnection = (HttpURLConnection)  url.openConnection();
//
//            //Waiting on data in the background:
//            InputStream replied = urlConnection.getInputStream();
//
//            //Parsing the JSON we receive...
//            //1. Read data
//
//            //2. Convert to JSON object
//
//        }
//        catch(Exception e){
//            Log.e(null, "doInBackground error occurred: ", e);
//        }

        // READING IN DATA FROM API
        BufferedReader reader;
        String line;
        StringBuffer responseContent = new StringBuffer();

        try
        {
            ////////////////////////////////////////////////////////////////////////////////////////
            // ESTABLISHING CONNECTION BETWEEN API
            URL url = new URL("https://eonet.gsfc.nasa.gov/api/v3/events/geojson?category=wildfires");
            connection = (HttpsURLConnection) url.openConnection();

            // Setting up connection
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            connection.setRequestProperty("Accept", "application/json");
            connection.connect();

            int status = connection.getResponseCode();
            //If connection something is wrong with the connection...
            if (status > 299)
            {
                Log.d("HERE!!!", "CONNECTION IS FAULTY");
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                while ((line = reader.readLine()) != null)
                {
                    responseContent.append(line);
                }
                reader.close();
            }
            //If connection is established...
            else
            {
                //Read data and append to responseContent string
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line = reader.readLine()) != null)
                {
                    responseContent.append(line);
                }
                reader.close();
            }
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            connection.disconnect();
        }

        try {
            parse(responseContent.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Function parses data from the response of the API, converts data into JSONArray OR JSONObject
    public static String parse(String responseBody) throws JSONException {
        // Grabs everthing within {}
        JSONObject obj = new JSONObject(responseBody);
        // Grabs features array "features: [...]"
        JSONArray features = obj.getJSONArray("features");

        for (int i = 0; i < features.length(); i++)
        {
            // Grabs properties object for each iteration in features array "properties: {...}"
            JSONObject properties = features.getJSONObject(i).getJSONObject("properties");
            // Grabs title string for event: "title: 'TITLE'"
            String title = properties.getString("title");

            // Grabs geometry object for each iteration in features array "geometry: {...}"
            JSONObject geometry = features.getJSONObject(i).getJSONObject("geometry");
            // Grabs array given by coordinate attribute "coordinates: [...,...]"
            JSONArray coords = geometry.getJSONArray("coordinates");

            System.out.println(title + " " + coords);
        }
        return null;
    }

    protected void onProgressUpdate(Integer... progress){
    }
    public void onPostExecute(String fromDoInBackground){
    }
}
