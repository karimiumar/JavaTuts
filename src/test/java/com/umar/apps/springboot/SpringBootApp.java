package com.umar.apps.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;

import java.util.Arrays;

@SpringBootApplication
@ImportResource({"classpath*:applicationContext.xml"})
public class SpringBootApp {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(SpringBootApp.class, args);
        Arrays.stream(context.getBeanDefinitionNames()).forEach(System.out::println);
        for(String name: context.getBeanDefinitionNames()) {
            System.out.println(name);
        }
        String bean1 = (String)context.getBean("xmlStringBean1");
        System.out.println(bean1);
    }
}
