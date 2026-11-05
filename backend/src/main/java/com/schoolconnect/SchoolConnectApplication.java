package com.schoolconnect;

import jakarta.annotation.PostConstruct;
import java.util.TimeZone;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SchoolConnectApplication {

    public static void main(String[] args) {
        SpringApplication.run(SchoolConnectApplication.class, args);
    }

    @PostConstruct
    public void initTimezone() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
    }
}

