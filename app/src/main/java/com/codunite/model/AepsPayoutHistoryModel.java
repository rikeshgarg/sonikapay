package com.codunite.model;

public class AepsPayoutHistoryModel {

    private String memberId;
    private String accountHolderName;
    private String dateTime;
    private String mobile;
    private String ifscCode;
    private String transferAmount;
    private String transactionId;
    private String rrn;
    private String status;

    public AepsPayoutHistoryModel(String memberId, String accountHolderName, String dateTime, String mobile, String ifscCode, String transferAmount, String transactionId, String rrn, String status) {
        this.memberId = memberId;
        this.accountHolderName = accountHolderName;
        this.dateTime = dateTime;
        this.mobile = mobile;
        this.ifscCode = ifscCode;
        this.transferAmount = transferAmount;
        this.transactionId = transactionId;
        this.rrn = rrn;
        this.status = status;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public String getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(String transferAmount) {
        this.transferAmount = transferAmount;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}