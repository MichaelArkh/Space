package com.example.michaelarkhangelskiy_final.ui.saved;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.michaelarkhangelskiy_final.R;
import com.example.michaelarkhangelskiy_final.database.SavedItemDataSource;

/**
 * The fragment for the saved objects
 */
public class SavedFragment extends Fragment {

    private SavedViewModel savedViewModel;
    private RecyclerView rv;
    static SavedItemAdapter ia;
    static SavedItemDataSource ds;

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
        savedViewModel = new ViewModelProvider(this).get(SavedViewModel.class);
        View root = inflater.inflate(R.layout.fragment_saved, container, false);
        loadView(root);
        return root;
    }

    /**
     * Loads the root view for the object
     * @param root the root view
     */
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
