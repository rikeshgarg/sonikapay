package com.codunite.model;

public class HeadTabsModel {
    private String str_name;
    private int cur_pos;

    public HeadTabsModel(String str_name, int cur_pos) {
        this.str_name = str_name;
        this.cur_pos = cur_pos;
    }

    public String getStr_name() {
        return str_name;
    }

    public void setStr_name(String str_name) {
        this.str_name = str_name;
    }

    public int getCur_pos() {
        return cur_pos;
    }

    public void setCur_pos(int cur_pos) {
        this.cur_pos = cur_pos;
    }
}
