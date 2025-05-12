package com.company.project;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.company.project",
        "com.company.project.entity"
})
@EntityScan(basePackages = {"com.company.project.entity"})
@ComponentScan(basePackageClasses = {
        //PORepositoryImpl.class, Account1RepositoryImpl.class
})
@ImportAutoConfiguration({FeignAutoConfiguration.class})
public class HsmServiceApplication {
    static final Logger logger = LoggerFactory.getLogger(HsmServiceApplication.class);

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(HsmServiceApplication.class, args);
    }
}
