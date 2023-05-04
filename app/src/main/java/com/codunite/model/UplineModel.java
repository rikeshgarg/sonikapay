package com.codunite.model;

public class UplineModel {

    private String str_usercode,str_name,str_mobile;

    public UplineModel(String str_usercode, String str_name, String str_mobile) {
        this.str_usercode = str_usercode;
        this.str_name = str_name;
        this.str_mobile = str_mobile;
    }

    public String getStr_usercode() {
        return str_usercode;
    }

    public void setStr_usercode(String str_usercode) {
        this.str_usercode = str_usercode;
    }

    public String getStr_name() {
        return str_name;
    }

    public void setStr_name(String str_name) {
        this.str_name = str_name;
    }

    public String getStr_mobile() {
        return str_mobile;
    }

    public void setStr_mobile(String str_mobile) {
        this.str_mobile = str_mobile;
    }
}
