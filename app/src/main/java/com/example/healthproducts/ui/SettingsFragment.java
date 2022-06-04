package com.example.healthproducts.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthproducts.MainActivityHP;
import com.example.healthproducts.R;


public class SettingsFragment extends Fragment {


    private Spinner spinnerTheme;
    private LinearLayout themeSet;
    SharedPreferences settings;
    public static final String APP_PREFERENCES = "settings";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        String[] themes = {"Светлая", "Темная", "Системная"};


        spinnerTheme = view.findViewById(R.id.spinner_themes);
        themeSet = view.findViewById(R.id.theme_set);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, themes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        settings = getActivity().getPreferences(Context.MODE_PRIVATE);

        spinnerTheme.setAdapter(adapter);
        if(spinnerTheme.getSelectedItemId() != settings.getInt("THEME", 0)) {spinnerTheme.setSelection(settings.getInt("THEME", 0));}
        spinnerTheme.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                SettingsFragment settingsFragment = new SettingsFragment();
                SharedPreferences.Editor ed;
                switch (position){
                    case 0:
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        settings = getActivity().getPreferences(Context.MODE_PRIVATE);
                        ed = settings.edit();
                        ed.putInt("THEME", position);
                        ed.putBoolean("IS_CHANGED", true);
                        ed.apply();
                        break;

                    case 1:
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        settings = getActivity().getPreferences(Context.MODE_PRIVATE);
                        ed = settings.edit();
                        ed.putInt("THEME", position);
                        ed.putBoolean("IS_CHANGED", true);
                        ed.apply();
                        break;

                    case 2:
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                        settings = getActivity().getPreferences(Context.MODE_PRIVATE);
                        ed = settings.edit();
                        ed.putInt("THEME", position);
                        ed.putBoolean("IS_CHANGED", true);
                        ed.apply();
                        break;
                }




            }
            @Override
            public void onNothingSelected(AdapterView<?> arg) {
            }
        });

        themeSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerTheme.performClick();
            }
        });

        return view;
    }



}