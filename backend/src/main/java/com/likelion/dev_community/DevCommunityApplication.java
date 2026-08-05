package com.likelion.dev_community;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.security.autoconfigure.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude = UserDetailsServiceAutoConfiguration.class)
public class DevCommunityApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevCommunityApplication.class, args);
    }

}
