package com.codunite.model;

public class BBPSCommisionModel {
    private String strOperator, strcode, strType, strcommision,strflat,strsurcharge;

    public BBPSCommisionModel(String strOperator, String strcode, String strType, String strcommision, String strflat, String strsurcharge) {
        this.strOperator = strOperator;
        this.strcode = strcode;
        this.strType = strType;
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

    public String getStrcode() {
        return strcode;
    }

    public void setStrcode(String strcode) {
        this.strcode = strcode;
    }

    public String getStrType() {
        return strType;
    }

    public void setStrType(String strType) {
        this.strType = strType;
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
