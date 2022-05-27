package com.example.healthproducts.server.domain;

import java.io.Serializable;

public class Category implements Serializable {
    private int id;

    private String name;

    public Category(int id, String name){
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public Category(String name) {
        this.name = name;
    }

    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }
}
