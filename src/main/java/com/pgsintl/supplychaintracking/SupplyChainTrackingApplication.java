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
    AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    public static void main(String[] args) {
        SpringApplication.run(SupplyChainTrackingApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {


        Account account = new Account();
        account= accountRepository.findById(1L).orElse(null);

        if(account==null){
            Account account2 = new Account();
            account2.setPassword(passwordEncoder.encode("admin"));
            account2.setName("Nasreddine Madhkour");
            account2.setEmail("nasreddine.elmadhkour@gmail.com");
            account2.setRole(Role.CARRIER);
            account2.setPhoneNumber("28000046");
            account2.setDatecreation(new Date());
            accountRepository.save(account2);
        }


    }

}
