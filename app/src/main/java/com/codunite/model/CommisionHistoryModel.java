package com.codunite.model;

public class CommisionHistoryModel {
    private String str_amount, str_datetime, str_description;

    public CommisionHistoryModel(String str_amount, String str_datetime, String str_description) {
        this.str_amount = str_amount;
        this.str_datetime = str_datetime;
        this.str_description = str_description;
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
}
