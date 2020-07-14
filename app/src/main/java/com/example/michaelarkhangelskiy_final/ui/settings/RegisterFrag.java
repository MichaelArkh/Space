package com.example.michaelarkhangelskiy_final.ui.settings;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.michaelarkhangelskiy_final.R;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

/**
 * The register/login fragment
 */
public class RegisterFrag extends DialogFragment {
    SubmitListener sub;
    String type;
    Button submit;
    EditText user, pass;
    TextView tv;

    /**
     * required empty constructor
     */
    public RegisterFrag() {
        // Required empty public constructor
    }

    /**
     * The constructor for a string to designate if its for logging in or registering
     * @param type of fragment
     */
    public RegisterFrag( String type) {
        //this.sub = listen;
        this.type = type;
    }

    /**
     * The constructor for a string to designate if its for logging in or registering and a listener
     * @param listen the listener when button is pushed
     * @param type of fragment
     */
    public RegisterFrag(SubmitListener listen, String type) {
        this.sub = listen;
        this.type = type;
    }


    /**
     * The submitlistener when button is pushed
     */
    public interface SubmitListener {
        void finishedSave();
    }

    /**
     * Lifecycle method
     * @param savedInstanceState the savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Creates the view for fragment
     * @param inflater the inflator
     * @param container the container
     * @param savedInstanceState the savedInstanceState
     * @return the view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        initView(view);
        initButton(view);
        return view;
    }

    /**
     * Initializes the view
     * @param view the root view
     */
    private void initView(View view){
        tv = view.findViewById(R.id.textViewName);
        submit = view.findViewById(R.id.submitButton);
        user = view.findViewById(R.id.saveUser);
        pass = view.findViewById(R.id.savePass);
        tv.setText(type);
    }

    /**
     * Initializes the submit button
     * @param view the root view
     */
    private void initButton(View view){
        if(type.equals("Register")) {
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        JsonObject json = new JsonObject();
                        json.addProperty("username", user.getText().toString());
                        json.addProperty("password", pass.getText().toString());
                        dismiss();
                        json.addProperty("type", "0");
                        Ion.with(v.getContext())
                                .load(getString(R.string.login_api))
                                .setJsonObjectBody(json)
                                .asJsonObject()
                                .setCallback(new FutureCallback<JsonObject>() {
                                    @Override
                                    public void onCompleted(Exception e, JsonObject result) {
                                        // do stuff with the result or error
                                        Log.e("test", result.toString());
                                        if(result.get("success").getAsInt() == 1) {
                                            Toast.makeText(v.getContext(), "Successfully registered", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(v.getContext(), result.get("message").getAsString(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    } catch (Exception e) {}
                }
            });
        } else {
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        JsonObject json = new JsonObject();
                        String usera = user.getText().toString();
                        json.addProperty("username", user.getText().toString());
                        json.addProperty("password", pass.getText().toString());

                        json.addProperty("type", "1");
                        Ion.with(v.getContext())
                                .load(getString(R.string.login_api))
                                .setJsonObjectBody(json)
                                .asJsonObject()
                                .setCallback(new FutureCallback<JsonObject>() {
                                    @Override
                                    public void onCompleted(Exception e, JsonObject result) {
                                        // do stuff with the result or error
                                        Log.e("test", result.toString());
                                        if(result.get("success").getAsInt() == 1) {
                                            Toast.makeText(v.getContext(), "Successfully logged in", Toast.LENGTH_SHORT).show();
                                            v.getContext().getSharedPreferences("searchPref", Context.MODE_PRIVATE).edit().putString("loginToken", result.get("token").getAsString()).apply();
                                            v.getContext().getSharedPreferences("searchPref", Context.MODE_PRIVATE).edit().putString("loginUser", usera).apply();
                                        } else {
                                            Toast.makeText(v.getContext(), result.get("message").getAsString(), Toast.LENGTH_SHORT).show();
                                        }
                                        dismiss();
                                        sub.finishedSave();
                                    }
                                });
                    } catch (Exception e) {}
                }
            });
        }
    }
}
