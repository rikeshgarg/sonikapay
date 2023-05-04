package com.codunite.model;

public class DirectIncomeModel {

    private String str_date, str_tds_amount, str_by_member, str_direct_amount,
            str_wallet_settle_amount, str_is_paid;

    public DirectIncomeModel(String str_date, String str_tds_amount, String str_by_member, String str_direct_amount,
                             String str_wallet_settle_amount, String str_is_paid) {
        this.str_date = str_date;
        this.str_tds_amount = str_tds_amount;
        this.str_by_member = str_by_member;
        this.str_direct_amount = str_direct_amount;
        this.str_wallet_settle_amount = str_wallet_settle_amount;
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

    public String getStr_by_member() {
        return str_by_member;
    }

    public void setStr_by_member(String str_by_member) {
        this.str_by_member = str_by_member;
    }

    public String getStr_direct_amount() {
        return str_direct_amount;
    }

    public void setStr_direct_amount(String str_direct_amount) {
        this.str_direct_amount = str_direct_amount;
    }

    public String getStr_wallet_settle_amount() {
        return str_wallet_settle_amount;
    }

    public void setStr_wallet_settle_amount(String str_wallet_settle_amount) {
        this.str_wallet_settle_amount = str_wallet_settle_amount;
    }

    public String getStr_is_paid() {
        return str_is_paid;
    }

    public void setStr_is_paid(String str_is_paid) {
        this.str_is_paid = str_is_paid;
    }
}
