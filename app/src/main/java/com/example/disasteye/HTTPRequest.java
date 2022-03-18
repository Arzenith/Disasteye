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

import javax.net.ssl.HttpsURLConnection;

class HTTPRequest extends AsyncTask<String, Integer, String> {
    //AsyncTask: https://developer.android.com/reference/android/os/AsyncTask
    //<args, progress, result> needs 4 things to happen: onPreExecute(), doInBackground(param), onProgressUpdate(), and onPostExecute()
    HttpsURLConnection connection;

    //public void onPreExecute(){    }
    public String doInBackground(String... args){
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

        // CODE WORKS IN STANDALONE JAVA FILE - Patrick
//        try {
//            URL url = new URL("https://eonet.gsfc.nasa.gov/api/v3/categories/wildfires");
//            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
//
//            // Request setup
//            connection.setRequestMethod("GET");
//            connection.setConnectTimeout(5000);
//            connection.setReadTimeout(5000);
//            // Don't need api key??
//            //connection.setRequestProperty("x-apikey", "TpXnAgxHg3q8GMFPCd4AEagnwbI64SkaVMaNb3XV");
//            connection.connect();
//            int status = connection.getResponseCode();
//            switch (status) {
//                case 200:
//                case 201:
//                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//                    StringBuilder sb = new StringBuilder();
//                    String line;
//                    while ((line = br.readLine()) != null) {
//                        sb.append(line+"\n");
//                    }
//                    br.close();
//                    System.out.println(sb.toString());
//            }
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return "testing" + "123" + "abc";
    }
    protected void onProgressUpdate(Integer... progress){
    }
    public void onPostExecute(String fromDoInBackground){
    }
}
