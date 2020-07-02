package com.example.michaelarkhangelskiy_final.ui.notifications;

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

import com.example.michaelarkhangelskiy_final.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.concurrent.ExecutionException;

public class NotificationsFragment extends Fragment implements OnMapReadyCallback {

    private NotificationsViewModel notificationsViewModel;
    MapView mapView;
    GoogleMap map;
    LinearLayout astro;
    Button refresh;
    Marker current = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        setView(root);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        MapsInitializer.initialize(root.getContext());

        return root;
    }

    public void setView(final View root){
        final Resources res = getResources();
        TextView info = root.findViewById(R.id.iss_people);
        info.setText(res.getString(R.string.people, NotificationsViewModel.information.getPeople().length));
        refresh = root.findViewById(R.id.iss_refresh_button);
        mapView = root.findViewById(R.id.mapView);
        astro = root.findViewById(R.id.iss_astro);
        String[] people = NotificationsViewModel.information.getPeople();
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
//                try {
//                    //new RefreshViews.ISSTask().execute().get();
//                } catch (ExecutionException e) {
//                    e.printStackTrace();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                setView(root);
                mapView.postInvalidate();
                onMapReady(map);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.moveCamera(CameraUpdateFactory.newLatLng(NotificationsViewModel.information.getLocation()));


        BitmapDrawable bd= (BitmapDrawable) getResources().getDrawable(R.drawable.sputnik, null).getCurrent();
        Bitmap ok = bd.getBitmap();
        Bitmap ok2 = Bitmap.createScaledBitmap(ok, 80,80, false);
        if(current != null){
            current.remove();
        }
        current = map.addMarker(new MarkerOptions().position(NotificationsViewModel.information.getLocation()).title("ISS Location").icon(BitmapDescriptorFactory.fromBitmap(ok2)));

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
