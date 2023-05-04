package com.codunite.model;

public class SliderModel {
    String banner_id, banner_name = "", banner_url, banner_desc = "", banner_img;
    public boolean expanded = false;

    public SliderModel(String banner_id, String banner_name, String banner_url, String banner_desc, String banner_img) {
        this.banner_id = banner_id;
        this.banner_name = banner_name;
        this.banner_url = banner_url;
        this.banner_desc = banner_desc;
        this.banner_img = banner_img;
        this.expanded = expanded;
    }

    public SliderModel(String banner_id, String banner_url, String banner_img) {
        this.banner_id = banner_id;
        this.banner_url = banner_url;
        this.banner_img = banner_img;
    }

    public String getBanner_id() {
        return banner_id;
    }

    public void setBanner_id(String banner_id) {
        this.banner_id = banner_id;
    }

    public String getBanner_name() {
        return banner_name;
    }

    public void setBanner_name(String banner_name) {
        this.banner_name = banner_name;
    }

    public String getBanner_url() {
        return banner_url;
    }

    public void setBanner_url(String banner_url) {
        this.banner_url = banner_url;
    }

    public String getBanner_desc() {
        return banner_desc;
    }

    public void setBanner_desc(String banner_desc) {
        this.banner_desc = banner_desc;
    }

    public String getBanner_img() {
        return banner_img;
    }

    public void setBanner_img(String banner_img) {
        this.banner_img = banner_img;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
}
