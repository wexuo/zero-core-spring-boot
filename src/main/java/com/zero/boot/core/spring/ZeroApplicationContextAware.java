package com.zero.boot.core.spring;

import com.zero.boot.core.util.SpringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ZeroApplicationContextAware implements ApplicationContextAware {
    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        SpringUtils.setApplicationContext(applicationContext);
    }
}
