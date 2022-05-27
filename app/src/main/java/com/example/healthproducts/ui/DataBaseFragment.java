package com.example.healthproducts.ui;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.healthproducts.R;
import com.example.healthproducts.server.adapter.ProductAdapter;
import com.example.healthproducts.server.dataBase.DataBase;
import com.example.healthproducts.server.domain.Category;
import com.example.healthproducts.server.domain.Product;
import com.example.healthproducts.server.rest.HealthProductsApiVolley;

import java.util.List;


public class DataBaseFragment extends Fragment {
    private static final String PRODUCT_KEY = "Product";
    private RecyclerView rvProduct;
    private ProductAdapter productAdapter;
    HealthProductsApiVolley healthProductsApiVolley;
    private ItemTouchHelper.SimpleCallback simpleCallback;

    private AppCompatButton btnAdd;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_data_base, container, false);

        HealthProductsApiVolley healthProductsApiVolley = new HealthProductsApiVolley(getContext());
        healthProductsApiVolley.fillProduct();
        healthProductsApiVolley.fillCategory();



        rvProduct = view.findViewById(R.id.rv_book);

        productAdapter = new ProductAdapter(getContext(), DataBase.PRODUCT_LIST);
        rvProduct.setAdapter(productAdapter);

       return view;
    }




}