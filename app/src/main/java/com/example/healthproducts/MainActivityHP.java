package com.example.healthproducts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.healthproducts.server.adapter.ProductAdapter;
import com.example.healthproducts.server.dataBase.DataBase;
import com.example.healthproducts.ui.DataBaseFragment;
import com.example.healthproducts.ui.ProductInfoFragment;
import com.example.healthproducts.ui.ScannerFragment;
import com.example.healthproducts.ui.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

public class MainActivityHP extends AppCompatActivity {
    public boolean isActiveInfoProductFragment;


    BottomNavigationView bottomNavigationView;
    ScannerFragment scannerFragment = new ScannerFragment();
    DataBaseFragment dataBaseFragment = new DataBaseFragment();
    SettingsFragment settingsFragment = new SettingsFragment();

    SharedPreferences settings;

    private ProductAdapter productAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_hp);

        SettingsFragment settingsFragment = new SettingsFragment();



       bottomNavigationView = findViewById(R.id.botton_navigation);

        settings = this.getPreferences(Context.MODE_PRIVATE);
        switch (settings.getInt("THEME", 0)){
            case 0:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;

            case 1:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;

            case 2:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
        }

        if(settings.getBoolean("IS_CHANGED", false)){
            getSupportFragmentManager().beginTransaction().replace(R.id.container, settingsFragment).commit();
            SharedPreferences.Editor ed = settings.edit();
            ed.putBoolean("IS_CHANGED", false);
            ed.apply();
        }else{
            getSupportFragmentManager().beginTransaction().replace(R.id.container, scannerFragment).commit();

        }

       bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
           @Override
           public boolean onNavigationItemSelected(MenuItem item) { //navigation bottom bar
               SharedPreferences.Editor ed = settings.edit();
               ed.putBoolean("IS_CHANGED", false);
               ed.apply();
               switch(item.getItemId()){
                   case R.id.scanner:
                   getSupportFragmentManager().beginTransaction().replace(R.id.container, scannerFragment).commit();
                   return true;

                   case R.id.database:
                       getSupportFragmentManager().beginTransaction().replace(R.id.container, dataBaseFragment).commit();
                       return true;

                   case R.id.settings:
                       getSupportFragmentManager().beginTransaction().replace(R.id.container, settingsFragment).commit();
                       return true;



           }

           return false;
           }
       });

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        int size = fragmentList.size();
        if(size > 1){
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(fragmentList.get(size - 1))
                    .commit();

        }else{
            finish();

        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }
    public void updateAdapter(){
        productAdapter.notifyDataSetChanged();
    }
}