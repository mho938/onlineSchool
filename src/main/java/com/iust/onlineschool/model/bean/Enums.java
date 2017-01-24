package com.iust.onlineschool.model.bean;

/**
 * Created by Mohsen on 1/22/2017.
 */
public class Enums {
    private String  value;
    private int id;

    public Enums(String value, int id) {
        this.value = value;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
