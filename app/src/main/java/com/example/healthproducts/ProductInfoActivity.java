package com.example.healthproducts;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.healthproducts.server.dataBase.DataBase;
import com.example.healthproducts.server.domain.Product;
import com.example.healthproducts.server.rest.HealthProductsApiVolley;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class ProductInfoActivity extends AppCompatActivity {




    private String code;

    WebView gifLoad;
    private String loadUrl = "https://s2.siteapi.org/17f5be73889a440/docs/d711owd1khwgg0484csg8w8kgko00w";
    private TextView textStatus;
    private ImageView imageStatus;
    Handler handler;



    private TextView nameProduct;
    private LinearLayout infoProduct;

    

    private boolean isFinding = true;


    private Document doc;
    private Thread parseThread;
    private Runnable parseRunnable;
    Elements elements;

    ArrayList<String> urls;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);

        gifLoad = findViewById(R.id.gifload);
        textStatus = findViewById(R.id.textStatus);
        imageStatus = findViewById(R.id.imageStatus);
        nameProduct = findViewById(R.id.nameProduct);
        infoProduct = findViewById(R.id.productInfo);

        infoProduct.setVisibility(View.GONE);

        imageStatus.setVisibility(View.GONE);
        gifLoad.setVisibility(View.GONE);


        handler = new Handler();


        urls = new ArrayList<>();
        code = getIntent().getStringExtra("code");

        showStatus("search");

        Product foundProduct = null;

        for (Product product : DataBase.PRODUCT_LIST) {
            if(product.getCode().toString().equals(code.toString())){
                foundProduct = product;
                break;
            }Log.d("API_TEST", code + " = + = " + product.getCode());
        }

        if(foundProduct != null){
            Intent intent = new Intent(ProductInfoActivity.this, Products_DataBase.class);
            intent.putExtra("code", code);
            startActivity(intent);
        }
        showStatus("load");
        init();


    }

    private void init(){
        parseRunnable = new Runnable() {
            @Override
            public void run() {
                getWeb();
            }
        };
        parseThread = new Thread(parseRunnable);
        parseThread.start();
    }
    private void getWeb(){

        try {
            doc = Jsoup.connect("https://www.google.com/search?q=" + code).get();
            Log.d("ParseLog", "connected to: https://www.google.com/search?q=" + code);


            for (int a = 0; a < urls.size(); a++) {
                urls.remove(a);
            }

        } catch (IOException e) {
            Log.d("ParseLog", "ERROR TO CONNECT");
            e.printStackTrace();

        }
        Element table = doc.getElementById("rso");
        try{
            elements = table.children();
        }catch (Exception e){
            Log.d("ParseLog", "ERROR: No results found for " + code);

            handler.post(new Runnable() {
                @Override
                public void run() {
                    showStatus("error");
                }
            });
        }
        int i = 0;
        Log.d("ParseLog", "=================[STEP 1: search for urls]=================================");
        while(true) {
            try{
                Element el1Url = elements.get(i);
                Elements elements1Url = el1Url.children();

                Element el2Url = elements1Url.get(0);
                Elements elements2Url = el2Url.children();
                Log.d("ParseLog", "========url: " + el2Url.absUrl("href"));
                addSiteUrl(el2Url.absUrl("href"));

                Element el3Url = elements2Url.get(0);
                Elements elements3Url = el3Url.children();
                Log.d("ParseLog", "========url: " + el3Url.absUrl("href"));
                addSiteUrl(el3Url.absUrl("href"));

                Element el4Url = elements3Url.get(0);
                Elements elements4Url = el4Url.children();
                Log.d("ParseLog", "========url: " + el4Url.absUrl("href"));
                addSiteUrl(el4Url.absUrl("href"));

                Element el5Url = elements4Url.get(0);
                Elements elements5Url = el5Url.children();
                Log.d("ParseLog", "========url: " + el5Url.absUrl("href"));
                addSiteUrl(el5Url.absUrl("href"));

                Element el6Url = elements5Url.get(0);
                Log.d("ParseLog", "========url: " + el6Url.absUrl("href"));
                addSiteUrl(el6Url.absUrl("href"));
            }catch (Exception e){ }
            i++;
            if(i > 30){
                break;
            }
        }
        Log.d("ParseLog", urls.toString());
        isFinding = false;
        findProductInfo();
    }

    void addSiteUrl(String url){
        if(!url.equals("")){
            urls.add(url);
        }
    }

    void findProductInfo(){
        Log.d("ParseLog", "=================[STEP 2: search for name]=================================");
        String nameProductWeb = "";


        /*
        int i = 0;
        while(i < urls.size()) {
            try {

                doc = Jsoup.connect(urls.get(i)).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("ParseLog", "connected to: " + urls.get(i));
            try{
                nameProductWeb = doc.getElementsByTag("h1").text();
                Log.d("ParseLog", "I_name_ " + nameProductWeb);
            }catch(Exception e){

            }


            Log.d("ParseLog", "I_contains_composition_ " + doc.getElementsByTag("h1").parents().text().contains("состав"));


            if(doc.getElementsByTag("h1").parents().text().contains("состав")) {
                Elements elsC = doc.getElementsByTag("h1");

                Element el = elsC.get(0).parent();

                Elements elsForSearchComposition = el.children();
                Element elForCheck = elsForSearchComposition.get(0);

                Log.d("ParseLog", "I_parent_ " + el.className());
                int a = 0;
            while (!elForCheck.text().contains("Cостав:")){
                a++;
                try{
                    elForCheck = elsForSearchComposition.get(a);
                }catch(Exception e){}


                if(elForCheck.text().contains("состав") || a > 100){
                    break;
                }
                Log.d("ParseLog", "I_STEP_ " + a + " I " + elForCheck.text() + " I " + elForCheck.id());

            }
                Log.d("ParseLog", "I_composition_ " + elForCheck.text());

            }




            if(nameProductWeb != null){

                String finalNameProductWeb = nameProductWeb;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        showStatus("complete");
                        infoProduct.setVisibility(View.VISIBLE);
                        nameProduct.setText(finalNameProductWeb);

                    }
                });
            }
            i++;

        }*/

        int i = 0;
        while(i < urls.size()) {



        }

    }

    void showStatus(String status){
        switch(status){
            case "load":
                gifLoad.loadUrl(loadUrl);
                gifLoad.setVisibility(View.VISIBLE);
                imageStatus.setVisibility(View.GONE);
                textStatus.setText("Поиск товара...");
                textStatus.setTextColor(getResources().getColor(R.color.darkGray));
                break;

            case "search":
                gifLoad.loadUrl(loadUrl);
                gifLoad.setVisibility(View.VISIBLE);
                imageStatus.setVisibility(View.GONE);
                textStatus.setText("Поиск по базе данных...");
                textStatus.setTextColor(getResources().getColor(R.color.darkGray));
                break;

            case "error":
                gifLoad.setVisibility(View.GONE);
                imageStatus.setVisibility(View.VISIBLE);
                imageStatus.setImageResource(R.drawable.error);
                textStatus.setText("Товар не найден");
                textStatus.setTextColor(getResources().getColor(R.color.yellow));
                break;

            case "complete":
                gifLoad.setVisibility(View.GONE);
                imageStatus.setVisibility(View.VISIBLE);
                imageStatus.setImageResource(R.drawable.complete);
                textStatus.setText("Поиск завершен!");
                textStatus.setTextColor(getResources().getColor(R.color.cyan));
                break;


        }
    }



    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}