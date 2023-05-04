package com.codunite.model;

public class PinHistoryModel {
    private String package_name, package_amount, token,
            is_used, used_by, used_date;

    public PinHistoryModel(String package_name, String package_amount, String token, String is_used,
                           String used_by, String used_date) {
        this.package_name = package_name;
        this.package_amount = package_amount;
        this.token = token;
        this.is_used = is_used;
        this.used_by = used_by;
        this.used_date = used_date;
    }

    public String getPackage_name() {
        return package_name;
    }

    public void setPackage_name(String package_name) {
        this.package_name = package_name;
    }

    public String getPackage_amount() {
        return package_amount;
    }

    public void setPackage_amount(String package_amount) {
        this.package_amount = package_amount;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getIs_used() {
        return is_used;
    }

    public void setIs_used(String is_used) {
        this.is_used = is_used;
    }

    public String getUsed_by() {
        return used_by;
    }

    public void setUsed_by(String used_by) {
        this.used_by = used_by;
    }

    public String getUsed_date() {
        return used_date;
    }

    public void setUsed_date(String used_date) {
        this.used_date = used_date;
    }
}
