package com.example.michaelarkhangelskiy_final.ui.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.michaelarkhangelskiy_final.DataManager;
import com.example.michaelarkhangelskiy_final.MainActivity;
import com.example.michaelarkhangelskiy_final.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class NotificationsFragment extends Fragment implements OnMapReadyCallback {

    private NotificationsViewModel notificationsViewModel;
    private MapView mapView;
    private GoogleMap map;
    private LinearLayout astro;
    private Button refresh;
    private View root;
    private Marker current = null;
    private ISS now;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            now = DataManager.loadISS(root.getContext());
            setView(root);
            onMapReady(map);
        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        now = DataManager.loadISS(root.getContext());
        setView(root);
        this.root = root;
        registerBroadcast(root);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        MapsInitializer.initialize(root.getContext());

        return root;
    }

    private void registerBroadcast(View root){
        LocalBroadcastManager.getInstance(root.getContext()).registerReceiver(receiver,
                new IntentFilter("ISS-Finished"));
    }

    public void setView(final View root){
        final Resources res = root.getResources();
        TextView info = root.findViewById(R.id.iss_people);
        info.setText(res.getString(R.string.people, now.getPeople().length));
        refresh = root.findViewById(R.id.iss_refresh_button);
        mapView = root.findViewById(R.id.mapView);
        astro = root.findViewById(R.id.iss_astro);
        String[] people = now.getPeople();
        for(int i = 0; i < people.length; i++){
            TextView astronaut = new TextView(root.getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0,5,0,5);
            astronaut.setGravity(Gravity.CENTER_HORIZONTAL);
            astronaut.setLayoutParams(params);
            astronaut.setText(people[i]);
            astro.addView(astronaut);
        }
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.dm.generateISS();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.moveCamera(CameraUpdateFactory.newLatLng(now.getLocation()));


        BitmapDrawable bd= (BitmapDrawable) root.getResources().getDrawable(R.drawable.sputnik, null).getCurrent();
        Bitmap ok = bd.getBitmap();
        Bitmap ok2 = Bitmap.createScaledBitmap(ok, 80,80, false);
        if(current != null){
            current.remove();
        }
        current = map.addMarker(new MarkerOptions().position(now.getLocation()).title("ISS Location").icon(BitmapDescriptorFactory.fromBitmap(ok2)));

    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
