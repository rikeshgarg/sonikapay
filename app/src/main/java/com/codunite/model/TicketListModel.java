package com.codunite.model;

public class TicketListModel {
    private String str_ticket_id, str_message, str_subject,
            str_datetime, str_type, str_data_status;
    private String imgUrl = "";
    public TicketListModel(String str_ticket_id, String str_message, String str_subject, String str_datetime,
                           String str_type, String str_data_status) {
        this.str_ticket_id = str_ticket_id;
        this.str_subject = str_subject;
        this.str_message = str_message;
        this.str_datetime = str_datetime;
        this.str_type = str_type;
        this.str_data_status = str_data_status;
        this.imgUrl = "";
    }

    public TicketListModel(String str_ticket_id, String str_message, String str_subject, String str_datetime,
                           String str_type, String str_data_status, String imgUrl) {
        this.str_ticket_id = str_ticket_id;
        this.str_subject = str_subject;
        this.str_message = str_message;
        this.str_datetime = str_datetime;
        this.str_type = str_type;
        this.str_data_status = str_data_status;
        this.imgUrl = imgUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getStr_message() {
        return str_message;
    }

    public String getStr_ticket_id() {
        return str_ticket_id;
    }

    public void setStr_ticket_id(String str_ticket_id) {
        this.str_ticket_id = str_ticket_id;
    }

    public String getStr_subject() {
        return str_subject;
    }

    public void setStr_subject(String str_subject) {
        this.str_subject = str_subject;
    }

    public String getStr_datetime() {
        return str_datetime;
    }

    public void setStr_datetime(String str_datetime) {
        this.str_datetime = str_datetime;
    }

    public String getStr_type() {
        return str_type;
    }

    public void setStr_type(String str_type) {
        this.str_type = str_type;
    }

    public String getStr_data_status() {
        return str_data_status;
    }

    public void setStr_data_status(String str_data_status) {
        this.str_data_status = str_data_status;
    }
}
