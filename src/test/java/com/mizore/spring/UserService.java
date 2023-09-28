package com.mizore.spring;


public class UserService {

    UserDao userDao;

    String name;
    public void query() {
        System.out.println("query user...");
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
