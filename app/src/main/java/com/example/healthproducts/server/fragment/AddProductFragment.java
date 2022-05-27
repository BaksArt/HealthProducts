package com.example.healthproducts.server.fragment;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.healthproducts.R;
import com.example.healthproducts.server.adapter.CategorySpinnerAdapter;
import com.example.healthproducts.server.dataBase.DataBase;
import com.example.healthproducts.server.domain.Category;
import com.example.healthproducts.server.domain.Product;
import com.example.healthproducts.server.rest.HealthProductsApiVolley;

public class AddProductFragment extends Fragment {
    private AppCompatSpinner spCategory;
    private CategorySpinnerAdapter categorySpinnerAdapter;
    private EditText etProductName;
    private EditText etProductCode;
    private EditText etProductComposition;
    private EditText etProductFoto;
    private AppCompatButton btnAdd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {




        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_product, container, false);

        spCategory = view.findViewById(R.id.sp_category);
        btnAdd = view.findViewById(R.id.btn_add);
        etProductName = view.findViewById(R.id.et_productName);
        etProductCode = view.findViewById(R.id.et_productCode);
        etProductFoto = view.findViewById(R.id.et_productFoto);
        etProductComposition = view.findViewById(R.id.et_productComposition);

        categorySpinnerAdapter = new CategorySpinnerAdapter(getContext(), DataBase.CATEGORY_NAMES_LIST);

        spCategory.setAdapter(categorySpinnerAdapter);



        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new HealthProductsApiVolley(getContext()).addProduct(
                        new Product(
                                etProductName.getText().toString(),
                                etProductCode.getText().toString(),
                                DataBase.CATEGORY_LIST.get((int) spCategory.getSelectedItemId()),
                                etProductComposition.getText().toString(),
                                etProductFoto.getText().toString()

                        )
                );

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .remove(AddProductFragment.this)
                        .commit();
            }
        });


        return view;}
}