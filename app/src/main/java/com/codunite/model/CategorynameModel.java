package com.codunite.model;

public class CategorynameModel {
    private String str_catId,str_title,str_slug;

    public CategorynameModel(String str_catId, String str_title, String str_slug) {
        this.str_catId = str_catId;
        this.str_title = str_title;
        this.str_slug = str_slug;
    }

    public String getStr_catId() {
        return str_catId;
    }

    public void setStr_catId(String str_catId) {
        this.str_catId = str_catId;
    }

    public String getStr_title() {
        return str_title;
    }

    public void setStr_title(String str_title) {
        this.str_title = str_title;
    }

    public String getStr_slug() {
        return str_slug;
    }

    public void setStr_slug(String str_slug) {
        this.str_slug = str_slug;
    }
}
