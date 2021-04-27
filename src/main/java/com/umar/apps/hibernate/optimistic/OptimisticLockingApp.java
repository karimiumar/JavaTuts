package com.umar.apps.hibernate.optimistic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.sql.DataSource;

@SpringBootApplication
@Configuration
@EnableJpaRepositories
@EnableAsync
@PropertySources({
        @PropertySource(value = "classpath:application.properties")
})
public class OptimisticLockingApp {

    public static void main(String[] args) {
        SpringApplication.run(OptimisticLockingApp.class, args);
    }

    @Primary
    @Bean(name="applicationDataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource getApplicationDataSource() {
        return DataSourceBuilder.create().build();
    }
}
