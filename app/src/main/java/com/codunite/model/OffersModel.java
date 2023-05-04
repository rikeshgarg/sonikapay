package com.codunite.model;

public class OffersModel {
    private String offerPrice, offerDesc;

    public OffersModel(String offerPrice, String offerDesc) {
        this.offerPrice = offerPrice;
        this.offerDesc = offerDesc;
    }

    public String getOfferPrice() {
        return offerPrice;
    }

    public void setOfferPrice(String offerPrice) {
        this.offerPrice = offerPrice;
    }

    public String getOfferDesc() {
        return offerDesc;
    }

    public void setOfferDesc(String offerDesc) {
        this.offerDesc = offerDesc;
    }
}
