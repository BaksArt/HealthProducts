package com.example.healthproducts.ui;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.healthproducts.MainActivityHP;
import com.example.healthproducts.R;
import com.example.healthproducts.server.adapter.CategorySpinnerAdapter;
import com.example.healthproducts.server.adapter.ProductAdapter;
import com.example.healthproducts.server.dataBase.DataBase;
import com.example.healthproducts.server.domain.Product;
import com.example.healthproducts.server.fragment.ChangeProductFragment;
import com.example.healthproducts.server.rest.HealthProductsApiVolley;
import com.squareup.picasso.Picasso;

public class ProductInfoFragment extends Fragment {
    private TextView tvCategory;
    private TextView tvProductName;
    private TextView tvProductCode;
    private TextView tvProductComposition;
    private ImageView ivProductFoto;
    private AppCompatButton btnClose;
    private ProgressBar progressHealth;

    private LinearLayout mFreePalmOil, mFreeSugar, mFreeE, mFullNature;
    private TextView percText;

    Product product;

    int progress;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_info, container, false);

        product = (Product)getArguments().getSerializable(ProductAdapter.PRODUCT_KEY);
        String act = getArguments().getSerializable("act").toString();

        tvCategory = view.findViewById(R.id.tv_category);
        btnClose = view.findViewById(R.id.btn_close);
        tvProductName = view.findViewById(R.id.tv_productName);
        tvProductCode = view.findViewById(R.id.tv_productCode);
        ivProductFoto = view.findViewById(R.id.iv_product_image);
        tvProductComposition = view.findViewById(R.id.tv_productComposition);
        progressHealth = view.findViewById(R.id.prodress_bar);
        percText = view.findViewById(R.id.infoHealth);

        mFreePalmOil = view.findViewById(R.id.m_free_palm_oil);
        mFreeSugar = view.findViewById(R.id.m_free_sugar);
        mFreeE = view.findViewById(R.id.m_free_e);
        mFullNature = view.findViewById(R.id.m_full_nature);

        tvProductName.setText(product.getName());
        tvProductCode.setText(product.getCode());
        tvProductComposition.setText(product.getComposition());
        tvCategory.setText(product.getCategory().getName());

        try{
            Picasso.get().load(product.getFoto()).into(ivProductFoto); //получаем картинку из url с помощью библиотеки
        }catch(Exception e){e.printStackTrace();}

        checkComposition();




        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               ScannerFragment scannerFragment = new ScannerFragment();
               DataBaseFragment dataBaseFragment = new DataBaseFragment();
                if(act.equals("scanner")){
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.container, scannerFragment)
                            .commit();
                }else{
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.container, dataBaseFragment)
                            .commit();
                }

            }
        });

        return view;
    }

    private void setHighLightedText(TextView tv, String textToHighlight,int addSymb, int color, int addProgress) { //метод выделения слов в составе
        String tvt = tv.getText().toString();
        int ofe = tvt.indexOf(textToHighlight, 0);
        Spannable wordToSpan = new SpannableString(tv.getText());
        for (int ofs = 0; ofs < tvt.length() && ofe != -1; ofs = ofe + 1) {
            ofe = tvt.indexOf(textToHighlight, ofs);
            if (ofe == -1)
                break;
            else {
                progress += addProgress;
                wordToSpan.setSpan(new BackgroundColorSpan(color), ofe, ofe + textToHighlight.length() + addSymb, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv.setText(wordToSpan, TextView.BufferType.SPANNABLE);
            }
        }
    }

    private void checkComposition(){ //метод проверки состава продукта
        setHighLightedText(tvProductComposition, "жир растительный", 0, Color.argb(255, 255, 193, 7), 20);
        setHighLightedText(tvProductComposition, "растительные масла", 0, Color.argb(255, 255, 193, 7), 20);
        setHighLightedText(tvProductComposition, "растительное масло", 0, Color.argb(255, 255, 193, 7), 20);
        setHighLightedText(tvProductComposition, "масло растительное", 0, Color.argb(255, 255, 193, 7), 20);
        setHighLightedText(tvProductComposition, "пальмовое", 0, Color.argb(255, 255, 193, 7), 20);
        setHighLightedText(tvProductComposition, "идентичный натуральному", 0, Color.argb(255, 255, 193, 7), 10);
        setHighLightedText(tvProductComposition, "E", 3,  Color.argb(255, 255, 193, 7),5);
        setHighLightedText(tvProductComposition, "Е", 3,  Color.argb(255, 255, 193, 7), 5);

        progress = 100 - progress;
        if(progress >= 0){
            progressHealth.setProgress(progress);
        }else{
            progressHealth.setProgress(0);
            progress = 0;
        }

        percText.setText("Продукт натурален на " + String.valueOf(progress) + "%");

        if(!product.getComposition().contains("жир растительный") && !product.getComposition().contains("растительные масла") && !product.getComposition().contains("растительное масло,") && !product.getComposition().contains("пальмовое")){
            mFreePalmOil.setVisibility(View.VISIBLE);
        }

        if(!product.getComposition().contains("E") && !product.getComposition().contains("Е")){
            mFreeE.setVisibility(View.VISIBLE);
        }

        if(!product.getComposition().contains("сахар")){
            mFreeSugar.setVisibility(View.VISIBLE);
        }

        if(progress == 100){
            mFullNature.setVisibility(View.VISIBLE);
        }
    }


}