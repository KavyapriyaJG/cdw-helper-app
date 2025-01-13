package com.cdw.helper.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.cdw.helper.app.*"})
public class HelperApp {
    public static void main(String[] args) {
        SpringApplication.run(HelperApp.class, args);
    }
}
