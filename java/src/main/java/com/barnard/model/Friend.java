package com.barnard.model;

public class Friend {

    private int userId;
    private int friendId;
    private int frienshipId;
    private String status;
    private String username;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getFriendId() {
        return friendId;
    }

    public void setFriendId(int friendId) {
        this.friendId = friendId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getFrienshipId() {
        return frienshipId;
    }

    public void setFrienshipId(int frienshipId) {
        this.frienshipId = frienshipId;
    }
}
