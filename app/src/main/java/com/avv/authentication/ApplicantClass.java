package com.avv.authentication;

public class ApplicantClass {
    String jobID;
    String ApplicationID;
    String Name;
    String Application;

    public ApplicantClass(String jobID, String applicationID, String name, String application) {
        this.jobID = jobID;
        ApplicationID = applicationID;
        Name = name;
        Application = application;
    }

    public ApplicantClass() {
    }

    public String getJobID() {
        return jobID;
    }

    public void setJobID(String jobID) {
        this.jobID = jobID;
    }

    public String getApplicationID() {
        return ApplicationID;
    }

    public void setApplicationID(String applicationID) {
        ApplicationID = applicationID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getApplication() {
        return Application;
    }

    public void setApplication(String application) {
        Application = application;
    }
}
