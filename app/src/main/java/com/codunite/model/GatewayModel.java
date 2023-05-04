package com.codunite.model;

public class GatewayModel {
    private String order_id, amount, status, datetime;

    public GatewayModel(String order_id, String amount, String status, String datetime) {
        this.order_id = order_id;
        this.amount = amount;
        this.status = status;
        this.datetime = datetime;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}


