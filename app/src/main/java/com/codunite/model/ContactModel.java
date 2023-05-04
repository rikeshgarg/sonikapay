package com.codunite.model;

public class ContactModel {

    private String id;
    private String contactId;
    private String contactNumber;
    private String contactName;
    private String imageUri;

    public ContactModel() {
        // empty constructor
    }

    public ContactModel(String id, String contactId, String contactNumber, String contactName, String imageUri) {
        this.id = id;
        this.contactId = contactId;
        this.contactNumber = contactNumber;
        this.contactName = contactName;
        this.imageUri = imageUri;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

}