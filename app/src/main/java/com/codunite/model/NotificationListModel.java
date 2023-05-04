package com.codunite.model;

public class NotificationListModel {

    private String strTitle, strDesc, imgUrl;

    public NotificationListModel(String strTitle, String strDesc) {
        this.strTitle = strTitle;
        this.strDesc = strDesc;
        this.imgUrl = "";
    }

    public NotificationListModel(String strTitle, String strDesc, String imgUrl) {
        this.strTitle = strTitle;
        this.strDesc = strDesc;
        this.imgUrl = imgUrl;
    }

    public String getStrTitle() {
        return strTitle;
    }

    public void setStrTitle(String strTitle) {
        this.strTitle = strTitle;
    }

    public String getStrDesc() {
        return strDesc;
    }

    public void setStrDesc(String strDesc) {
        this.strDesc = strDesc;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

}
