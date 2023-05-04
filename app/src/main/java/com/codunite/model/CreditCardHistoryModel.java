package com.codunite.model;

public class CreditCardHistoryModel {
    private String str_loan_status, str_installment_amount, str_loan_display_id,
            str_loan_amount, str_installment_duration,str_loan_status_no,
            str_loan_id;

    public CreditCardHistoryModel(String str_loan_status, String str_installment_amount,
                                  String str_loan_display_id, String str_loan_amount,
                                  String str_installment_duration,
                                  String str_loan_status_no, String str_loan_id) {
        this.str_loan_status = str_loan_status;
        this.str_installment_amount = str_installment_amount;
        this.str_loan_display_id = str_loan_display_id;
        this.str_loan_amount = str_loan_amount;
        this.str_installment_duration = str_installment_duration;
        this.str_loan_status_no = str_loan_status_no;
        this.str_loan_id = str_loan_id;
    }

    public String getStr_loan_id() {
        return str_loan_id;
    }

    public void setStr_loan_id(String str_loan_id) {
        this.str_loan_id = str_loan_id;
    }

    public String getStr_loan_status() {
        return str_loan_status;
    }

    public void setStr_loan_status(String str_loan_status) {
        this.str_loan_status = str_loan_status;
    }

    public String getStr_installment_amount() {
        return str_installment_amount;
    }

    public void setStr_installment_amount(String str_installment_amount) {
        this.str_installment_amount = str_installment_amount;
    }

    public String getStr_loan_display_id() {
        return str_loan_display_id;
    }

    public void setStr_loan_display_id(String str_loan_display_id) {
        this.str_loan_display_id = str_loan_display_id;
    }

    public String getStr_loan_amount() {
        return str_loan_amount;
    }

    public void setStr_loan_amount(String str_loan_amount) {
        this.str_loan_amount = str_loan_amount;
    }

    public String getStr_installment_duration() {
        return str_installment_duration;
    }

    public void setStr_installment_duration(String str_installment_duration) {
        this.str_installment_duration = str_installment_duration;
    }

    public String getStr_loan_status_no() {
        return str_loan_status_no;
    }

    public void setStr_loan_status_no(String str_loan_status_no) {
        this.str_loan_status_no = str_loan_status_no;
    }
}
