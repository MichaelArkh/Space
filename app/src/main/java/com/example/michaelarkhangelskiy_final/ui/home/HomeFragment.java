package com.example.michaelarkhangelskiy_final.ui.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.michaelarkhangelskiy_final.DataManager;
import com.example.michaelarkhangelskiy_final.R;

/**
 * The class for all the news objects
 */
public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private RecyclerView rv;
    private NewsItemAdapter ia;
    private View root;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ia.setNewItems(DataManager.loadNews(root.getContext()));
            ia.notifyDataSetChanged();
        }
    };

    /**
     * Creates the view for the fragment
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return the view
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        this.root = root;
        registerBroadcast(root);
        loadView(root);
        return root;
    }

    /**
     * Loads the view the layout
     * @param root the root view
     */
    private void loadView(View root){
        ia = new NewsItemAdapter(DataManager.loadNews(root.getContext()), root.getContext());
        rv = root.findViewById(R.id.news_recycle);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(root.getContext());
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(ia);
    }

    /**
     * Registers broadcasts for the root view
     * @param root the root view
     */
    private void registerBroadcast(View root){
        LocalBroadcastManager.getInstance(root.getContext()).registerReceiver(receiver,
                new IntentFilter("News-Finished"));
    }
}
