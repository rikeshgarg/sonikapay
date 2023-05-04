package com.codunite.model;

public class AEPSBeneficiaryListModel {
    private String str_name, str_account_no, str_ifsc, str_bank_name, str_benId, str_benMob, str_status, str_otp_verify, str_date;


    public AEPSBeneficiaryListModel(String str_name, String str_account_no, String str_ifsc, String str_bank_name,
                                    String str_benId, String str_benMob, String str_status, String str_otp_verify, String str_date) {
        this.str_name = str_name;
        this.str_account_no = str_account_no;
        this.str_ifsc = str_ifsc;
        this.str_bank_name = str_bank_name;
        this.str_benId = str_benId;
        this.str_benMob = str_benMob;
        this.str_status = str_status;
        this.str_otp_verify = str_otp_verify;
        this.str_date = str_date;
    }

    public String getStr_name() {
        return str_name;
    }

    public void setStr_name(String str_name) {
        this.str_name = str_name;
    }

    public String getStr_account_no() {
        return str_account_no;
    }

    public void setStr_account_no(String str_account_no) {
        this.str_account_no = str_account_no;
    }

    public String getStr_ifsc() {
        return str_ifsc;
    }

    public void setStr_ifsc(String str_ifsc) {
        this.str_ifsc = str_ifsc;
    }

    public String getStr_bank_name() {
        return str_bank_name;
    }

    public void setStr_bank_name(String str_bank_name) {
        this.str_bank_name = str_bank_name;
    }

    public String getStr_benId() {
        return str_benId;
    }

    public void setStr_benId(String str_benId) {
        this.str_benId = str_benId;
    }

    public String getStr_benMob() {
        return str_benMob;
    }

    public void setStr_benMob(String str_benMob) {
        this.str_benMob = str_benMob;
    }

    public String getStr_status() {
        return str_status;
    }

    public void setStr_status(String str_status) {
        this.str_status = str_status;
    }

    public String getStr_otp_verify() {
        return str_otp_verify;
    }

    public void setStr_otp_verify(String str_otp_verify) {
        this.str_otp_verify = str_otp_verify;
    }

    public String getStr_date() {
        return str_date;
    }

    public void setStr_date(String str_date) {
        this.str_date = str_date;
    }
}


