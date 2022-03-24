package com.example.jsonparsingtodb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    ListView listView;
    Database database;
    Context act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = new Database(this);
        database.getWritableDatabase();
        act = this;

        textView = (TextView) findViewById(R.id.textView);
        listView = (ListView) findViewById(R.id.listView);
        getJSON("https://jsonplaceholder.typicode.com/users");
    }

    ///JSON WORK FROM HERE
    private void getJSON(final String urlWebService) {
        /*
         * As fetching the json string is a network operation
         * And we cannot perform a network operation in main thread
         * so we need an AsyncTask
         * The constrains defined here are
         * Void -> We are not passing anything
         * Void -> Nothing at progress update as well
         * String -> After completion it should return a string and it will be the json string
         * */

        class GetJSON extends AsyncTask<Void, Void, String> {

            //this method will be called before execution
            //you can display a progress bar or something
            //so that user can understand that he should wait
            //as network operation may take some time
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            //this method will be called after execution
            //so here we are displaying a toast with the json string
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();

                try {
                    loadIntoListView(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


            //in this method we are fetching the json string
            @Override
            protected String doInBackground(Void... voids) {
                try {

                    //creating a URL
                    URL url = new URL(urlWebService);

                    //Opening the URL using HttpUrlConnection
                    HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

                    //String builder object to read the string from the service
                    StringBuilder sb = new StringBuilder();

                    //Using buffered reader to read the string from service
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    //A simple string to read values from each line
                    String json;

                    //reading until we dont find null
                    while ((json = bufferedReader.readLine()) != null){
                        //appending it to a string builder
                        sb.append(json + "\n");
                    }

                    //finally returning the read string
                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }
            }
        }

        //creating async task object and executing it
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }

    //Method to load the data and display in a list view in the UI
    private void loadIntoListView(String json) throws JSONException{
        //creating a json array from the json string
        JSONArray jsonArray = new JSONArray(json);

        //creating a string array for listview
        String[] data = new String[jsonArray.length()];

        //looping through all the elements in json array
        for (int i = 0; i < jsonArray.length(); i++){
            //getting json object from json array
            JSONObject obj = jsonArray.getJSONObject(i);

            //getting the name from the json object and putting it inside string array
            data[i] = obj.getString("name");

        }


        //the array adapter to load the data into list
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.activity_main, R.id.textView, data);


        //attaching adapter to list view
        listView.setAdapter(arrayAdapter);
    }
}