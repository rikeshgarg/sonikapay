package com.codunite.model;

public class DownLineModel {
    private String str_memberID, str_name, str_mobile, str_membership, str_email,
            str_user_code, str_level;

    public DownLineModel(String str_memberID, String str_name, String str_mobile,
                         String str_membership, String str_email, String str_user_code, String str_level) {
        this.str_memberID = str_memberID;
        this.str_name = str_name;
        this.str_mobile = str_mobile;
        this.str_membership = str_membership;
        this.str_email = str_email;
        this.str_user_code = str_user_code;
        this.str_level = str_level;
    }

    public String getStr_memberID() {
        return str_memberID;
    }

    public void setStr_memberID(String str_memberID) {
        this.str_memberID = str_memberID;
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

    public String getStr_membership() {
        return str_membership;
    }

    public void setStr_membership(String str_membership) {
        this.str_membership = str_membership;
    }

    public String getStr_email() {
        return str_email;
    }

    public void setStr_email(String str_email) {
        this.str_email = str_email;
    }

    public String getStr_user_code() {
        return str_user_code;
    }

    public void setStr_user_code(String str_user_code) {
        this.str_user_code = str_user_code;
    }

    public String getStr_level() {
        return str_level;
    }

    public void setStr_level(String str_level) {
        this.str_level = str_level;
    }
}
