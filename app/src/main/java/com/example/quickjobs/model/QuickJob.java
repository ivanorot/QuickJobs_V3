package com.example.quickjobs.model;

import java.util.ArrayList;
import java.util.List;

public class QuickJob {

    private User whoPosted;
    private List<User> whoBidded;

    private String jobTitle;
    private String jobDescription;

    private String amountForJob;
    private String dateOfJob;

    private List<String> jobImages;

    public QuickJob(User inWhoPosted) {
        whoPosted = inWhoPosted;
        whoBidded = new ArrayList<>();
        jobImages = new ArrayList<>();
    }

    public User getOwner() {
        return whoPosted;
    }

    public void setOwner(User inWhoPosted) {
        whoPosted = inWhoPosted;
    }

    public void addBidder(String imageUrl)
    {
        jobImages.add(imageUrl);
    }

    public void removeBidder(String imageUrl)
    {
        jobImages.remove(imageUrl);
    }

    public List<User> getAllBidders(){ return whoBidded;}

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getAmountForJob() {
        return amountForJob;
    }

    public void setAmountForJob(String amountForJob) {
        this.amountForJob = amountForJob;
    }

    public String getDateOfJob() {
        return dateOfJob;
    }

    public void setDateOfJob(String dateOfJob) {
        this.dateOfJob = dateOfJob;
    }

    public List<String> getJobImages() {
        return jobImages;
    }

    public void addImage(String imageUrl)
    {
        jobImages.add(imageUrl);
    }

    public void removeImage(String imageUrl)
    {
        jobImages.remove(imageUrl);
    }

    public List<String> getAllImages(){return jobImages;}
}
