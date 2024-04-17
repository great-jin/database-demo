package xyz.ibudai.database.jdbc.mysql;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("xyz.ibudai.database.jdbc.mysql")
public class SpringMyBatisPlusApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringMyBatisPlusApplication.class, args);
    }

}
