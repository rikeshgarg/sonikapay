package com.codunite.model;

public class DMRCommisionModel {
    private String strstartrange, strendrange,strflat,strsurcharge;

    public DMRCommisionModel(String strstartrange, String strendrange, String strflat, String strsurcharge) {
        this.strstartrange = strstartrange;
        this.strendrange = strendrange;
        this.strflat = strflat;
        this.strsurcharge = strsurcharge;
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
