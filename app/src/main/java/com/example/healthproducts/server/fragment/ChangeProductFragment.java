package com.example.healthproducts.server.fragment;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.healthproducts.R;
import com.example.healthproducts.server.adapter.CategorySpinnerAdapter;
import com.example.healthproducts.server.adapter.ProductAdapter;
import com.example.healthproducts.server.dataBase.DataBase;
import com.example.healthproducts.server.domain.Category;
import com.example.healthproducts.server.domain.Product;
import com.example.healthproducts.server.rest.HealthProductsApiVolley;

public class ChangeProductFragment extends Fragment {
    private AppCompatSpinner spCategory;
    private CategorySpinnerAdapter categorySpinnerAdapter;
    private EditText etProductName;
    private EditText etProductCode;
    private EditText etProductComposition;
    private EditText etProductFoto;
    private AppCompatButton btnChange;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_product, container, false);

        Product product = (Product)getArguments().getSerializable(ProductAdapter.PRODUCT_KEY);

        spCategory = view.findViewById(R.id.sp_category);
        btnChange = view.findViewById(R.id.btn_change);
        etProductName = view.findViewById(R.id.et_productName);
        etProductCode = view.findViewById(R.id.et_productCode);
        etProductFoto = view.findViewById(R.id.et_productFoto);
        etProductComposition = view.findViewById(R.id.et_productComposition);

        etProductName.setText(product.getName());
        etProductCode.setText(product.getCode());
        etProductFoto.setText(product.getFoto());
        etProductComposition.setText(product.getComposition());



        categorySpinnerAdapter = new CategorySpinnerAdapter(getContext(), DataBase.CATEGORY_NAMES_LIST);



        spCategory.setAdapter(categorySpinnerAdapter);

        spCategory.setSelection(product.getCategory().getId() - 1, true);

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new HealthProductsApiVolley(getContext()).updateProduct(
                        product.getId(),
                        etProductName.getText().toString(),
                        etProductCode.getText().toString(),
                        spCategory.getSelectedItem().toString(),
                        etProductComposition.getText().toString(),
                        etProductFoto.getText().toString()

                );

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .remove(ChangeProductFragment.this)
                        .commit();
            }
        });


        return view;
    }
}