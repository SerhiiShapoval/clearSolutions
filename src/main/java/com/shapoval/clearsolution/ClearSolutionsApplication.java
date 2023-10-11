package com.shapoval.clearsolution;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ClearSolutionsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClearSolutionsApplication.class, args);
    }

}
