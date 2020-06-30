package com.example.michaelarkhangelskiy_final;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.michaelarkhangelskiy_final.ui.dashboard.DashboardViewModel;
import com.example.michaelarkhangelskiy_final.ui.home.HomeViewModel;
import com.example.michaelarkhangelskiy_final.ui.notifications.NotificationsViewModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RefreshViews {
    static public class ISSTask extends AsyncTask<String, Void, String[]> {

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
            NotificationsViewModel.locationResult = response;
            NotificationsViewModel.peopleResult = response2;
            try {
                NotificationsViewModel.setInformation();
            } catch (Exception e) {
                //Toast.makeText(a, "Internet Error", Toast.LENGTH_LONG);
            }
            return new String[]{response, response2};
        }
    }

    static class RocketTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String response = null;
            try {
                URL url = new URL("https://launchlibrary.net/1.4/launch/next/10");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(connection.getInputStream()));
                response = reader.readLine();
            } catch (IOException e) { }

            return response;
        }
        @Override
        protected void onPostExecute(String result) {
            DashboardViewModel.items = result;
            try {
                DashboardViewModel.getNewsItems();
            } catch (Exception e) {
                //Toast.makeText(a, "Internet Error", Toast.LENGTH_LONG);
            }
        }
    }

    static class NewsTask extends AsyncTask<String, Void, String> {
        MainActivity a;

        public NewsTask(MainActivity a) {
            this.a = a;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected String doInBackground(String... strings) {
            String response = null;
            try {
                String key = a.getResources().getString(R.string.news_api);
                Date currentTime = Calendar.getInstance().getTime();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                String date = format1.format(currentTime);

                URL url = new URL("https://newsapi.org/v2/everything?q=nasa&language=en&from=" + date +"&sortBy=popularity&apiKey=" + key);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(connection.getInputStream()));
                response = reader.readLine();
            } catch (IOException e) { }

            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            HomeViewModel.items = result;
            try {
                HomeViewModel.getNewsItems();
            } catch (Exception e) {

            }
        }
    }
}
