package com.codunite.model;

public class LevelIncomeModel {

    private String str_date, str_tds_amount, str_by_member, str_level_num,
            str_wallet_settle_amount, str_is_paid, service_tax_amount;

    public LevelIncomeModel(String str_date, String str_tds_amount, String str_by_member, String str_level_num,
                            String str_wallet_settle_amount, String str_is_paid, String service_tax_amount) {
        this.str_date = str_date;
        this.str_tds_amount = str_tds_amount;
        this.str_by_member = str_by_member;
        this.str_level_num = str_level_num;
        this.str_wallet_settle_amount = str_wallet_settle_amount;
        this.str_is_paid = str_is_paid;
        this.service_tax_amount = service_tax_amount;
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

    public String getStr_level_num() {
        return str_level_num;
    }

    public void setStr_level_num(String str_level_num) {
        this.str_level_num = str_level_num;
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

    public String getService_tax_amount() {
        return service_tax_amount;
    }

    public void setService_tax_amount(String service_tax_amount) {
        this.service_tax_amount = service_tax_amount;
    }
}
