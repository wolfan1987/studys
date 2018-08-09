package com.zjht.cardsys;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.zjht.cardsys.orgquery.mapper")
public class AorServer {

    public static void main(String[] args) {

        SpringApplication.run(AorServer.class, args);

    }

}
