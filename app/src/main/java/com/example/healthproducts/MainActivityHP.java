package com.example.healthproducts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
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
    private ProductAdapter productAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_hp);

       bottomNavigationView = findViewById(R.id.botton_navigation);

       getSupportFragmentManager().beginTransaction().replace(R.id.container, scannerFragment).commit();
       bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
           @Override
           public boolean onNavigationItemSelected(MenuItem item) { //navigation bottom bar
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
    public void updateAdapter(){
        productAdapter.notifyDataSetChanged();
    }
}