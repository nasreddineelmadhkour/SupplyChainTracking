package com.pgsintl.supplychaintracking.Services;

import com.pgsintl.supplychaintracking.Dto.AccountLoginDto;
import com.pgsintl.supplychaintracking.Entities.Account;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AccountIService {

    public Account CreatAccountCarrier(Account account);

    public Account CreatAccountDriver(String name, String password , String email, String cardNumber , String serialNumber , String phoneNumber, MultipartFile file, Long idCarrier)throws IOException;

    public List<AccountLoginDto> getAllUser();

    public List<Account> getAllDriverByCarrier(Long idCarrier);
    public boolean SetAllNoPDP(MultipartFile file) throws IOException;

    boolean SendCodeReset(String identity);
    public boolean verifyCode(String code , String identity);
    public boolean ChangePasswordAfterVerification(String newPassword , String identity);


    public boolean deleteDriver(Long idDriver);

    }
