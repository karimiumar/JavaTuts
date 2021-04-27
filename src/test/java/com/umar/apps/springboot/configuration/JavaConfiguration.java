package com.umar.apps.springboot.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JavaConfiguration {

    @Bean
    public String getBean1(){
        return "bean1";
    }

    @Bean
    public String getBean2(){
        return "bean2";
    }
}
