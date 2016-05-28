package com.v4ivstudio.whistler;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    List<Whistle> whistles;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    SharedPreferences sharedPref;
    boolean nsfw_enabled;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        nsfw_enabled = sharedPref.getBoolean("nsfw_switch", true);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        whistles = new ArrayList<>();

        if (isConnected()) {
            Toast.makeText(getBaseContext(), "Welcome", Toast.LENGTH_SHORT).show();
            mSwipeRefreshLayout.setRefreshing(true);
            new RetrieveWhistleTask().execute();
        } else {
            Toast.makeText(getBaseContext(), "Error : Connection Unavailable", Toast.LENGTH_LONG).show();
            mSwipeRefreshLayout.setRefreshing(false);
        }

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new RetrieveWhistleTask().execute();
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent confessIntent = new Intent(view.getContext(), NewWhistleActivity.class);
                startActivity(confessIntent);
            }
        });
    }

    public boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(settingsActivity);
            return true;
        }
        if (id == R.id.action_refresh) {
            new RetrieveWhistleTask().execute();
            mSwipeRefreshLayout.setRefreshing(true);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class RetrieveWhistleTask extends AsyncTask<Void, Void, String> {
        private InputStreamReader inputStreamReader;

        protected void onPreExecute() {
        }

        protected String doInBackground(Void... urls) {

            try {
                URL url = new URL("http://whistle.pythonanywhere.com/api/whistle.json");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    inputStreamReader = new InputStreamReader(urlConnection.getInputStream());
                    return CommonUtils.convertInputStreamReaderToString(inputStreamReader);
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if (response == null) {
                Toast.makeText(MainActivity.this, "Unknown Error", Toast.LENGTH_SHORT).show();
                mSwipeRefreshLayout.setRefreshing(false);
            } else {

                try {
                    JSONArray data = new JSONArray(response);
                    whistles.clear();
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject jsonObject = data.getJSONObject(i);
                        if (!nsfw_enabled) {
                            if (jsonObject.getBoolean("nsfw")) {
                                continue;
                            } else {
                                Whistle whistles = new Whistle(
                                        jsonObject.getInt("id"),
                                        jsonObject.getString("whistler"),
                                        jsonObject.getString("statement"),
                                        jsonObject.getString("location"),
                                        jsonObject.getString("category"),
                                        jsonObject.getBoolean("nsfw"),
                                        jsonObject.getString("published_date"));
                                MainActivity.this.whistles.add(whistles);
                            }
                        } else {
                            Whistle whistles = new Whistle(
                                    jsonObject.getInt("id"),
                                    jsonObject.getString("whistler"),
                                    jsonObject.getString("statement"),
                                    jsonObject.getString("location"),
                                    jsonObject.getString("category"),
                                    jsonObject.getBoolean("nsfw"),
                                    jsonObject.getString("published_date"));
                            MainActivity.this.whistles.add(whistles);
                        }
                    }
                    RecyclerView.Adapter mAdapter = new WhistleAdapter(whistles);
                    mRecyclerView.setAdapter(mAdapter);
                    mSwipeRefreshLayout.setRefreshing(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }
    // TODO Infinite Recycler View Pagination
    // TODO Recycler View OnClick
    // TODO Nearby Popular Fresh and Categories Tabs
    // TODO T&C and Privacy
}