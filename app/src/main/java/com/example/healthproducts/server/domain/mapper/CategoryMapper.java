package com.example.healthproducts.server.domain.mapper;

import com.example.healthproducts.server.domain.Category;

import org.json.JSONException;
import org.json.JSONObject;

public class CategoryMapper {
    public static Category categoryFromJson(JSONObject jsonObject){
        Category category = null;

        try{
            category = new Category(
                    jsonObject.getInt("id"),
                    jsonObject.getString("name")
            );
        }catch (JSONException e){e.printStackTrace();}


        return category;
    }

    public static Category categoryFromProductJson(JSONObject jsonObject){
        Category category = null;

        try{
            category = new Category(
                    jsonObject.getJSONObject("categoryDto").getInt("id"),
                    jsonObject.getJSONObject("categoryDto").getString("name")
            );
        }catch (JSONException e){e.printStackTrace();}

        return category;
    }
}
