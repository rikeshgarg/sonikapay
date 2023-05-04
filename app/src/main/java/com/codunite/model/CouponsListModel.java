package com.codunite.model;

public class CouponsListModel {

    private String strloginid, strcoupon, strdate;

    public CouponsListModel(String strloginid, String strcoupon, String strdate) {
        this.strloginid = strloginid;
        this.strcoupon = strcoupon;
        this.strdate = strdate;
    }

    public String getStrloginid() {
        return strloginid;
    }

    public void setStrloginid(String strloginid) {
        this.strloginid = strloginid;
    }

    public String getStrcoupon() {
        return strcoupon;
    }

    public void setStrcoupon(String strcoupon) {
        this.strcoupon = strcoupon;
    }

    public String getStrdate() {
        return strdate;
    }

    public void setStrdate(String strdate) {
        this.strdate = strdate;
    }
}
