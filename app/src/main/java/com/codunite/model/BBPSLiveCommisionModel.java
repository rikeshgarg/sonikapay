package com.codunite.model;

public class BBPSLiveCommisionModel {
    private String strOperator,strcommision,strflat,strsurcharge;

    public BBPSLiveCommisionModel(String strOperator,String strcommision, String strflat, String strsurcharge) {
        this.strOperator = strOperator;
        this.strcommision = strcommision;
        this.strflat = strflat;
        this.strsurcharge = strsurcharge;
    }

    public String getStrOperator() {
        return strOperator;
    }

    public void setStrOperator(String strOperator) {
        this.strOperator = strOperator;
    }

    public String getStrcommision() {
        return strcommision;
    }

    public void setStrcommision(String strcommision) {
        this.strcommision = strcommision;
    }

    public String getStrflat() {
        return strflat;
    }

    public void setStrflat(String strflat) {
        this.strflat = strflat;
    }

    public String getStrsurcharge() {
        return strsurcharge;
    }

    public void setStrsurcharge(String strsurcharge) {
        this.strsurcharge = strsurcharge;
    }
}
