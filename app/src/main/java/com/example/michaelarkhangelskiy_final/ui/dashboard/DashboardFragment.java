package com.example.michaelarkhangelskiy_final.ui.dashboard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.michaelarkhangelskiy_final.DataManager;
import com.example.michaelarkhangelskiy_final.R;
import com.example.michaelarkhangelskiy_final.ui.home.HomeViewModel;
import com.example.michaelarkhangelskiy_final.ui.home.NewsItemAdapter;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private RecyclerView rv;
    private RocketItemAdapter ia;
    private View root;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ia.setNewItems(DataManager.loadRocket(root.getContext()));
            ia.notifyDataSetChanged();
        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        registerBroadcast(root);
        this.root = root;
        loadView(root);
        return root;
    }

    private void loadView(View root){
        ia = new RocketItemAdapter(DataManager.loadRocket(root.getContext()), root.getContext());
        rv = root.findViewById(R.id.rocket_recycle);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(root.getContext());
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(ia);
    }

    private void registerBroadcast(View root){
        LocalBroadcastManager.getInstance(root.getContext()).registerReceiver(receiver,
                new IntentFilter("Rocket-Finished"));
    }
}
