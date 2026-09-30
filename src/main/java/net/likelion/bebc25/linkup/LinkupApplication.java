package net.likelion.bebc25.linkup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LinkupApplication {

    public static void main(String[] args) {
        SpringApplication.run(LinkupApplication.class, args);
    }

}