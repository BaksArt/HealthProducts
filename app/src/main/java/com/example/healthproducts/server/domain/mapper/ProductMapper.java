package com.example.healthproducts.server.domain.mapper;

import com.example.healthproducts.server.domain.Product;

import org.json.JSONException;
import org.json.JSONObject;

public class ProductMapper {
    public static Product productFromJson(JSONObject jsonObject){
        Product product = null;

        try{
            product = new Product(
                    jsonObject.getInt("id"),
                    jsonObject.getString("name"),
                    jsonObject.getString("code"),
                    CategoryMapper.categoryFromProductJson(jsonObject),
                    jsonObject.getString("composition"),
                    jsonObject.getString("foto")
            );
        }catch (JSONException e){e.printStackTrace();}

        return product;
    }
}
