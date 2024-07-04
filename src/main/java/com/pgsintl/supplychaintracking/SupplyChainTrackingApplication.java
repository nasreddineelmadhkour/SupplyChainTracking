package com.pgsintl.supplychaintracking;

import com.pgsintl.supplychaintracking.Entities.Account;
import com.pgsintl.supplychaintracking.Entities.Role;
import com.pgsintl.supplychaintracking.Repository.AccountRepository;
import com.pgsintl.supplychaintracking.Utils.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;

@SpringBootApplication
public class SupplyChainTrackingApplication implements CommandLineRunner {


    @Autowired
    private JdbcTemplate jdbcTemplate;
    public static void main(String[] args) {
        SpringApplication.run(SupplyChainTrackingApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {


    }

}
