package com.example.michaelarkhangelskiy_final.ui.saved;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.michaelarkhangelskiy_final.R;
import com.example.michaelarkhangelskiy_final.database.SavedItem;
import com.example.michaelarkhangelskiy_final.database.SavedItemDataSource;
import com.example.michaelarkhangelskiy_final.ui.home.NewsItem;
import com.example.michaelarkhangelskiy_final.ui.home.NewsItemAdapter;
import com.example.michaelarkhangelskiy_final.ui.notifications.NotificationsViewModel;
import com.example.michaelarkhangelskiy_final.ui.settings.SettingsViewModel;

public class SavedFragment extends Fragment {

    private SavedViewModel savedViewModel;
    private RecyclerView rv;
    static SavedItemAdapter ia;
    static SavedItemDataSource ds;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        savedViewModel = new ViewModelProvider(this).get(SavedViewModel.class);
        View root = inflater.inflate(R.layout.fragment_saved, container, false);
        loadView(root);
        return root;
    }

    private void loadView(View root){
        ds = new SavedItemDataSource(root.getContext());
        SavedViewModel.loadFromDB(root.getContext());
        ia = new SavedItemAdapter(SavedViewModel.itemList, root.getContext());
        Log.e("items", SavedViewModel.itemList.toString());
        rv = root.findViewById(R.id.saved_recycle);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(root.getContext());
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(ia);
        SavedViewModel.notifier(root.getContext());
    }


}
