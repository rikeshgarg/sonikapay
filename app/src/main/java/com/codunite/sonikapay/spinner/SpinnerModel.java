package com.codunite.sonikapay.spinner;

import java.io.Serializable;

/**
 * Created by ssridharatiwari on 2021.
 */

public class SpinnerModel implements Serializable {
    String id;
    String title;
    String desc;
    String image;
    String strImgUrl;


    // exrtas
    public String getStrBillerAliasName() {
        return strBillerAliasName;
    }

    public void setStrBillerAliasName(String strBillerAliasName) {
        this.strBillerAliasName = strBillerAliasName;
    }

    public String getIsFetch() {
        return isFetch;
    }

    public void setIsFetch(String isFetch) {
        this.isFetch = isFetch;
    }

    String strBillerAliasName;
    String isFetch;


    public SpinnerModel(String id, String title, String strBillerAliasName, String isFetch) {
        this.id = id;
        this.title = title;
        this.desc = "";
        this.image = "";
        this.strImgUrl = "";
        this.strBillerAliasName = strBillerAliasName;
        this.isFetch = isFetch;
    }

    public SpinnerModel(String id, String title, String strImgUrl) {
        this.id = id;
        this.title = title;
        this.desc = "";
        this.image = "";
        this.strImgUrl = strImgUrl;
        this.strBillerAliasName = "";
        this.isFetch = "";
    }

    public SpinnerModel(String id, String title, String desc, String image, String strImgUrl) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.image = image;
        this.strImgUrl = strImgUrl;
        this.strBillerAliasName = "";
        this.isFetch = "";
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getDesc() {
        return desc;
    }

    public String getStrImgUrl() {
        return strImgUrl;
    }

}