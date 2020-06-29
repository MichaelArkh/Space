package com.example.michaelarkhangelskiy_final;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.michaelarkhangelskiy_final.ui.dashboard.DashboardViewModel;
import com.example.michaelarkhangelskiy_final.ui.home.HomeViewModel;
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
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private int numTasks = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new NewsTask(this).execute();
        new RocketTask().execute();
    }

    class RocketTask extends AsyncTask<String, Void, String> {

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
            Log.e("test", result);
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

    public void removeTask(){
        numTasks--;
    }
    public void finishedTask(){
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
}
