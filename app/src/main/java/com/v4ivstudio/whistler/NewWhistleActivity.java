package com.v4ivstudio.whistler;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class NewWhistleActivity extends AppCompatActivity {

    String whistler, title, statement, category, location, jsonString;
    private EditText detailEditText;
    private CheckBox nsfwCheck;
    String[] type = {"CNF", "PSN", "FAM", "SCL", "WRK", "SEX", "MIL", "FUD", "SPT"};
    boolean nsfw;
    Spinner spinner;
    String display_name;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_whistle);

        detailEditText = (EditText) findViewById(R.id.etDetails);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        display_name = sharedPref.getString("display_name", "anonymous");
        nsfwCheck = (CheckBox) findViewById(R.id.checkBox);
        spinner = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.pref_category, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_whistle, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_whistle) {
            whistler = display_name;
            statement = detailEditText.getText().toString();
            location = "";
            category = type[spinner.getSelectedItemPosition()];
            nsfw = nsfwCheck.isChecked();
            if (statement.equals("") || statement != null || statement.equals(" ")) {
                if (isConnected()) {
                    new SendWhistleTask().execute();

                } else {
                    Toast.makeText(getBaseContext(), "Error : Connection Unavailable!", Toast.LENGTH_SHORT).show();
                }
            } else if (title.equals("")) {
                Toast.makeText(getBaseContext(), "Error : Statement is a Required Field", Toast.LENGTH_LONG).show();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class SendWhistleTask extends AsyncTask<Void, Void, String> {

        HttpURLConnection httpURLConnection = null;
        String result;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Whistle whistle = new Whistle(whistler, statement, location, category, nsfw);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("whistler", whistle.whistler);
                jsonObject.accumulate("statement", whistle.statement);
                jsonObject.accumulate("location", whistle.location);
                jsonObject.accumulate("category", whistle.category);
                jsonObject.accumulate("nsfw", whistle.nsfw);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonString = jsonObject.toString();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                URL url = new URL("http://whistle.pythonanywhere.com/api/whistle/");
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setChunkedStreamingMode(0);
                httpURLConnection.setRequestMethod("POST");

                OutputStream out = new BufferedOutputStream(httpURLConnection.getOutputStream());
                out.write(jsonString.getBytes("UTF-8"));
                out.flush();
                out.close();

                int httpResult = httpURLConnection.getResponseCode();
                // read the response
                InputStream in = new BufferedInputStream(httpURLConnection.getInputStream());
                result = in.toString();
                //JSONObject jsonObject = new JSONObject(result);
                in.close();

                return "Response: " + httpResult;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } finally {
                httpURLConnection.disconnect();
            }
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            //int objectId = 0;
            if (response == null) {
                Toast.makeText(getBaseContext(), "Unknown Error", Toast.LENGTH_LONG).show();
            } else {
                /*JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    objectId = jsonObject.getInt("id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
                finish();

            }
        }
    }

    public boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
