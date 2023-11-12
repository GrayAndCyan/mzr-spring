package com.mizore.spring.test.event;

import com.mizore.spring.context.ApplicationListener;
import com.mizore.spring.context.event.ContextRefreshEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ContextRefreshEventListener implements ApplicationListener<ContextRefreshEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshEvent event) {
      log.info("监听到应用上下文刷新 ： {}", event);
      log.info("source ： {}", event.getSource());
    }
}
