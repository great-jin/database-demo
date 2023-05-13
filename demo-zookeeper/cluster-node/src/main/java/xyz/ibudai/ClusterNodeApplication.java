package xyz.ibudai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ClusterNodeApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClusterNodeApplication.class, args);
    }

}
