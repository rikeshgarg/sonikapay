package com.codunite.model;

public class ViewAllPlans {
    private String plansPrice, plansDesc, plansValidty;
    private String sectionName;
    public boolean section = false;

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public ViewAllPlans(String sectionName, boolean section) {
        this.sectionName = sectionName;
        this.section = section;
    }

    public ViewAllPlans(String plansPrice, String plansDesc, String plansValidty, boolean section) {
        this.plansPrice = plansPrice;
        this.plansDesc = plansDesc;
        this.plansValidty = plansValidty;
        this.section = section;
    }

    public String getPlansPrice() {
        return plansPrice;
    }

    public void setPlansPrice(String plansPrice) {
        this.plansPrice = plansPrice;
    }

    public String getPlansDesc() {
        return plansDesc;
    }

    public void setPlansDesc(String plansDesc) {
        this.plansDesc = plansDesc;
    }

    public String getPlansValidty() {
        return plansValidty;
    }

    public void setPlansValidty(String plansValidty) {
        this.plansValidty = plansValidty;
    }

    public boolean isSection() {
        return section;
    }

    public void setSection(boolean section) {
        this.section = section;
    }
}
