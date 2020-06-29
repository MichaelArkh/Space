package com.example.michaelarkhangelskiy_final.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.michaelarkhangelskiy_final.R;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private RecyclerView rv;
    private NewsItemAdapter ia;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        loadView(root);
        return root;
    }

    private void loadView(View root){
        ia = new NewsItemAdapter(HomeViewModel.itemList, root.getContext());
        rv = root.findViewById(R.id.news_recycle);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(root.getContext());
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(ia);
    }
}
