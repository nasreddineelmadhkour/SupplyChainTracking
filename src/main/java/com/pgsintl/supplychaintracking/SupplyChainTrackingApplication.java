package com.pgsintl.supplychaintracking;

import lombok.AllArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication @AllArgsConstructor
public class SupplyChainTrackingApplication {
    private JdbcTemplate jdbcTemplate;
    public static void main(String[] args) {
        SpringApplication.run(SupplyChainTrackingApplication.class, args);
    }


}
