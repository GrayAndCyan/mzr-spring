package com.mizore.spring;

public class User {
    private String username;

    private int userId;

    public User(String username) {
        this.username = username;
    }

    public User(int userId) {
        this.userId = userId;
    }

    public User(String username, int userId) {
        this.username = username;
        this.userId = userId;
    }

    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", userId=" + userId +
                '}';
    }
}
