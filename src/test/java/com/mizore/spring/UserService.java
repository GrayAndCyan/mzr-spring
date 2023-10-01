package com.mizore.spring;


public class UserService {

    private UserDao userDao;

    private String name;

    private String uId;
    public void query() {
        System.out.println("query user...");
        System.out.println(userDao.queryUserName(uId));
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
