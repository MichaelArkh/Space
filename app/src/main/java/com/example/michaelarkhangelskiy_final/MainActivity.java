package com.example.michaelarkhangelskiy_final;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
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
import com.example.michaelarkhangelskiy_final.ui.saved.SavedViewModel;
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
    boolean internet;
    public static DataManager dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.isInternetAvailable();
        dm = new DataManager(this);
        if(internet) {
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
        } else {
            //Generate Blank
            Toast.makeText(this, "No internet connection found", Toast.LENGTH_LONG).show();
        }
        createNotificationChannel();
        SavedViewModel.loadFromDB(this);
        SavedViewModel.notifier(this);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("saved", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
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
