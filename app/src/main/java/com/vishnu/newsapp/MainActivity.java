package com.vishnu.newsapp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vishnu.newsapp.Adapter.HomeAdapter;
import com.vishnu.newsapp.Model.Articles;
import com.vishnu.newsapp.Model.Source;
import com.vishnu.newsapp.Utils.HttpHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private ArrayList<Articles> articlesList;
    ProgressDialog pd;
    private RecyclerView.Adapter homeAdapter;

    ProgressBar progressBar;
    RecyclerView recyclerView;
    private static String url = "https://candidate-test-data-moengage.s3.amazonaws.com/Android/news-api-feed/staticResponse.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.home_recyclerview);

        homeAdapter = new HomeAdapter(MainActivity.this, articlesList);
        recyclerView.setAdapter(homeAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        Log.e(TAG,"Starting API CALL");
        new GetNews().execute();

    }
    private class GetNews extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray articlesResponse = jsonObj.getJSONArray("articles");

                    // looping through All Contacts
                    for (int i = 0; i < articlesResponse.length(); i++) {

                        String id,name,author,title,description,url,imageUrl,publishedAt,content;

                        JSONObject articleObject = articlesResponse.getJSONObject(i);
                        JSONObject sourceObject = articleObject.getJSONObject("source");

                        Log.e(TAG, "Articles: " + articleObject.getString("title"));
                        Log.e(TAG, "Source: " + sourceObject.getString("id"));

                        if (sourceObject.getString("id") == null ){
                             id = "Not Available";
                        } else{
                            id = sourceObject.getString("id");
                        }

                        if (sourceObject.getString("name") == null ){
                            name = "Not Available";
                        } else{
                            name = sourceObject.getString("name");
                        }

                        if (articleObject.getString("author") == null ){
                            author = "Not Available";
                        } else{
                            author = articleObject.getString("author");
                        }

                        if (articleObject.getString("title") == null ){
                            title = "Not Available";
                        } else{
                            title = articleObject.getString("title");
                        }

                        if (articleObject.getString("description") == null ){
                            description = "Not Available";
                        } else{
                            description = articleObject.getString("description");
                        }

                        if (articleObject.getString("url") == null ){
                            url = "Not Available";
                        } else{
                            url = articleObject.getString("url");
                        }

                        if (articleObject.getString("imageUrl") == null ){
                            imageUrl = "Not Available";
                        } else{
                            imageUrl = articleObject.getString("imageUrl");
                        }

                        if (articleObject.getString("publishedAt") == null ){
                            publishedAt = "Not Available";
                        } else{
                            publishedAt = articleObject.getString("publishedAt");
                        }

                        if (articleObject.getString("content") == null ){
                            content = "Not Available";
                        } else{
                            content = articleObject.getString("content");
                        }

                        Source source = new Source(id,name);

                        Articles articles = new Articles(publishedAt,author,imageUrl,description,source,title,url,content);


                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
//            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

         homeAdapter.notifyDataSetChanged();
        }

    }
}