package com.mizore.spring.test.event;

import com.mizore.spring.context.ApplicationListener;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomListener implements ApplicationListener<CustomEvent> {

    // 被通知到事件发生了 该做些什么动作
    @Override
    public void onApplicationEvent(CustomEvent event) {
      log.info("event： {}", event);
      log.info("source: {}", event.getSource());
    }
}
