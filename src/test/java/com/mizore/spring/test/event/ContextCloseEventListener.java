package com.mizore.spring.test.event;

import com.mizore.spring.context.ApplicationListener;
import com.mizore.spring.context.event.ContextCloseEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ContextCloseEventListener implements ApplicationListener<ContextCloseEvent> {


    @Override
    public void onApplicationEvent(ContextCloseEvent event) {
      log.info("应用上下文关闭 ： {}", event);
      log.info("source ： {}", event.getSource());
    }
}
