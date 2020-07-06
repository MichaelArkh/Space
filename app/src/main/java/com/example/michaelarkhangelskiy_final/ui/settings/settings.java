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
import android.content.SharedPreferences;
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
import android.widget.Toast;

import com.example.michaelarkhangelskiy_final.MainActivity;
import com.example.michaelarkhangelskiy_final.R;
import com.example.michaelarkhangelskiy_final.database.SavedItem;
import com.example.michaelarkhangelskiy_final.database.SavedItemDataSource;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * The settings class that controls settings for the app
 */
public class settings extends Fragment {

    private SettingsViewModel settingsViewModel;
    private RadioGroup searchTerm;
    private RadioButton nasa;
    private RadioButton space;
    private RadioButton ISS;
    private RadioButton custom;
    private Button date, register, login, saveData, downloadData;
    private EditText input, newsCount, rocketCount;
    private CheckBox check, notificatons;
    private TextView dateText;
    private View root;

    /**
     * Inflates the view
     * @param inflater the inflator
     * @param container the container
     * @param savedInstanceState the savedInstanceState
     * @return the root view
     */
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

    /**
     * Lifecycle method that stores all chosen values into sharedpreferences
     */
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

    /**
     * Initialized the view
     * @param root the root view
     */
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
        register = root.findViewById(R.id.registerButton);
        login = root.findViewById(R.id.loginButton);
        downloadData = root.findViewById(R.id.downloadData);
        saveData = root.findViewById(R.id.saveData);
    }

    /**
     * Registers listeners for the view
     * @param root the root view
     */
    public void registerListeners(final View root){
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
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getParentFragmentManager();
                RegisterFrag rg = new RegisterFrag("Register");
                rg.show(fm, "register");
            }
        });
        if(root.getContext().getSharedPreferences("searchPref", Context.MODE_PRIVATE).contains("loginToken")){
            login.setText("Logout: " + root.getContext().getSharedPreferences("searchPref", Context.MODE_PRIVATE).getString("loginUser", ""));
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        JsonObject json = new JsonObject();
                        json.addProperty("token", root.getContext().getSharedPreferences("searchPref", Context.MODE_PRIVATE).getString("loginToken", ""));
                        json.addProperty("type", "2");
                        Ion.with(v.getContext())
                                .load("https://testing.mgelsk.com")
                                .setJsonObjectBody(json)
                                .asJsonObject()
                                .setCallback(new FutureCallback<JsonObject>() {
                                    @Override
                                    public void onCompleted(Exception e, JsonObject result) {
                                        // do stuff with the result or error
                                        Log.e("test", result.toString());
                                        if (result.get("success").getAsInt() == 1) {
                                            SharedPreferences prefs = root.getContext().getSharedPreferences("searchPref", Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = prefs.edit();
                                            editor.remove("loginToken");
                                            editor.remove("loginUser");
                                            editor.commit();
                                            Toast.makeText(v.getContext(), result.get("message").getAsString(), Toast.LENGTH_SHORT).show();
                                            login.setText("Login");
                                        } else {
                                            SharedPreferences prefs = root.getContext().getSharedPreferences("searchPref", Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = prefs.edit();
                                            editor.remove("loginToken");
                                            editor.remove("loginUser");
                                            editor.commit();
                                            Toast.makeText(v.getContext(), result.get("message").getAsString(), Toast.LENGTH_SHORT).show();
                                        }
                                        registerListeners(root);
                                    }
                                });

                    } catch (Exception e) {}
                }
            });
            saveData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SavedItemDataSource a = new SavedItemDataSource(v.getContext());
                    try {
                        a.open();
                        ArrayList<SavedItem> b = a.getItems();
                        Gson gson = new Gson();
                        String data = gson.toJson(b);
                        JsonArray json1 = gson.fromJson(data, JsonArray.class);
                        JsonObject json = new JsonObject();
                        json.add("dataArr", json1);
                        json.addProperty("token", root.getContext().getSharedPreferences("searchPref", Context.MODE_PRIVATE).getString("loginToken", ""));
                        json.addProperty("type", "4");
                        Log.e("test", json.toString());
                        Ion.with(v.getContext())
                                .load("https://testing.mgelsk.com")
                                .setJsonObjectBody(json)
                                .asJsonObject()
                                .setCallback(new FutureCallback<JsonObject>() {
                                    @Override
                                    public void onCompleted(Exception e, JsonObject result) {
                                        // do stuff with the result or error

                                        Log.e("test", result.toString());
                                        if (result.get("success").getAsInt() == 1) {
                                            Toast.makeText(v.getContext(), "Save Success", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(v.getContext(), "Save Failure", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                        a.close();
                    } catch (Exception e) { e.printStackTrace();}
                }
            });
            downloadData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            saveData.setEnabled(true);
            downloadData.setEnabled(true);
        } else {
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fm = getParentFragmentManager();
                    RegisterFrag rg = new RegisterFrag(new RegisterFrag.SubmitListener() {
                        @Override
                        public void finishedSave() {
                            registerListeners(root);
                        }
                    }, "Login");
                    rg.show(fm, "login");
                }
            });
            saveData.setEnabled(false);
            downloadData.setEnabled(false);
        }
    }

    /**
     * Attains the result from the dateFragment
     * @param requestCode the resquestcode
     * @param resultCode the resultcode
     * @param data the date attained
     */
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

    /**
     * Sets default values for elements in the view
     * @param root the root view
     */
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
