package com.codunite.model;

public class CCLoanInstallmentModel {
    private String str_emi_num,
            str_total_emi_amount, str_emi_id,
            str_paid_status, str_emi_amount, str_due_amount,
            str_emi_date, str_is_paid;

    public CCLoanInstallmentModel(String str_emi_num, String str_total_emi_amount,
                                  String str_emi_id, String str_paid_status,
                                  String str_emi_amount, String str_due_amount,
                                  String str_emi_date, String str_is_paid) {
        this.str_emi_num = str_emi_num;
        this.str_total_emi_amount = str_total_emi_amount;
        this.str_emi_id = str_emi_id;
        this.str_paid_status = str_paid_status;
        this.str_emi_amount = str_emi_amount;
        this.str_due_amount = str_due_amount;
        this.str_emi_date = str_emi_date;
        this.str_is_paid = str_is_paid;
    }

    public String getStr_emi_num() {
        return str_emi_num;
    }

    public void setStr_emi_num(String str_emi_num) {
        this.str_emi_num = str_emi_num;
    }

    public String getStr_total_emi_amount() {
        return str_total_emi_amount;
    }

    public void setStr_total_emi_amount(String str_total_emi_amount) {
        this.str_total_emi_amount = str_total_emi_amount;
    }

    public String getStr_emi_id() {
        return str_emi_id;
    }

    public void setStr_emi_id(String str_emi_id) {
        this.str_emi_id = str_emi_id;
    }

    public String getStr_paid_status() {
        return str_paid_status;
    }

    public void setStr_paid_status(String str_paid_status) {
        this.str_paid_status = str_paid_status;
    }

    public String getStr_emi_amount() {
        return str_emi_amount;
    }

    public void setStr_emi_amount(String str_emi_amount) {
        this.str_emi_amount = str_emi_amount;
    }

    public String getStr_due_amount() {
        return str_due_amount;
    }

    public void setStr_due_amount(String str_due_amount) {
        this.str_due_amount = str_due_amount;
    }

    public String getStr_emi_date() {
        return str_emi_date;
    }

    public void setStr_emi_date(String str_emi_date) {
        this.str_emi_date = str_emi_date;
    }

    public String getStr_is_paid() {
        return str_is_paid;
    }

    public void setStr_is_paid(String str_is_paid) {
        this.str_is_paid = str_is_paid;
    }
}
