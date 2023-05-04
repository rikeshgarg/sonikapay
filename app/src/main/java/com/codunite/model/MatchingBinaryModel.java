package com.codunite.model;

public class MatchingBinaryModel {

    private String str_is_paid, str_date, str_tds_amount, str_binary_amount,
            str_wallet_settle_amount, str_income_amount;

    public MatchingBinaryModel(String str_is_paid, String str_date, String str_tds_amount, String str_binary_amount,
                               String str_wallet_settle_amount, String str_income_amount) {
        this.str_is_paid = str_is_paid;
        this.str_date = str_date;
        this.str_tds_amount = str_tds_amount;
        this.str_binary_amount = str_binary_amount;
        this.str_wallet_settle_amount = str_wallet_settle_amount;
        this.str_income_amount = str_income_amount;
    }

    public String getStr_is_paid() {
        return str_is_paid;
    }

    public void setStr_is_paid(String str_is_paid) {
        this.str_is_paid = str_is_paid;
    }

    public String getStr_date() {
        return str_date;
    }

    public void setStr_date(String str_date) {
        this.str_date = str_date;
    }

    public String getStr_tds_amount() {
        return str_tds_amount;
    }

    public void setStr_tds_amount(String str_tds_amount) {
        this.str_tds_amount = str_tds_amount;
    }

    public String getStr_binary_amount() {
        return str_binary_amount;
    }

    public void setStr_binary_amount(String str_binary_amount) {
        this.str_binary_amount = str_binary_amount;
    }

    public String getStr_wallet_settle_amount() {
        return str_wallet_settle_amount;
    }

    public void setStr_wallet_settle_amount(String str_wallet_settle_amount) {
        this.str_wallet_settle_amount = str_wallet_settle_amount;
    }

    public String getStr_income_amount() {
        return str_income_amount;
    }

    public void setStr_income_amount(String str_income_amount) {
        this.str_income_amount = str_income_amount;
    }
}
