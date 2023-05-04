package com.codunite.model;

public class WalletHistoryModel {
    private String str_amount, str_datetime, str_description, str_type,str_openbal,str_close_bal;

    public String getStr_openbal() {
        return str_openbal;
    }

    public void setStr_openbal(String str_openbal) {
        this.str_openbal = str_openbal;
    }

    public String getStr_close_bal() {
        return str_close_bal;
    }

    public void setStr_close_bal(String str_close_bal) {
        this.str_close_bal = str_close_bal;
    }

    public WalletHistoryModel(String str_amount, String str_datetime, String str_description, String str_type, String str_openbal, String str_close_bal) {
        this.str_amount = str_amount;
        this.str_datetime = str_datetime;
        this.str_description = str_description;
        this.str_type = str_type;
        this.str_openbal = str_openbal;
        this.str_close_bal = str_close_bal;
    }

    public WalletHistoryModel(String str_amount, String str_datetime, String str_description, String str_type) {
        this.str_amount = str_amount;
        this.str_datetime = str_datetime;
        this.str_description = str_description;
        this.str_type = str_type;
    }

    public String getStr_amount() {
        return str_amount;
    }

    public void setStr_amount(String str_amount) {
        this.str_amount = str_amount;
    }

    public String getStr_datetime() {
        return str_datetime;
    }

    public void setStr_datetime(String str_datetime) {
        this.str_datetime = str_datetime;
    }

    public String getStr_description() {
        return str_description;
    }

    public void setStr_description(String str_description) {
        this.str_description = str_description;
    }

    public String getStr_type() {
        return str_type;
    }

    public void setStr_type(String str_type) {
        this.str_type = str_type;
    }
}
