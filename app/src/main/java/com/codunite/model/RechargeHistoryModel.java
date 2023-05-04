package com.codunite.model;

public class RechargeHistoryModel {
    private String str_recharge_id, str_order_id, str_amount, str_datetime,
            str_status, operator, mobile="", type, memberDetail, beforeBalance, afterBalance,
            txtId,str_account_number="", operatorTranId = "";

    public String getStr_account_number() {
        return str_account_number;
    }

    public void setStr_account_number(String str_account_number) {
        this.str_account_number = str_account_number;
    }

    public RechargeHistoryModel(String str_recharge_id, String memberDetail, String str_order_id, String str_amount, String str_datetime, String str_status,
                                String operator, String mobile, String type, String beforeBalance, String afterBalance,
                                String txtId, String account_number, String operatorTranId) {
        this.str_recharge_id = str_recharge_id;
        this.str_account_number=account_number;
        this.memberDetail = memberDetail;
        this.str_order_id = str_order_id;
        this.str_amount = str_amount;
        this.str_datetime = str_datetime;
        this.str_status = str_status;
        this.operator = operator;
        this.mobile = mobile;
        this.type = type;
        this.beforeBalance = beforeBalance;
        this.afterBalance = afterBalance;
        this.txtId = txtId;
        this.operatorTranId = operatorTranId;
    }
    public RechargeHistoryModel(String str_recharge_id, String memberDetail, String str_order_id, String str_amount, String str_datetime, String str_status,
                                String operator, String mobile, String type, String beforeBalance, String afterBalance,
                                String txtId, String operatorTranId) {
        this.str_recharge_id = str_recharge_id;
        this.memberDetail = memberDetail;
        this.str_order_id = str_order_id;
        this.str_amount = str_amount;
        this.str_datetime = str_datetime;
        this.str_status = str_status;
        this.operator = operator;
        this.mobile = mobile;
        this.type = type;
        this.beforeBalance = beforeBalance;
        this.afterBalance = afterBalance;
        this.txtId = txtId;
        this.operatorTranId = operatorTranId;
    }

    public String getStr_recharge_id() {
        return str_recharge_id;
    }

    public void setStr_recharge_id(String str_recharge_id) {
        this.str_recharge_id = str_recharge_id;
    }

    public String getTxtId() {
        return txtId;
    }

    public void setTxtId(String txtId) {
        this.txtId = txtId;
    }

    public String getBeforeBalance() {
        return beforeBalance;
    }

    public void setBeforeBalance(String beforeBalance) {
        this.beforeBalance = beforeBalance;
    }

    public String getAfterBalance() {
        return afterBalance;
    }

    public void setAfterBalance(String afterBalance) {
        this.afterBalance = afterBalance;
    }

    public String getMemberDetail() {
        return memberDetail;
    }

    public String getOperatorTranId() {
        return operatorTranId;
    }

    public void setOperatorTranId(String operatorTranId) {
        this.operatorTranId = operatorTranId;
    }

    public void setMemberDetail(String memberDetail) {
        this.memberDetail = memberDetail;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStr_order_id() {
        return str_order_id;
    }

    public void setStr_order_id(String str_order_id) {
        this.str_order_id = str_order_id;
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
}
