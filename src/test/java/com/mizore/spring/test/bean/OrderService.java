package com.mizore.spring.test.bean;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class OrderService implements IOrderService{
    @Override
    public String create(String orderId) {
        try {
            TimeUnit.MILLISECONDS.sleep(new Random().nextInt(100));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return "创建订单： " + orderId;
    }

    @Override
    public String query() {
        try {
            TimeUnit.MILLISECONDS.sleep(new Random().nextInt(100));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return "查询订单成功！！";
    }
}
