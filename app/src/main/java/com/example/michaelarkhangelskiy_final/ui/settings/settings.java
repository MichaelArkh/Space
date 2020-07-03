package com.example.michaelarkhangelskiy_final.ui.settings;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.michaelarkhangelskiy_final.MainActivity;
import com.example.michaelarkhangelskiy_final.R;
import com.example.michaelarkhangelskiy_final.ui.notifications.NotificationsViewModel;
import com.example.michaelarkhangelskiy_final.ui.saved.SavedViewModel;

import java.util.concurrent.ExecutionException;

public class settings extends Fragment {

    private SettingsViewModel settingsViewModel;
    private RadioGroup searchTerm;
    private RadioButton nasa;
    private RadioButton space;
    private RadioButton ISS;
    private RadioButton custom;
    private EditText input;
    private View root;




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        this.root = root;
        initView(root);
        registerListeners(root);
        setDefault(root);
        return root;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("test", root.getContext().getSharedPreferences("searchPref", Context.MODE_PRIVATE).getString("searchPref", "nasa"));
        if(custom.isChecked()){
            String custom1 = input.getText().toString();
            root.getContext().getSharedPreferences("searchPref", Context.MODE_PRIVATE).edit().putString("searchPref", custom1).apply();
        }
        MainActivity.dm.generateFiles();
    }

    private void initView(View root){
        nasa = root.findViewById(R.id.radioNasa);
        space = root.findViewById(R.id.radioSpace);
        ISS = root.findViewById(R.id.radioISS);
        custom = root.findViewById(R.id.radioCustom);
        input = root.findViewById(R.id.customInput);
        searchTerm = root.findViewById(R.id.searchTerm);
    }

    private void registerListeners(final View root){
        searchTerm.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(nasa.isChecked()){
                    custom.setChecked(false);
                    root.getContext().getSharedPreferences("searchPref", Context.MODE_PRIVATE).edit().putString("searchPref", "nasa").apply();
                } else if(space.isChecked()){
                    custom.setChecked(false);
                    root.getContext().getSharedPreferences("searchPref", Context.MODE_PRIVATE).edit().putString("searchPref", "space").apply();
                } else if (ISS.isChecked()){
                    custom.setChecked(false);
                    root.getContext().getSharedPreferences("searchPref", Context.MODE_PRIVATE).edit().putString("searchPref", "iss").apply();
                }
            }
        });
        custom.setOnCheckedChangeListener(new RadioButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    searchTerm.clearCheck();
                }
            }
        });
    }

    private void setDefault(final View root) {
        String searchPref = root.getContext().getSharedPreferences("searchPref", Context.MODE_PRIVATE).getString("searchPref", "nasa");
        if(searchPref.equals("nasa")){
            nasa.setChecked(true);
        } else if (searchPref.equals("space")){
            space.setChecked(true);
        } else if (searchPref.equals("iss")){
            ISS.setChecked(true);
        } else {
            custom.setChecked(true);
            input.setText(searchPref);
        }
    }

}
