package com.swaroop.navigation;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class HomesData extends AsyncTask<String, String, String> {
    //ProgressDialog pdLoading = new ProgressDialog(GetHomes.this);
    HttpURLConnection conn;
    private TaskCompleted mCallback;
    URL url = null;
    public final static ArrayList<String> rec = new ArrayList<String>();
    public final static ArrayList<String> lats = new ArrayList<String>();
    public final static ArrayList<String> longs = new ArrayList<String>();

    //this method will interact with UI, here display loading message
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        //pdLoading.setMessage("\tLoading...");
        //pdLoading.setCancelable(false);
        //pdLoading.show();

    }

    // This method does not interact with UI, You need to pass result to onPostExecute to display
    @Override
    protected String doInBackground(String... params) {
        try {
            // Enter URL address where your php file resides
            url = new URL("http://qav2.cs.odu.edu/swaroop/Hack/getHomes.php");

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return e.toString();
        }
        try {

            // Setup HttpURLConnection class to send and receive data from php
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");


            // setDoInput and setDoOutput method depict handling of both send and receive
            conn.setDoInput(true);
            conn.setDoOutput(true);

            // Append parameters to URL
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("latitude", params[0])
                    .appendQueryParameter("longitude", params[1]);
            String query = builder.build().getEncodedQuery();

            // Open connection for sending data
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();

        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return e1.toString();
        }

        try {

            int response_code = conn.getResponseCode();

            // Check if successful connection made
            if (response_code == HttpURLConnection.HTTP_OK) {

                // Read data sent from server
                InputStream input = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                // Pass data to onPostExecute method
                return (result.toString());

            } else {

                return ("unsuccessful");
            }

        } catch (IOException e) {
            e.printStackTrace();
            return e.toString();
        } finally {
            conn.disconnect();
        }


    }

    // this method will interact with UI, display result sent from doInBackground method
    @Override
    protected void onPostExecute(String result) {

        //pdLoading.dismiss();
        Log.i("result", String.valueOf(result));
        try {
            JSONObject reader = new JSONObject(result);
            JSONArray houses = reader.getJSONArray("data");
            lats.clear();
            longs.clear();
            for(int i =0;i<houses.length();i++){
                JSONObject c = houses.getJSONObject(i);
                lats.add(c.getString("House_lat"));
                longs.add(c.getString("House_long"));
                rec.add(c.getString("Rec"));
            }
//            mCallback.onTaskComplete(lats);
            Log.i("result", lats.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
