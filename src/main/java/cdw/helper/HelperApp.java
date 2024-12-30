package cdw.helper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"cdw.helper.*"})
public class HelperApp {
    public static void main(String[] args) {
        SpringApplication.run(HelperApp.class, args);
    }
}
