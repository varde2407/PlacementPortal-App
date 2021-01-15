package com.avv.authentication;

import android.content.Intent;

public class JobHelperClass {
    String jobID;
    String companyID;
    String position;
    String Description;
    int free_positions;
    Boolean status;
    String selected_name;

    public String getSelected_name() {
        return selected_name;
    }

    public void setSelected_name(String selected_name) {
        this.selected_name = selected_name;
    }

    public String getJobID() {
        return jobID;
    }

    public void setJobID(String jobID) {
        this.jobID = jobID;
    }

    public JobHelperClass() {
    }

    public JobHelperClass(String jobID, String companyID, String position, String description, int free_positions, Boolean status, String selected_name) {
        this.companyID = companyID;
        this.position = position;
        Description = description;
        this.free_positions = free_positions;
        this.status = status;
        this.jobID= jobID;
        this.selected_name=selected_name;
    }

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getFree_positions() {
        return free_positions;
    }

    public void setFree_positions(int free_positions) {
        this.free_positions = free_positions;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
