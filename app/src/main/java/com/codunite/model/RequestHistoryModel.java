package com.codunite.model;

public class RequestHistoryModel {
    private String str_amount, str_datetime, str_requestamount, request_id,
            txnid, status;

    public RequestHistoryModel(String str_amount, String str_datetime, String str_requestamount,
                               String request_id, String txnid, String status) {
        this.str_amount = str_amount;
        this.str_datetime = str_datetime;
        this.str_requestamount = str_requestamount;
        this.request_id = request_id;
        this.txnid = txnid;
        this.status = status;
    }

    public String getStr_amount() {
        return str_amount;
    }

    public String getStr_datetime() {
        return str_datetime;
    }

    public String getStr_requestamount() {
        return str_requestamount;
    }

    public String getRequest_id() {
        return request_id;
    }

    public String getTxnid() {
        return txnid;
    }

    public String getStatus() {
        return status;
    }
}
