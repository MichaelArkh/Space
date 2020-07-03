package com.example.michaelarkhangelskiy_final;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.michaelarkhangelskiy_final.ui.dashboard.RocketItem;
import com.example.michaelarkhangelskiy_final.ui.home.NewsItem;
import com.example.michaelarkhangelskiy_final.ui.notifications.ISS;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DataManager {

    static public final String newsLocation = "news.json";
    static public final String rocketLocation = "rocket.json";
    static public final String ISSLocation = "ISS.json";
    private Context c;

    public DataManager(Context c){
        this.c = c;
        //Using GSON to store and load files
        try {
            //throw new IOException();
            InputStream is = new FileInputStream(new File(c.getFilesDir(), ISSLocation));
            //Need to load files here, stored as json RocketItem and NewsItem

        } catch (IOException e) {
            //Need to generate files here
            generateFiles();
        }
    }

    public void generateFiles(){
        Toast.makeText(c, "Currently loading data from online", Toast.LENGTH_SHORT).show();
        new ISSTask().execute();
        new RocketTask().execute();
        new NewsTask(c).execute();
    }

    public void generateISS() {
        new ISSTask().execute();
    }

    static public List<NewsItem> loadNews(Context c){
        try {
            Gson gson = new Gson();
            return Arrays.asList(gson.fromJson(new BufferedReader(new FileReader(new File(c.getFilesDir(), newsLocation))), NewsItem[].class));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return new ArrayList<NewsItem>();
    }

    static public List<RocketItem> loadRocket(Context c){
        try {
            Gson gson = new Gson();
            return Arrays.asList(gson.fromJson(new BufferedReader(new FileReader(new File(c.getFilesDir(), rocketLocation))), RocketItem[].class));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return new ArrayList<RocketItem>();
    }

    static public ISS loadISS(Context c){
        try {
            Gson gson = new Gson();
            return gson.fromJson(new BufferedReader(new FileReader(new File(c.getFilesDir(), ISSLocation))), ISS.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return new ISS(new String[0], new LatLng(0,0));
    }

    class ISSTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... strings) {
            String response = null;
            String response2 = null;
            try {
                URL url = new URL("http://api.open-notify.org/iss-now.json");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                response = reader.readLine();
                url = new URL("http://api.open-notify.org/astros.json");
                HttpURLConnection connection2 = (HttpURLConnection) url.openConnection();
                BufferedReader reader2 = new BufferedReader(new InputStreamReader(connection2.getInputStream()));
                response2 = reader2.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new String[]{response, response2};
        }

        @Override
        protected void onPostExecute(String[] result) {
            ISS save = null;
            try {
                JSONArray peoplea = new JSONObject(result[1]).getJSONArray("people");
                String[] people = new String[peoplea.length()];
                for (int i = 0; i < peoplea.length(); i++) {
                    people[i] = peoplea.getJSONObject(i).getString("name");
                }
                JSONObject location = new JSONObject(result[0]).getJSONObject("iss_position");
                double lat = location.getDouble("latitude");
                double lng = location.getDouble("longitude");
                LatLng pos = new LatLng(lat, lng);
                save = new ISS(people, pos);
                Gson g = new Gson();
                Writer writer = new FileWriter(new File(c.getFilesDir(), ISSLocation));
                g.toJson(save, writer);
                writer.flush();
                writer.close();
            } catch (Exception e){
                return;
            }
            Intent intent = new Intent("ISS-Finished");
            LocalBroadcastManager.getInstance(c).sendBroadcast(intent);
        }
    }


    class RocketTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String response = null;
            try {
                URL url = new URL("https://launchlibrary.net/1.4/launch/next/50");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(connection.getInputStream()));
                response = reader.readLine();
            } catch (IOException e) { }

            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                ArrayList<RocketItem> ret = new ArrayList<RocketItem>();
                JSONObject values = new JSONObject(result);

                JSONArray main = values.getJSONArray("launches");
                int count = values.getInt("count");
                for (int i = 0; i < count; i++) {
                    JSONObject one = main.getJSONObject(i);
                    //Location Data
                    JSONObject locationdata = one.getJSONObject("location");
                    String location = locationdata.getString("name");
                    double lat = locationdata.getJSONArray("pads").getJSONObject(0).getDouble("latitude");
                    double lng = locationdata.getJSONArray("pads").getJSONObject(0).getDouble("longitude");
                    LatLng latLng = new LatLng(lat, lng);

                    //Rocket Data
                    JSONObject rocketdata = one.getJSONObject("rocket");
                    String rocketName = rocketdata.getString("name");
                    String image = rocketdata.getString("imageURL");

                    //Summary
                    JSONArray missiondata = one.getJSONArray("missions");
                    String summary = null;
                    String name = rocketName;
                    String redirectUrl = "";
                    if (missiondata.length() > 0) {
                        JSONObject summarydata = missiondata.getJSONObject(0);
                        summary = summarydata.getString("description");
                        name = summarydata.getString("name");
                        redirectUrl = summarydata.getString("wikiURL");
                    }
                    if (redirectUrl.equals("")) {
                        redirectUrl = rocketdata.getString("wikiURL");
                    }

                    //Date
                    DateFormat format = new SimpleDateFormat("MMMMM dd, yyyy HH:mm:ss z", Locale.ENGLISH);
                    Date date = format.parse(one.getString("windowstart"));


                    ret.add(new RocketItem(date, name, summary, image, location, latLng, rocketName, redirectUrl));
                }
                Gson g = new Gson();
                Writer write = new FileWriter(new File(c.getFilesDir(), rocketLocation));
                g.toJson(ret, write);
                write.flush();
                write.close();
            } catch (Exception e) { }
            Intent intent = new Intent("Rocket-Finished");
            LocalBroadcastManager.getInstance(c).sendBroadcast(intent);
        }
    }

    class NewsTask extends AsyncTask<String, Void, String> {
        Context a;

        public NewsTask(Context a) {
            this.a = a;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String response = null;
            try {
                String key = a.getResources().getString(R.string.news_api);
                Date currentTime = Calendar.getInstance().getTime();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                String date = format1.format(currentTime);
                String search = a.getSharedPreferences("searchPref", Context.MODE_PRIVATE).getString("searchPref", "nasa");
                //search = "nasa";
                URL url = new URL("https://newsapi.org/v2/everything?q=" + search + "&language=en&from=" + date +"&sortBy=popularity&apiKey=" + key);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(connection.getInputStream()));
                response = reader.readLine();
            } catch (IOException e) { }

            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                ArrayList<NewsItem> ret = new ArrayList<NewsItem>();
                JSONObject values = new JSONObject(result);
                JSONArray main = values.getJSONArray("articles");
                int maxCount = values.getInt("totalResults");
                if(maxCount > 10) {
                    maxCount = 10;
                }
                for (int i = 0; i < maxCount; i++) {
                    JSONObject one = main.getJSONObject(i);
                    String author = one.getString("author");
                    String title = one.getString("title");
                    String description = one.getString("description");
                    String url = one.getString("url");
                    String imgURL = one.getString("urlToImage");
                    Date date = new DateTime(one.getString("publishedAt")).toDate();
                    ret.add(new NewsItem(title, author, date, description, imgURL, url, false));
                }
                Gson g = new Gson();
                Writer write = new FileWriter(new File(c.getFilesDir(), newsLocation));
                g.toJson(ret, write);
                write.flush();
                write.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Intent intent = new Intent("News-Finished");
            LocalBroadcastManager.getInstance(c).sendBroadcast(intent);
        }
    }
}
