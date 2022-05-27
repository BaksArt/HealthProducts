package com.example.healthproducts.server.domain;

import java.io.Serializable;
import java.util.List;

public class Product implements Serializable {
    private int id;

    private String name;

    private String code;

    private Category category;

    private String composition;

    private String foto;

    public Product(int id, String name, String code, Category category, String composition, String foto) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.category = category;
        this.composition = composition;
        this.foto = foto;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", category=" + category +
                ", composition='" + composition + '\'' +
                ", foto='" + foto + '\'' +
                '}';
    }

    public Product(String name, String code, Category category, String composition, String foto) {
        this.name = name;
        this.code = code;
        this.category = category;
        this.composition = composition;
        this.foto = foto;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }
    public String getFoto() {
        return foto;
    }

    public Category getCategory() {
        return category;
    }

    public String getComposition() {
        return composition;
    }
}