package com.codunite.model;

public class DashboardAffilateModel {
    private String name;
    private int drawble;
    private String imgUrl;
    private String webUrl;

    public DashboardAffilateModel(String name, int drawble, String webUrl) {
        this.name = name;
        this.drawble = drawble;
        this.webUrl = webUrl;
    }

    public DashboardAffilateModel(String name, String imgUrl, String webUrl) {
        this.name = name;
        this.imgUrl = imgUrl;
        this.webUrl = webUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDrawble() {
        return drawble;
    }

    public void setDrawble(int drawble) {
        this.drawble = drawble;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

}
