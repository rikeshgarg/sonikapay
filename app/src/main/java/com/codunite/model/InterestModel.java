package com.codunite.model;

public class InterestModel {
    private String interstId, interestImage, interestName;
    private boolean isChecked = false;

    public InterestModel(String interstId, String interestImage, String interestName) {
        this.interstId = interstId;
        this.interestImage = interestImage;
        this.interestName = interestName;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getInterstId() {
        return interstId;
    }

    public void setInterstId(String interstId) {
        this.interstId = interstId;
    }

    public String getInterestImage() {
        return interestImage;
    }

    public void setInterestImage(String interestImage) {
        this.interestImage = interestImage;
    }

    public String getInterestName() {
        return interestName;
    }

    public void setInterestName(String interestName) {
        this.interestName = interestName;
    }

}
