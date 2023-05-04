package com.codunite.model;

public class LoanRequestListModel {
    private String str_name,str_DateTime,str_status,str_mobile,str_email,str_pancardno,str_loanamount,str_tenure;

    public LoanRequestListModel(String str_name, String str_DateTime, String str_status, String str_mobile, String str_email, String str_pancardno, String str_loanamount, String str_tenure) {
        this.str_name = str_name;
        this.str_DateTime = str_DateTime;
        this.str_status = str_status;
        this.str_mobile = str_mobile;
        this.str_email = str_email;
        this.str_pancardno = str_pancardno;
        this.str_loanamount = str_loanamount;
        this.str_tenure = str_tenure;
    }

    public String getStr_name() {
        return str_name;
    }

    public void setStr_name(String str_name) {
        this.str_name = str_name;
    }

    public String getStr_DateTime() {
        return str_DateTime;
    }

    public void setStr_DateTime(String str_DateTime) {
        this.str_DateTime = str_DateTime;
    }

    public String getStr_status() {
        return str_status;
    }

    public void setStr_status(String str_status) {
        this.str_status = str_status;
    }

    public String getStr_mobile() {
        return str_mobile;
    }

    public void setStr_mobile(String str_mobile) {
        this.str_mobile = str_mobile;
    }

    public String getStr_email() {
        return str_email;
    }

    public void setStr_email(String str_email) {
        this.str_email = str_email;
    }

    public String getStr_pancardno() {
        return str_pancardno;
    }

    public void setStr_pancardno(String str_pancardno) {
        this.str_pancardno = str_pancardno;
    }

    public String getStr_loanamount() {
        return str_loanamount;
    }

    public void setStr_loanamount(String str_loanamount) {
        this.str_loanamount = str_loanamount;
    }

    public String getStr_tenure() {
        return str_tenure;
    }

    public void setStr_tenure(String str_tenure) {
        this.str_tenure = str_tenure;
    }
}
