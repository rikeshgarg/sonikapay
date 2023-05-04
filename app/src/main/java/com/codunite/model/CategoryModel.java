package com.codunite.model;

public class CategoryModel {
    private String str_catID, str_category_icon, str_title, str_slug;

    public CategoryModel(String str_catID, String str_category_icon, String str_title, String str_slug) {
        this.str_catID = str_catID;
        this.str_category_icon = str_category_icon;
        this.str_title = str_title;
        this.str_slug = str_slug;
    }

    public String getStr_catID() {
        return str_catID;
    }

    public void setStr_catID(String str_catID) {
        this.str_catID = str_catID;
    }

    public String getStr_category_icon() {
        return str_category_icon;
    }

    public void setStr_category_icon(String str_category_icon) {
        this.str_category_icon = str_category_icon;
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
