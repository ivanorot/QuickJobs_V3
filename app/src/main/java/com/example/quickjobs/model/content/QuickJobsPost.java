package com.example.quickjobs.model.content;

import java.util.ArrayList;

public class QuickJobsPost {

    private String whoPostedEmail;
    private String whoPostedDisplayName;

    private String jobTitle;
    private String jobDescription;

    private ArrayList<String> jobImages;

    public QuickJobsPost(String whoPostedEmail, String whoPostedDisplayName) {
        this.whoPostedEmail = whoPostedEmail;
        this.whoPostedDisplayName = whoPostedDisplayName;
        jobImages = new ArrayList<>();
    }

    public String getWhoPostedEmail() {
        return whoPostedEmail;
    }

    public void setWhoPostedEmail(String whoPostedEmail) {
        this.whoPostedEmail = whoPostedEmail;
    }

    public String getWhoPostedDisplayName() {
        return whoPostedDisplayName;
    }

    public void setWhoPostedDisplayName(String whoPostedDisplayName) {
        this.whoPostedDisplayName = whoPostedDisplayName;
    }

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

    public ArrayList<String> getJobImages() {
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
}
