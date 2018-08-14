package com.howtographql.sampl.hackernewsgraphqljava.configurations;

import com.howtographql.sampl.hackernewsgraphqljava.service.SessionService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Service;

@Service("springBeanUtils")
public class SpringBeanUtils implements BeanFactoryAware {

    private static BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        SpringBeanUtils.beanFactory = beanFactory;
    }

    public static <T> T getBean(Class<T> clazz) {
        return beanFactory.getBean(clazz);
    }

    public static <T> T getBean(Class<T> clazz, String name) {
        return beanFactory.getBean(name, clazz);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        return (T)beanFactory.getBean(name);
    }

    public static SessionService session() {
        return getBean(SessionService.class);
    }
}
