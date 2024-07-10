package com.pgsintl.supplychaintracking.Services;

import com.pgsintl.supplychaintracking.Dto.AccountLoginDto;
import com.pgsintl.supplychaintracking.Entities.Account;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AccountIService {

    public Account creatAccountCarrier(Account account);

    public Account creatAccountDriver(String name, String password , String email, String cardNumber , String serialNumber , String phoneNumber, MultipartFile file, Long idCarrier)throws IOException;

    public List<AccountLoginDto> getAllUser();

    public List<Account> getAllDriverByCarrier(Long idCarrier);
    public boolean setAllNoPDP(MultipartFile file) throws IOException;

    boolean sendCodeReset(String identity);
    public boolean verifyCode(String code , String identity);
    public boolean changePasswordAfterVerification(String newPassword , String identity);


    public boolean deleteDriver(Long idDriver);

    Account updateProfile(Long idAccount, MultipartFile file, String name, String phoneNumber, String email, String password, String isEmail, String isPhone, String isPassword, String isPhoto, String isName) throws IOException;
}
