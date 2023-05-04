package com.codunite.model;

public class AepsIncomeModel {
    private String strservice, strtxdid, strdate, strcommision,stramount;

    public AepsIncomeModel(String strservice, String strtxdid, String strdate, String strcommision, String stramount) {
        this.strservice = strservice;
        this.strtxdid = strtxdid;
        this.strdate = strdate;
        this.strcommision = strcommision;
        this.stramount = stramount;
    }

    public String getStrservice() {
        return strservice;
    }

    public void setStrservice(String strservice) {
        this.strservice = strservice;
    }

    public String getStrtxdid() {
        return strtxdid;
    }

    public void setStrtxdid(String strtxdid) {
        this.strtxdid = strtxdid;
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
