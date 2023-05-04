package com.codunite.model;

public class FundRequestHistoryModel {
    private String memberDeatil, requestId, txnID, str_status,
            amount, payment_screen, datetime, bank_name;

    public FundRequestHistoryModel(String memberDeatil, String service, String txnID, String str_status, String amount, String payment_screen, String datetime, String bank_name) {
        this.memberDeatil = memberDeatil;
        this.requestId = service;
        this.txnID = txnID;
        this.str_status = str_status;
        this.amount = amount;
        this.payment_screen = payment_screen;
        this.datetime = datetime;
        this.bank_name = bank_name;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getMemberDeatil() {
        return memberDeatil;
    }

    public void setMemberDeatil(String memberDeatil) {
        this.memberDeatil = memberDeatil;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getTxnID() {
        return txnID;
    }

    public void setTxnID(String txnID) {
        this.txnID = txnID;
    }

    public String getStr_status() {
        return str_status;
    }

    public void setStr_status(String str_status) {
        this.str_status = str_status;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPayment_screen() {
        return payment_screen;
    }

    public void setPayment_screen(String payment_screen) {
        this.payment_screen = payment_screen;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
