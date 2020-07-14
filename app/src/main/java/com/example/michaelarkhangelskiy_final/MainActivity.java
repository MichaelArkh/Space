package com.example.michaelarkhangelskiy_final;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.michaelarkhangelskiy_final.ui.saved.SavedViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.net.InetAddress;

public class MainActivity extends AppCompatActivity {
    boolean internet;
    public static DataManager dm;

    /**
     * Sets up the general layout for the app.
     *
     * @param savedInstanceState
     */
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
    }

    /**
     * Sends a push notification when the user leaves the app.
     */
    @Override
    protected void onPause() {
        super.onPause();
        SavedViewModel.loadFromDB(this);
        SavedViewModel.notifier(this);
    }

    /**
     * Required method to set up push notifications.
     */
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

    /**
     * Checks if internet is available and would not load the app without it.
     */
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
