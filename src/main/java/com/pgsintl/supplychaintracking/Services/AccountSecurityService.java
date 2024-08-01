package com.pgsintl.supplychaintracking.Services;

import com.pgsintl.supplychaintracking.Dto.AccountDetails;
import com.pgsintl.supplychaintracking.Dto.AccountLoginDto;
import com.pgsintl.supplychaintracking.Entities.Account;
import com.pgsintl.supplychaintracking.Repository.AccountRepository;

import com.pgsintl.supplychaintracking.Utils.ImageUtils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class AccountSecurityService implements UserDetailsService {

    @Autowired
    private AccountRepository userInfoRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    @Override
    public AccountDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> userInfoMobile ;
        userInfoMobile= userInfoRepository.findByPhoneNumber(username);

        if(userInfoMobile.isEmpty()){
            Optional<Account> userInfoEmail = userInfoRepository.findByEmail(username);
            return userInfoEmail.map(AccountDetails::new).orElseThrow(()-> new UsernameNotFoundException("User not found"+username));

        }
        return userInfoMobile.map(AccountDetails::new)
                .orElseThrow(()-> new UsernameNotFoundException("User not found"+username));
    }

    public AccountLoginDto loginSucces(String username){
        Account account = userInfoRepository.findByPhoneNumber(username).orElse(null);
        AccountLoginDto accountLoginDto = new AccountLoginDto();

        if(account!= null){

            accountLoginDto.setUserNumber(account.getUserNumber());
            accountLoginDto.setEmail(account.getEmail());
            accountLoginDto.setIsAccountNonExpired(account.getIsAccountNonExpired());
            accountLoginDto.setRole(account.getRole());
            accountLoginDto.setName(account.getName());
            accountLoginDto.setPassword(account.getPassword());
            accountLoginDto.setActivateCode(account.getActivateCode());
            accountLoginDto.setIsAccountNonLocked(account.getIsAccountNonLocked());
            accountLoginDto.setIsEnabled(account.getIsEnabled());
            accountLoginDto.setCardNumber(account.getCardNumber());
            accountLoginDto.setSerialNumber(account.getSerialNumber());

            accountLoginDto.setPhoneNumber(account.getPhoneNumber());
            byte[] images= ImageUtils.decompressImage(account.getPhoto());
            accountLoginDto.setPhoto(images);
            accountLoginDto.setToken(jwtService.generateToken(username));
        }else
        {
            Account accountEmail= userInfoRepository.findByEmail(username).orElse(null);
            if(accountEmail!= null){
                accountLoginDto.setUserNumber(accountEmail.getUserNumber());
                accountLoginDto.setEmail(accountEmail.getEmail());
                accountLoginDto.setIsAccountNonExpired(accountEmail.getIsAccountNonExpired());
                accountLoginDto.setRole(accountEmail.getRole());
                accountLoginDto.setName(accountEmail.getName());
                accountLoginDto.setPassword(accountEmail.getPassword());
                accountLoginDto.setActivateCode(accountEmail.getActivateCode());
                accountLoginDto.setIsAccountNonLocked(accountEmail.getIsAccountNonLocked());
                accountLoginDto.setIsEnabled(accountEmail.getIsEnabled());
                byte[] images= ImageUtils.decompressImage(account.getPhoto());
                accountLoginDto.setPhoto(images);
                accountLoginDto.setPhoneNumber(accountEmail.getPhoneNumber());
                accountLoginDto.setToken(jwtService.generateToken(username));
            }
        }

        return accountLoginDto;
    }



    public String addUser(Account userInfo){
        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        userInfoRepository.save(userInfo);
        return "User added successfully";
    }
}