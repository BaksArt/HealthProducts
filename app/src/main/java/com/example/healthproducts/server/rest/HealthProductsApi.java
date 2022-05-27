package com.example.healthproducts.server.rest;

import com.example.healthproducts.server.domain.Product;

public interface HealthProductsApi {
    void fillProduct();

    void fillCategory();

    void addProduct(Product product);

    void updateProduct(
            int id,
            String newProductName,
            String newCode,
            String newCategoryName,
            String newComposition,
            String newFoto
    );


    Product findProductByCode(String code);

    void deleteProduct(int id);

}
