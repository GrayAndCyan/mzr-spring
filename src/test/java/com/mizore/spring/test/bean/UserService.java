package com.mizore.spring.test.bean;


import com.mizore.spring.beans.BeansException;
import com.mizore.spring.beans.factory.*;
import com.mizore.spring.context.ApplicationContext;
import com.mizore.spring.context.ApplicationContextAware;
import com.mizore.spring.stereotype.Component;

@Component("myUserService")
public class UserService implements IUserService,InitializingBean, DisposableBean, BeanFactoryAware, BeanClassLoaderAware, BeanNameAware, ApplicationContextAware {

    private String token = "${token}";
    private IUserDao userDao;

    private String name;

    private String uId;
    private String location;
    private String company;


    @Override
    public String toString() {
        return "UserService{" +
                "userDao=" + userDao +
                ", name='" + name + '\'' +
                ", uId='" + uId + '\'' +
                ", location='" + location + '\'' +
                ", company='" + company + '\'' +
                ", token='" + token + '\'' +
                '}';
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
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

    public IUserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(IUserDao userDao) {
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

    @Override
    public void destroy() throws Exception {
        System.out.println("do userService.destroy()");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("do userService.afterPropertiesSet()");
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        System.out.println("get classLoader : " + classLoader);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println("get beanFactory : " + beanFactory);
    }

    @Override
    public void setBeanName(String name) {
        System.out.println("get beanName : " + name);

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("get applicationContext : " + applicationContext);

    }
}
