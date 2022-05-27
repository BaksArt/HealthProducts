package com.example.healthproducts.server.dataBase;

import com.example.healthproducts.server.domain.Category;
import com.example.healthproducts.server.domain.Product;

import java.util.ArrayList;
import java.util.List;

public class DataBase {

    public static final List<Product> PRODUCT_LIST = new ArrayList<>();
    public static final List<Category> CATEGORY_LIST = new ArrayList<>();
    public static final List<String> CATEGORY_NAMES_LIST = new ArrayList<>();
    public static final List<String> DANGER_INGREDIENTS = new ArrayList<>();
}
