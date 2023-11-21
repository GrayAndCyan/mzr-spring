package com.mizore.spring.test.bean;

import com.mizore.spring.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserDao implements IUserDao{
    private static Map<String, String> hashMap = new HashMap<>();

    public void initDataMethod() {
        hashMap.put("10001", "mizore");
        hashMap.put("10002", "八杯水");
        hashMap.put("10003", "阿毛");
    }

    public void destroyDataMethod() {
        System.out.println("UserDao : do destroy method");
        hashMap.clear();
    }
    public String queryUserName(String uId) {
        return hashMap.get(uId);
    }
}
