package com.codunite.model;

public class MtransferHistoryModel {
    private String str_date, str_txn_id, str_benificiary, str_transfer_amount,
            str_transfer_status, str_rrn;

    public MtransferHistoryModel(String str_date, String str_txn_id, String str_benificiary, String str_transfer_amount, String str_transfer_status, String str_rrn) {
        this.str_date = str_date;
        this.str_txn_id = str_txn_id;
        this.str_benificiary = str_benificiary;
        this.str_transfer_amount = str_transfer_amount;
        this.str_transfer_status = str_transfer_status;
        this.str_rrn = str_rrn;
    }

    public String getStr_date() {
        return str_date;
    }

    public void setStr_date(String str_date) {
        this.str_date = str_date;
    }

    public String getStr_txn_id() {
        return str_txn_id;
    }

    public void setStr_txn_id(String str_txn_id) {
        this.str_txn_id = str_txn_id;
    }

    public String getStr_benificiary() {
        return str_benificiary;
    }

    public void setStr_benificiary(String str_benificiary) {
        this.str_benificiary = str_benificiary;
    }

    public String getStr_transfer_amount() {
        return str_transfer_amount;
    }

    public void setStr_transfer_amount(String str_transfer_amount) {
        this.str_transfer_amount = str_transfer_amount;
    }

    public String getStr_transfer_status() {
        return str_transfer_status;
    }

    public void setStr_transfer_status(String str_transfer_status) {
        this.str_transfer_status = str_transfer_status;
    }

    public String getStr_rrn() {
        return str_rrn;
    }

    public void setStr_rrn(String str_rrn) {
        this.str_rrn = str_rrn;
    }
}
