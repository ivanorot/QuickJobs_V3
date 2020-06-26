package com.example.quickjobs.model.user;

import com.google.firebase.auth.FirebaseUser;

public class QuickJobsUser {

    private final String uid;

    private String emailAddress;
    private String displayName;

    private String phoneNumber;

    private float longitude;
    private float latitude;

    public QuickJobsUser(String inUid)
    {
        uid = inUid;
    }

    public QuickJobsUser(String inUid, String inDisplayName) {
        uid = inUid;
        displayName = inDisplayName;
    }

    public QuickJobsUser(FirebaseUser firebaseUser){
        uid = firebaseUser.getUid();

        if(firebaseUser.getEmail() != null) {
            emailAddress = firebaseUser.getEmail();
        }

        if(firebaseUser.getDisplayName() != null){
            displayName = firebaseUser.getDisplayName();
        }

        if(firebaseUser.getPhoneNumber() != null) {
            phoneNumber = firebaseUser.getPhoneNumber();
        }

    }

    public String getUid() {
        return uid;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }
}
