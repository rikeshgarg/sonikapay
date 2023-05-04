package com.codunite.model;

public class SliderItem {
    private String id, name, desc, imgUrl;
    private int imgDrawable;

    public SliderItem(String id, String name, String desc, String imgUrl) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.imgUrl = imgUrl;
    }

    public SliderItem(String id, String name, String desc, int imgDrawable) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.imgDrawable = imgDrawable;
    }

    public int getImgDrawable() {
        return imgDrawable;
    }

    public void setImgDrawable(int imgDrawable) {
        this.imgDrawable = imgDrawable;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
