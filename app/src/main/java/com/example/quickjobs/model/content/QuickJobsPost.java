package com.example.quickjobs.model.content;

import com.example.quickjobs.model.user.QuickJobsUser;

import java.util.ArrayList;
import java.util.List;

public class QuickJobsPost {

    private QuickJobsUser whoPosted;
    private List<QuickJobsUser> whoBidded;

    private String jobTitle;
    private String jobDescription;

    private String amountForJob;
    private String dateOfJob;

    private List<String> jobImages;

    public QuickJobsPost(QuickJobsUser inWhoPosted) {
        whoPosted = inWhoPosted;
        whoBidded = new ArrayList<>();
        jobImages = new ArrayList<>();
    }

    public QuickJobsUser getOwner() {
        return whoPosted;
    }

    public void setOwner(QuickJobsUser inWhoPosted) {
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

    public List<QuickJobsUser> getAllBidders(){ return whoBidded;}

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
