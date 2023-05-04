package com.codunite.model;

public class CCHistoryModel {
    private String str_amount, str_description,
            str_before_balance, str_type, strDateTime;

    public CCHistoryModel(String str_amount, String str_description,
                          String str_before_balance, String str_type, String strDateTime) {
        this.str_amount = str_amount;
        this.str_description = str_description;
        this.str_before_balance = str_before_balance;
        this.str_type = str_type;
        this.strDateTime =strDateTime;
    }

    public String getStrDateTime() {
        return strDateTime;
    }

    public void setStrDateTime(String strDateTime) {
        this.strDateTime = strDateTime;
    }

    public String getStr_amount() {
        return str_amount;
    }

    public void setStr_amount(String str_amount) {
        this.str_amount = str_amount;
    }

    public String getStr_description() {
        return str_description;
    }

    public void setStr_description(String str_description) {
        this.str_description = str_description;
    }

    public String getStr_before_balance() {
        return str_before_balance;
    }

    public void setStr_before_balance(String str_before_balance) {
        this.str_before_balance = str_before_balance;
    }

    public String getStr_type() {
        return str_type;
    }

    public void setStr_type(String str_type) {
        this.str_type = str_type;
    }
}
