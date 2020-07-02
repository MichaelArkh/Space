package com.example.michaelarkhangelskiy_final;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.michaelarkhangelskiy_final.ui.dashboard.DashboardViewModel;
import com.example.michaelarkhangelskiy_final.ui.dashboard.RocketItem;
import com.example.michaelarkhangelskiy_final.ui.home.HomeViewModel;
import com.example.michaelarkhangelskiy_final.ui.home.NewsItem;
import com.example.michaelarkhangelskiy_final.ui.notifications.ISS;
import com.example.michaelarkhangelskiy_final.ui.notifications.NotificationsViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private int numTasks = 3;
    boolean internet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.isInternetAvailable();
        if(internet) {
            new NewsTask(this).execute();
            new RocketTask().execute();
            new ISSTask().execute();
        } else {
            //Generate Blank
            Toast.makeText(this, "No internet connection found", Toast.LENGTH_LONG).show();
            HomeViewModel.itemList = new ArrayList<NewsItem>();
            DashboardViewModel.itemList = new ArrayList<RocketItem>();
            NotificationsViewModel.information = new ISS();
        }
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
            NotificationsViewModel.locationResult = result[0];
            NotificationsViewModel.peopleResult = result[1];
            try {
                NotificationsViewModel.setInformation();
            } catch (Exception e) {
                //Toast.makeText(a, "Internet Error", Toast.LENGTH_LONG);
            }
            removeTask();
            finishedTask();
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
            DashboardViewModel.items = result;
            try {
                DashboardViewModel.getNewsItems();
            } catch (Exception e) {
                //Toast.makeText(a, "Internet Error", Toast.LENGTH_LONG);
                e.printStackTrace();
            }
            removeTask();
            finishedTask();
        }
    }

    class NewsTask extends AsyncTask<String, Void, String> {
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
                Toast.makeText(a, "Internet Error", Toast.LENGTH_LONG);
            }
            removeTask();
            finishedTask();
        }
    }

    private void removeTask(){
        numTasks--;
    }

    private void finishedTask(){
        if(numTasks == 0){
            setContentView(R.layout.activity_main);
            BottomNavigationView navView = findViewById(R.id.nav_view);
            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
            AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications, R.id.navigation_saved, R.id.navigation_settings)
                    .build();
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
            NavigationUI.setupWithNavController(navView, navController);
        }
    }

    public void isInternetAvailable() {
        Thread a = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InetAddress ipAddr = InetAddress.getByName("google.com");
                    //You can replace it with your name
                    internet =  !ipAddr.equals("");

                } catch (Exception e) {
                    e.printStackTrace();
                    internet = false;
                }
            }
        });
        a.start();
        try {
            a.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
