package com.example.healthproducts.server.rest;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.healthproducts.MainActivityHP;
import com.example.healthproducts.server.dataBase.DataBase;
import com.example.healthproducts.server.domain.Category;
import com.example.healthproducts.server.domain.Product;
import com.example.healthproducts.server.domain.mapper.CategoryMapper;
import com.example.healthproducts.server.domain.mapper.ProductMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HealthProductsApiVolley implements HealthProductsApi{

    public static final String API_TEST = "API_TEST";
    private final Context context;
    public static final String BASE_URL = "http://192.168.43.14:8085";

    private Response.ErrorListener errorListener;

    public HealthProductsApiVolley(Context context){
        this.context = context;

        errorListener = error -> Log.d(API_TEST, error.toString());
    }

    @Override
    public void fillProduct() {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String url = BASE_URL + "/product";


        JsonArrayRequest arrayRequest = new JsonArrayRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        DataBase.PRODUCT_LIST.clear();
                        try{
                            for(int i = 0; i < response.length(); i++){
                                JSONObject jsonObject = response.getJSONObject(i);
                                Product product = ProductMapper.productFromJson(jsonObject);
                                DataBase.PRODUCT_LIST.add(product);
                            }
                            try{
                                ((MainActivityHP)context).updateAdapter();
                            }catch(Exception e){}


                            Log.d(API_TEST, DataBase.PRODUCT_LIST.toString());
                        }catch (JSONException e){e.printStackTrace();}
                    }
                },
                errorListener
        );

        requestQueue.add(arrayRequest);
    }

    @Override
    public void fillCategory() {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String url = BASE_URL + "/category";

        JsonArrayRequest arrayRequest = new JsonArrayRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        DataBase.CATEGORY_LIST.clear();
                        DataBase.CATEGORY_NAMES_LIST.clear();
                        try{
                            for(int i = 0; i < response.length(); i++){
                                JSONObject jsonObject = response.getJSONObject(i);
                                Category category = CategoryMapper.categoryFromJson(jsonObject);
                                DataBase.CATEGORY_LIST.add(category);
                                DataBase.CATEGORY_NAMES_LIST.add(category.getName());
                            }


                            Log.d(API_TEST, DataBase.CATEGORY_LIST.toString());
                        }catch (JSONException e){e.printStackTrace();}
                    }
                },
                errorListener
        );

        requestQueue.add(arrayRequest);
    }

    @Override
    public void addProduct(Product product) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String url = BASE_URL + "/product";

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        fillProduct();
                        Log.d(API_TEST, response);
                    }
                },
                errorListener
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("nameProduct", product.getName());
                params.put("code", product.getCode());
                params.put("nameCategory", product.getCategory().getName());
                params.put("composition", product.getComposition());
                params.put("foto", product.getFoto());

                return params;
            }
        };

        requestQueue.add(request);
    }

    @Override
    public void updateProduct(int id, String newProductName, String newCode, String newCategoryName, String newComposition, String newFoto) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String url = BASE_URL + "/product" + "/" + id;

        StringRequest request = new StringRequest(
                Request.Method.PUT,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        fillProduct();
                        Log.d(API_TEST, response);
                    }
                },
                errorListener
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("nameProduct", newProductName);
                params.put("code", newCode);
                params.put("nameCategory", newCategoryName);
                params.put("composition", newComposition);
                params.put("foto", newFoto);

                return params;
            }
        };

        requestQueue.add(request);
    }

    @Override
    public Product findProductByCode(String code) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String url = BASE_URL + "/product" + "/" + code;
        final Product[] product = new Product[1];

        JsonArrayRequest arrayRequest = new JsonArrayRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try{
                            for(int i = 0; i < response.length(); i++){
                                JSONObject jsonObject = response.getJSONObject(i);
                                product[0] = ProductMapper.productFromJson(jsonObject);
                            }


                        }catch (JSONException e){e.printStackTrace();}
                    }
                },
                errorListener
        );

        requestQueue.add(arrayRequest);
        return product[0];
    }


    @Override
    public void deleteProduct(int id) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String url = BASE_URL + "/product" + "/" + id;

        StringRequest request = new StringRequest(
                Request.Method.DELETE,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        fillProduct();
                        Log.d(API_TEST, response);
                    }
                },
                errorListener
        );

        requestQueue.add(request);
    }



}
