package xyz.ibudai.database.jdbc.mysql;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("xyz.ibudai.dao")
@SpringBootApplication
public class MysqlSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(MysqlSpringApplication.class, args);
    }

}
