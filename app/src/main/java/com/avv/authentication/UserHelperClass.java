package com.avv.authentication;

public class UserHelperClass {
    String username;
    String password;
    String name;
    Boolean isStudent;

    public UserHelperClass(String username, String password, Boolean isStudent) {
        this.username = username;
        this.password = password;
        this.isStudent = isStudent;
    }

    public UserHelperClass() {
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
