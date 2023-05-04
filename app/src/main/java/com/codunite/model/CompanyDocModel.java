package com.codunite.model;

public class CompanyDocModel {

    private String strId, strTitle, strUrl;

    public CompanyDocModel(String strId, String strTitle, String strUrl) {
        this.strId = strId;
        this.strTitle = strTitle;
        this.strUrl = strUrl;
    }

    public String getStrId() {
        return strId;
    }

    public void setStrId(String strId) {
        this.strId = strId;
    }

    public String getStrTitle() {
        return strTitle;
    }

    public void setStrTitle(String strTitle) {
        this.strTitle = strTitle;
    }

    public String getStrUrl() {
        return strUrl;
    }

    public void setStrUrl(String strUrl) {
        this.strUrl = strUrl;
    }

}