package com.example.healthproducts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.healthproducts.server.adapter.ProductAdapter;
import com.example.healthproducts.server.dataBase.DataBase;
import com.example.healthproducts.server.domain.Product;
import com.example.healthproducts.server.fragment.AddProductFragment;
import com.example.healthproducts.server.fragment.ChangeProductFragment;
import com.example.healthproducts.server.rest.HealthProductsApiVolley;

import java.util.List;

public class Products_DataBase extends AppCompatActivity {


    private static final String PRODUCT_KEY = "Product";
    private RecyclerView rvProduct;
    private ProductAdapter productAdapter;
    HealthProductsApiVolley healthProductsApiVolley;
    private ItemTouchHelper.SimpleCallback simpleCallback;

    private AppCompatButton btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_data_base);

        btnAdd = findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddProductFragment addProductFragment = new AddProductFragment();

                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.fl_main, addProductFragment)
                        .commit();
            }
        });



        HealthProductsApiVolley healthProductsApiVolley = new HealthProductsApiVolley(this);
        healthProductsApiVolley.fillProduct();
        healthProductsApiVolley.fillCategory();



        rvProduct = findViewById(R.id.rv_book);

        productAdapter = new ProductAdapter(this, DataBase.PRODUCT_LIST);
        rvProduct.setAdapter(productAdapter);

        if(getIntent().hasExtra("code")){
            ChangeProductFragment changeProductFragment = new ChangeProductFragment();
            Bundle bundle = new Bundle();

            Product foundProduct = null;
            for (Product product : DataBase.PRODUCT_LIST) {
                if(product.getCode().toString().equals(getIntent().getStringExtra("code"))){
                    foundProduct = product;
                    break;
                }

            }
            bundle.putSerializable(PRODUCT_KEY, foundProduct);


            changeProductFragment.setArguments(bundle);

            (Products_DataBase.this).getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fl_main, changeProductFragment)
                    .commit();
        }


        simpleCallback = new ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT
        ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                Product product = DataBase.PRODUCT_LIST.get(viewHolder.getAdapterPosition());
                if(direction == ItemTouchHelper.LEFT){
                    healthProductsApiVolley.deleteProduct(product.getId());
                }
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rvProduct);

    }

    public void updateAdapter(){
        productAdapter.notifyDataSetChanged();
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        int size = fragmentList.size();
        if(size > 0){
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(fragmentList.get(size - 1))
                    .commit();

        }else{
            finish();

        }
    }

}