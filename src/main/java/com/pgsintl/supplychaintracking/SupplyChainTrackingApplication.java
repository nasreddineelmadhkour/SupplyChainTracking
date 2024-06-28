package com.pgsintl.supplychaintracking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class SupplyChainTrackingApplication implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static void main(String[] args) {
        SpringApplication.run(SupplyChainTrackingApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        createDatabaseIfNotExists();
    }

    private void createDatabaseIfNotExists() {
        // Example SQL to create database if it doesn't exist
        jdbcTemplate.execute("IF NOT EXISTS (SELECT 1 FROM sys.databases WHERE name = 'pgsDB')\n" +
                "BEGIN\n" +
                "    CREATE DATABASE pgsDB;\n" +
                "END;");
    }
}
