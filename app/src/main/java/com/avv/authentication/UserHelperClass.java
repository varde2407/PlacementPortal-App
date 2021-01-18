package com.avv.authentication;

public class UserHelperClass {
    String username;
    String password;
    String name;
    Float cpi;
    String desc;
    String imageuri;
    String resumeuri;
    String branch;
    int gender;
    Boolean isStudent;

    public UserHelperClass(String username, String password,String name, String branch, Boolean isStudent, Float cpi , String desc, String imageuri, String resumeuri, int gender) {
        this.username = username;
        this.password = password;
        this.isStudent = isStudent;
        this.name = name;
        this.branch = branch;
        this.cpi = cpi;
        this.desc = desc;
        this.imageuri = imageuri;
        this.resumeuri = resumeuri;
        this.gender = gender;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getResumeuri() {
        return resumeuri;
    }

    public void setResumeuri(String resumeuri) {
        this.resumeuri = resumeuri;
    }

    public String getImageuri() {
        return imageuri;
    }

    public void setImageuri(String imageuri) {
        this.imageuri = imageuri;
    }

    public UserHelperClass() {
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Float getCpi() {
        return cpi;
    }

    public void setCpi(Float cpi) {
        this.cpi = cpi;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getStudent() {
        return isStudent;
    }

    public void setStudent(Boolean student) {
        isStudent = student;
    }
}
