package com.mizore.spring.test.bean;


import cn.hutool.core.lang.copier.SrcToDestCopier;
import com.mizore.spring.test.bean.UserDao;

public class UserService {

    private UserDao userDao;

    private String name;

    private String uId;
    private String location;
    private String company;
    public void query() {
        System.out.println("query user...");
        System.out.println(userDao.queryUserName(uId));
        System.out.println("location: " + location);
        System.out.println("company: " + company);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
