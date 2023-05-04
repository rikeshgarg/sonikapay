package com.codunite.model;

public class MoenyTransferHistoryModel {
    private String str_member_id, str_txn_id, str_amount, str_datetime,
            str_status, str_rrn, mobile,str_name,str_ifsc;

    public MoenyTransferHistoryModel(String str_member_id, String str_txn_id, String str_amount,
                                     String str_datetime, String str_status, String str_rrn, String mobile, String str_name, String str_ifsc) {
        this.str_member_id = str_member_id;
        this.str_txn_id = str_txn_id;
        this.str_amount = str_amount;
        this.str_datetime = str_datetime;
        this.str_status = str_status;
        this.str_rrn = str_rrn;
        this.mobile = mobile;
        this.str_name = str_name;
        this.str_ifsc = str_ifsc;
    }

    public String getStr_member_id() {
        return str_member_id;
    }

    public void setStr_member_id(String str_member_id) {
        this.str_member_id = str_member_id;
    }

    public String getStr_txn_id() {
        return str_txn_id;
    }

    public void setStr_txn_id(String str_txn_id) {
        this.str_txn_id = str_txn_id;
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

    public String getStr_status() {
        return str_status;
    }

    public void setStr_status(String str_status) {
        this.str_status = str_status;
    }

    public String getStr_rrn() {
        return str_rrn;
    }

    public void setStr_rrn(String str_rrn) {
        this.str_rrn = str_rrn;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getStr_name() {
        return str_name;
    }

    public void setStr_name(String str_name) {
        this.str_name = str_name;
    }


    public String getStr_ifsc() {
        return str_ifsc;
    }

    public void setStr_ifsc(String str_ifsc) {
        this.str_ifsc = str_ifsc;
    }
}
