package com.barnard.model;

public class UserAttributes {

    private int userId;
    private String email;
    private int nurtureState;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getNurtureState() {
        return nurtureState;
    }

    public void setNurtureState(int nurtureState) {
        this.nurtureState = nurtureState;
    }
}
