package com.example.quickjobs.model.beans;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class User {

    private final String uid;

    private String emailAddress;
    private String displayName;

    private String phoneNumber;

    private float longitude;
    private float latitude;

    private List<QuickJob> myJobs;
    private List<String> photos;

    private boolean isAuthenticated;
    private boolean isNew;
    private boolean isCreated;

    public User(String inUid)
    {
        uid = inUid;

        myJobs = new ArrayList<>();
        photos = new ArrayList<>();
    }

    public User(String inUid, String inDisplayName) {
        uid = inUid;
        displayName = inDisplayName;

        myJobs = new ArrayList<>();
        photos = new ArrayList<>();
    }

    public User(FirebaseUser firebaseUser){
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

    public void addQuickJob(QuickJob quickJob){
        myJobs.add(quickJob);
    }

    public void removeQuickJob(QuickJob quickJob){
        myJobs.remove(quickJob);
    }

    public List<QuickJob> getMyJobs(){
        return myJobs;
    }

    public void addPhoto(String urlPhoto){
        photos.add(urlPhoto);
    }

    public void removePhoto(String urlPhoto){
        photos.remove(urlPhoto);
    }

    public List<String> getPhotos(){
        return photos;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        isAuthenticated = authenticated;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public boolean isCreated() {
        return isCreated;
    }

    public void setCreated(boolean created) {
        isCreated = created;
    }
}
