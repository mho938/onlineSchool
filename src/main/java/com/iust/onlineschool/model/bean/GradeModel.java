package com.iust.onlineschool.model.bean;

/**
 * Created by Mohsen on 12/25/2016.
 */
public class GradeModel {
    private int id;
    private String text;

    public GradeModel(String text, int id) {
        this.text = text;
        this.id = id;
    }

    public String getText() {

        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
