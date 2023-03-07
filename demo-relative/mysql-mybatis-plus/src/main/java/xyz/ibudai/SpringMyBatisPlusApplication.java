package xyz.ibudai;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("xyz.ibudai.dao")
public class SpringMyBatisPlusApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringMyBatisPlusApplication.class, args);
    }

}
