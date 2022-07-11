package xyz.ibudai;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("xyz.ibudai.dao")
public class OracleSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(OracleSpringApplication.class, args);
    }

}
