package com.appsdeveloperblog.app.ws;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

//The ApplicationContextAware interface is part of the Spring framework
// and allows a bean to be aware of the ApplicationContext to which it belongs.
//By adding @Component to the SpringApplicationContext class, you are telling Spring to treat it as a component and automatically register it as a bean in the application context during component scanning.
//Since ApplicationContextAware is an interface that is often implemented by beans to gain access to the application context, you want Spring to manage the lifecycle of this bean, and marking it with @Component achieves that.
//The @Component annotation ensures that SpringApplicationContext is registered as a bean in the Spring container.
//When the bean is created, the setApplicationContext method is automatically called by Spring, providing the application context.
//This allows the SpringApplicationContext to store a reference to the application context, and the getBean method can be used to retrieve other beans dynamically.
@Component
public class SpringApplicationContext implements ApplicationContextAware
{
    //The ApplicationContext is a central interface in the Spring Framework for managing the configuration and lifecycle
    // of beans within a Spring application. It represents the Spring IoC (Inversion of Control) container and provides
    // mechanisms for wiring together components, managing bean lifecycle, and handling other aspects of application configuration.
    private static ApplicationContext CONTEXT;



    // This method is called automatically by the Spring framework during the initialization of the bean, providing the
    // application context as a parameter. In this case, it sets the application context to the static field CONTEXT.
    /*
    * Here's a brief overview of the sequence of events:

    Bean Creation and Initialization:

    When the Spring container is creating and initializing beans, it identifies beans that implement the ApplicationContextAware interface.
    Identification of ApplicationContextAware Beans:

    When a bean is identified as implementing ApplicationContextAware, the Spring container automatically calls the setApplicationContext method on that bean.
    Passing the ApplicationContext:

    The ApplicationContext is passed to the setApplicationContext method as an argument.
    Initialization Continues:

    The initialization process of the bean continues after the setApplicationContext method has been invoked.
    * */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        CONTEXT = applicationContext;
    }

    //This method can used to get any Bean we need by passing a string name;
    public static Object getBeanByName(String beanName)
    {
        return CONTEXT.getBean(beanName);
    }

    public static <T> T getBeanByType(Class<T> requiredType) {
        return CONTEXT.getBean(requiredType);
    }
}
