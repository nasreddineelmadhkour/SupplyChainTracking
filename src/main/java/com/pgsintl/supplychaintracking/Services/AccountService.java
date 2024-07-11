package com.pgsintl.supplychaintracking.Services;

import com.pgsintl.supplychaintracking.Config.TwilioConfig;
import com.pgsintl.supplychaintracking.Dto.AccountLoginDto;
import com.pgsintl.supplychaintracking.Entities.Account;
import com.pgsintl.supplychaintracking.Entities.Role;
import com.pgsintl.supplychaintracking.Repository.AccountRepository;
import com.pgsintl.supplychaintracking.Utils.ImageUtils;
import com.twilio.Twilio;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.*;

@Service
@Slf4j
@AllArgsConstructor
public class AccountService implements AccountIService {

    private AccountRepository accountRepository;

    private PasswordEncoder passwordEncoder;




    @Override
    public Account creatAccountCarrier(Account carrier)
    {
        carrier.setRole(Role.CARRIER);
        carrier.setPassword(passwordEncoder.encode(carrier.getPassword()));
        carrier.setDatecreation(new Date(System.currentTimeMillis()));

        return accountRepository.save(carrier);

    }


    @Override
    public Account creatAccountDriver(String name, String password , String email, String cardNumber , String serialNumber , String phoneNumber, MultipartFile file, Long idCarrier)throws IOException {

        Account newdriver = new Account();
        newdriver.setPhoneNumber(phoneNumber);
        newdriver.setName(name);
        newdriver.setEmail(email);
        newdriver.setCardNumber(cardNumber);
        newdriver.setSerialNumber(serialNumber);
        newdriver.setRole(Role.DRIVER);
        newdriver.setPassword(passwordEncoder.encode(password));
        newdriver.setDatecreation(new Date(System.currentTimeMillis()));

        newdriver.setNamePhoto(file.getOriginalFilename());
        newdriver.setTypePhoto(file.getContentType());
        newdriver.setPhoto(ImageUtils.compressImage(file.getBytes()));

        Account carrier = accountRepository.findById(idCarrier).orElse(null);

        assert carrier != null;
        carrier.getDrivers().add(newdriver);


        return accountRepository.save(newdriver);
    }


    @Override
    public List<AccountLoginDto> getAllUser(){

        List<AccountLoginDto> accounts = new ArrayList<>();
        for (Account account:accountRepository.findAll()){
            byte[] images= ImageUtils.decompressImage(account.getPhoto());

            AccountLoginDto accountdto = new AccountLoginDto();
            accountdto.setPhoto(images);
            accountdto.setName(account.getName());
            accountdto.setUserNumber(account.getUserNumber());
            accountdto.setRole(account.getRole());
            accountdto.setPhoneNumber(account.getPhoneNumber());
            accountdto.setEmail(account.getEmail());

            accounts.add(accountdto);
        }
        return accounts;
    }

    @Override
    public List<Account> getAllDriverByCarrier(Long idCarrier) {
        List<Account> accounts = new ArrayList<>();

        Optional<Account> optionalCarrierAccount = accountRepository.findById(idCarrier);

        if (optionalCarrierAccount.isPresent()) {
            Account carrierAccount = optionalCarrierAccount.get();

            for (Account account : carrierAccount.getDrivers()) {
                byte[] images = ImageUtils.decompressImage(account.getPhoto());
                account.setPhoto(images);
                accounts.add(account);
            }
        } else {

            log.info("Carrier account with ID " + idCarrier + " not found.");
        }

        return accounts;
    }





    @Override
    public boolean setAllNoPDP(MultipartFile file) throws IOException{

        for (Account account : accountRepository.findAll()){
            account.setNamePhoto(file.getOriginalFilename());
            account.setTypePhoto(file.getContentType());
            account.setPhoto(ImageUtils.compressImage(file.getBytes()));

            accountRepository.save(account);
        }

        return true;
    }

    @Override
    public boolean sendCodeReset(String identity) {


        Optional<Account> account = accountRepository.findByPhoneNumber(identity);

        if(account.isPresent()){
            Account account1 = account.get();
            account1.setCodeTel(String.valueOf(generateCode()));
            log.info(account1.getCodeTel());
            accountRepository.save(account1);
            String m = "Your Code verification code is: "+account1.getCodeTel();
            log.info(m);
            return true;
        }
        return false;
    }

    public int generateCode(){
        SecureRandom random = new SecureRandom();
        return 100000 + random.nextInt(999999 - 100000);

    }


    @Override
    public boolean verifyCode(String code , String identity){
        Account account = accountRepository.findByPhoneNumber(identity).orElse(null);

        if(account != null && account.getCodeTel().equals(code)){

                account.setCodeTel(null);
                accountRepository.save(account);
                return true;

        }

        return false;
    }

    public boolean changePasswordAfterVerification(String newPassword , String identity){
        Account account = accountRepository.findByPhoneNumber(identity).orElse(null);
        if(account!=null){
            account.setPassword(passwordEncoder.encode(newPassword));
            accountRepository.save(account);
            return true;
        }


        return false;
    }

    @Override
    public boolean deleteDriver(Long idDriver) {

        try {
            accountRepository.deleteById(idDriver);
            return true;
        }
        catch (RuntimeException e){
            log.error("Error :" +e);
            return false;
        }

    }

    @Override
    public Account updateProfile(Long idAccount, MultipartFile file, String name, String phoneNumber, String email, String password, String isEmail, String isPhone, String isP, String isPhoto, String isName) throws IOException {

        Account account = accountRepository.findById(idAccount).orElse(null);
        if(account!=null){

            if(isEmail.equals("true"))
                account.setEmail(email);
            if(isP.equals("true"))
                account.setPassword(passwordEncoder.encode(password));
            if(isPhone.equals("true"))
                account.setPhoneNumber(phoneNumber);
            if(isName.equals("true"))
                account.setName(name);
            if(isPhoto.equals("true")) {
                account.setNamePhoto(file.getOriginalFilename());
                account.setTypePhoto(file.getContentType());
                account.setPhoto(ImageUtils.compressImage(file.getBytes()));
            }





            accountRepository.save(account);
        }

        Account accountReturn= accountRepository.findById(idAccount).orElse(null);

        assert accountReturn != null;
        byte[] images= ImageUtils.decompressImage(accountReturn.getPhoto());

        accountReturn.setPhoto(images);
        return accountReturn;
    }


}
