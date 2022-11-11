package com.boyun.boyunfile;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.boyun.boyunfile.mapper")
@SpringBootApplication
public class BoyunFileApplication {

    public static void main(String[] args) {
        SpringApplication.run(BoyunFileApplication.class, args);
    }

}
