package com.example.healthproducts.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.healthproducts.R;
import com.example.healthproducts.server.adapter.ProductAdapter;
import com.example.healthproducts.server.dataBase.DataBase;
import com.example.healthproducts.server.domain.Category;
import com.example.healthproducts.server.domain.Product;
import com.example.healthproducts.server.rest.HealthProductsApiVolley;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;


public class SearchProductFragment extends Fragment {
    private String code;

    WebView gifLoad;
    public static final String loadUrl = "https://s2.siteapi.org/17f5be73889a440/docs/d711owd1khwgg0484csg8w8kgko00w";
    private TextView textStatus;
    private ImageView imageStatus;
    Handler handler;
    private TextView nameProduct;
    private LinearLayout infoProduct;
    private boolean isFinding;
    private Document doc;
    private Thread parseThread;
    private Runnable parseRunnable;
    Elements elements;
    ArrayList<String> urls;
    String foundName;
    String foundComposition;
    String foundFototUrl;
    Category foundCategory;
    Product foundProduct;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search_product, container, false);
        gifLoad = view.findViewById(R.id.gifload);
        textStatus = view.findViewById(R.id.textStatus);
        imageStatus = view.findViewById(R.id.imageStatus);
        nameProduct = view.findViewById(R.id.nameProduct);
        infoProduct = view.findViewById(R.id.productInfo);

        infoProduct.setVisibility(View.GONE);

        imageStatus.setVisibility(View.GONE);
        gifLoad.setVisibility(View.GONE);


        handler = new Handler();

        urls = new ArrayList<>();

        code = getArguments().getSerializable("code").toString();

        showStatus("search");

        Product foundProduct = null;
        for (Product product : DataBase.PRODUCT_LIST) {
            if(product.getCode().equals(code.toString())){
                foundProduct = product;
                break;
            }
            Log.d("API_TEST", code + " = + = " + product.getCode());
        }

        if(foundProduct != null){ //если нашли продукт в базе данных, запускаем фрагмент с этим продуктом
            ProductInfoFragment productInfoFragment = new ProductInfoFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(ProductAdapter.PRODUCT_KEY, foundProduct);
            bundle.putSerializable("act", "scanner");


            productInfoFragment.setArguments(bundle);

            ((AppCompatActivity)getContext()).getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, productInfoFragment)
                    .commit();

        }
        showStatus("load");
        init();
        return view;
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
    private void getWeb(){ //метод получения url продукта (заходит в гугл и парсит все ссылки)

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

    void findProductInfo(){ //метод для нахождения информации о продукте
        Log.d("ParseLog", "=================[STEP 2: search for name]=================================");

        int i = 0;
        while(i < urls.size()) { //проходим по всем url
            try{
            try {
                doc = Jsoup.connect(urls.get(i)).get();
                Log.d("ParseLog", "connected to: " + urls.get(i));

            } catch (IOException e) {
                e.printStackTrace();
            }


            if (urls.get(i).contains("goods.kaypu.com")) {
                Element el = doc.getElementById("gallery");

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                foundFototUrl = "https://goods.kaypu.com" + el.select("a").attr("href").toString();
                Log.d("ParseLog", "fotoUrl " + foundFototUrl);
                Elements els = doc.getElementsByClass("good-desc");


                foundName = doc.getElementsByClass("hero").get(0).text();


                Product product = new Product(
                        foundName,
                        code,
                        DataBase.CATEGORY_LIST.get(0),
                        foundComposition,
                        foundFototUrl

                );
                if (foundName != null && foundComposition != null) {
                    new HealthProductsApiVolley(getContext()).addProduct(product);


                    foundProduct = product;
                    if (!DataBase.PRODUCT_LIST.contains(foundProduct)) {



                        Log.d("ParseLog", "product: " + foundProduct.toString());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                showStatus("complete");
                            }
                        });


                        break;
                    }
                }


            }

            if (urls.get(i).contains("www.onlinetrade.ru")){
                    Elements els = doc.getElementsByClass("productPage");
                    foundName = els.get(0).children().get(0).children().get(0).text();
                    els = doc.getElementsByClass("oldContent");
                    foundComposition = els.get(0).children().get(0).children().get(3).text();
                    els = doc.getElementsByClass("productPage__displayedItem__images__big");
                    foundFototUrl = els.get(0).select("a").attr("href");

                foundCategory = new Category(doc.getElementsByClass("productPage__shortProperties").get(0).children().get(1).children().get(1).text().substring(4));

                Product product = new Product(
                        foundName,
                        code,
                        DataBase.CATEGORY_LIST.get(1),
                        foundComposition,
                        foundFototUrl

                );
                if (foundName != null && foundComposition != null) {
                    new HealthProductsApiVolley(getContext()).addProduct(product);


                    foundProduct = product;
                    if (!DataBase.PRODUCT_LIST.contains(foundProduct)) {


                        Log.d("ParseLog", "product: " + foundProduct.toString());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                showStatus("complete");
                            }
                        });


                        break;
                    }
                }
            }



        }catch(Exception e) {
                e.printStackTrace();

            }

            i++;
        }


        handler.post(new Runnable() {
            @Override
            public void run() {
                showStatus("error");
            }
        });


    }

    @SuppressLint("ResourceAsColor")
    void showStatus(String status){ //метод показа статуса поиска продукта
        switch(status){
            case "load":
                gifLoad.loadUrl(loadUrl);
                gifLoad.setVisibility(View.VISIBLE);
                imageStatus.setVisibility(View.GONE);
                textStatus.setText("Поиск товара...");
                textStatus.setTextColor(R.color.darkGray);
                break;

            case "search":
                gifLoad.loadUrl(loadUrl);
                gifLoad.setVisibility(View.VISIBLE);
                imageStatus.setVisibility(View.GONE);
                textStatus.setText("Поиск по базе данных...");
                textStatus.setTextColor(R.color.darkGray);
                break;

            case "error":
                gifLoad.setVisibility(View.GONE);
                imageStatus.setVisibility(View.VISIBLE);
                imageStatus.setImageResource(R.drawable.error);
                textStatus.setText("Товар не найден");
                textStatus.setTextColor(R.color.yellow);
                break;

            case "complete":
                gifLoad.setVisibility(View.GONE);
                imageStatus.setVisibility(View.VISIBLE);
                imageStatus.setImageResource(R.drawable.complete);
                textStatus.setText("Поиск завершен!");
                textStatus.setTextColor(R.color.cyan);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Bundle bundle = new Bundle();
                bundle.putSerializable(ProductAdapter.PRODUCT_KEY, foundProduct);
                bundle.putSerializable("act", "scanner");

                ProductInfoFragment productInfoFragment = new ProductInfoFragment();

                productInfoFragment.setArguments(bundle);

                ((AppCompatActivity)getContext()).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, productInfoFragment)
                        .commit();
                break;


        }
    }
}