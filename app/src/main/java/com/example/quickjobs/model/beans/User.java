package com.example.quickjobs.model.beans;

import android.location.Address;
import android.net.Uri;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {

    private  String uid;

    private String emailAddress;
    private String displayName;

    private String bio;

    private String phoneNumber;

    private float longitude;
    private float latitude;

    private Address address;

    private List<QuickJob> myJobs;
    private List<String> photos;

    @Exclude
    private boolean isAuthenticated, isNew, isCreated;


    public User() {
    }

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

    public void setUid(String uid) {
        this.uid = uid;
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
