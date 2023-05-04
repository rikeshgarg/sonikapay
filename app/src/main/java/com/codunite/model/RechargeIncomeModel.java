package com.codunite.model;

public class RechargeIncomeModel {
    private String strrecharge, strdate, strcommision,stramount;

    public RechargeIncomeModel(String strrecharge, String strdate, String strcommision, String stramount) {
        this.strrecharge = strrecharge;
        this.strdate = strdate;
        this.strcommision = strcommision;
        this.stramount = stramount;
    }

    public String getStrrecharge() {
        return strrecharge;
    }

    public void setStrrecharge(String strrecharge) {
        this.strrecharge = strrecharge;
    }

    public String getStrdate() {
        return strdate;
    }

    public void setStrdate(String strdate) {
        this.strdate = strdate;
    }

    public String getStrcommision() {
        return strcommision;
    }

    public void setStrcommision(String strcommision) {
        this.strcommision = strcommision;
    }

    public String getStramount() {
        return stramount;
    }

    public void setStramount(String stramount) {
        this.stramount = stramount;
    }
}
