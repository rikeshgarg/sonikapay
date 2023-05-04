package com.codunite.model;

public class AEPSCommisionModel {
    private String strstartrange, strendrange, strcommsion,strtype,  strflat;

    public AEPSCommisionModel(String strstartrange, String strendrange, String strcommsion, String strtype, String strflat) {
        this.strstartrange = strstartrange;
        this.strendrange = strendrange;
        this.strcommsion = strcommsion;
        this.strtype = strtype;
        this.strflat = strflat;
    }

    public String getStrstartrange() {
        return strstartrange;
    }

    public void setStrstartrange(String strstartrange) {
        this.strstartrange = strstartrange;
    }

    public String getStrendrange() {
        return strendrange;
    }

    public void setStrendrange(String strendrange) {
        this.strendrange = strendrange;
    }

    public String getStrcommsion() {
        return strcommsion;
    }

    public void setStrcommsion(String strcommsion) {
        this.strcommsion = strcommsion;
    }

    public String getStrtype() {
        return strtype;
    }

    public void setStrtype(String strtype) {
        this.strtype = strtype;
    }

    public String getStrflat() {
        return strflat;
    }

    public void setStrflat(String strflat) {
        this.strflat = strflat;
    }
}
