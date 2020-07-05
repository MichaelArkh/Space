package com.example.michaelarkhangelskiy_final.ui.settings;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.michaelarkhangelskiy_final.MainActivity;
import com.example.michaelarkhangelskiy_final.R;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.Date;

public class settings extends Fragment {

    private SettingsViewModel settingsViewModel;
    private RadioGroup searchTerm;
    private RadioButton nasa;
    private RadioButton space;
    private RadioButton ISS;
    private RadioButton custom;
    private Button date;
    private EditText input, newsCount, rocketCount;
    private CheckBox check, notificatons;
    private TextView dateText;
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
        if(!newsCount.getText().toString().equals("")){
            root.getContext().getSharedPreferences("searchPref", Context.MODE_PRIVATE).edit().putInt("newsCount", Integer.parseInt(newsCount.getText().toString())).apply();
        }
        if(!rocketCount.getText().toString().equals("")) {
            root.getContext().getSharedPreferences("searchPref", Context.MODE_PRIVATE).edit().putInt("rocketCount", Integer.parseInt(rocketCount.getText().toString())).apply();
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
        date = root.findViewById(R.id.dateButton);
        dateText = root.findViewById(R.id.dateText);
        newsCount = root.findViewById(R.id.newsItemCount);
        rocketCount = root.findViewById(R.id.rocketItemCount);
        check = root.findViewById(R.id.showPrevious);
        notificatons = root.findViewById(R.id.notificationsEnabled);
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
        final FragmentManager fm = getParentFragmentManager();
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DateFragment();
                newFragment.setTargetFragment(settings.this, 0);
                newFragment.show(fm, "datePicker");
            }
        });
        check.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                root.getContext().getSharedPreferences("searchPref", Context.MODE_PRIVATE).edit().putBoolean("showBefore", isChecked).apply();
            }
        });
        notificatons.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                root.getContext().getSharedPreferences("searchPref", Context.MODE_PRIVATE).edit().putBoolean("notificationsEnabled", isChecked).apply();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check for the results
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            // get date from string
            Calendar c = (Calendar) data.getExtras().get("selectedCal");
            Date a = c.getTime();
            // set the value of the editText
            Gson g = new Gson();
            String text = g.toJson(a);
            root.getContext().getSharedPreferences("searchPref", Context.MODE_PRIVATE).edit().putString("startDate", text).apply();
            dateText.setText(a.toString());
        }
    }

    private void setDefault(final View root) {
        // RadioButtons
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
        //Date
        String def = root.getContext().getSharedPreferences("searchPref", Context.MODE_PRIVATE).getString("startDate", new Date().toString());
        if(root.getContext().getSharedPreferences("searchPref", Context.MODE_PRIVATE).contains("startDate")){
            String obj = root.getContext().getSharedPreferences("searchPref", Context.MODE_PRIVATE).getString("startDate", new Date().toString());
            Gson g = new Gson();
            Date a = g.fromJson(obj, Date.class);
            def = a.toString();
        }
        dateText.setText(def);
        //Item counts
        int newsCount = root.getContext().getSharedPreferences("searchPref", Context.MODE_PRIVATE).getInt("newsCount", 20);
        int rocketCount = root.getContext().getSharedPreferences("searchPref", Context.MODE_PRIVATE).getInt("rocketCount", 20);
        this.newsCount.setText(String.valueOf(newsCount));
        this.rocketCount.setText(String.valueOf(rocketCount));
        //Checkbox
        check.setChecked(root.getContext().getSharedPreferences("searchPref", Context.MODE_PRIVATE).getBoolean("showBefore", false));
        notificatons.setChecked(root.getContext().getSharedPreferences("searchPref", Context.MODE_PRIVATE).getBoolean("notificationsEnabled", true));
    }

}
