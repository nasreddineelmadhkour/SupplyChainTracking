package com.pgsintl.supplychaintracking.Services;

import com.pgsintl.supplychaintracking.Config.TwilioConfig;
import com.pgsintl.supplychaintracking.Dto.AccountLoginDto;
import com.pgsintl.supplychaintracking.Entities.Account;
import com.pgsintl.supplychaintracking.Entities.Role;
import com.pgsintl.supplychaintracking.Repository.AccountRepository;
import com.pgsintl.supplychaintracking.Repository.PhotoRepository;
import com.pgsintl.supplychaintracking.Utils.ImageUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service @AllArgsConstructor
public class AccountService implements AccountIService {
    AccountRepository accountRepository;
    @Autowired
    PhotoRepository photoRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TwilioConfig twilioConfig;
/*
    @PostConstruct
    public void initTwilio(){
        Twilio.init(twilioConfig.getAccountSid(),twilioConfig.getAuthToken());
    }*/

    @Override
    public Account CreatAccountCarrier(Account carrier)
    {
        carrier.setRole(Role.CARRIER);
        carrier.setPassword(passwordEncoder.encode(carrier.getPassword()));
        carrier.setDatecreation(new Date(System.currentTimeMillis()));

        return accountRepository.save(carrier);

    }


    @Override
    public Account CreatAccountDriver(String name, String password , String email, String cardNumber , String serialNumber , String phoneNumber, MultipartFile file, Long idCarrier)throws IOException {

        Account driver = new Account();
        driver.setPhoneNumber(phoneNumber);
        driver.setName(name);
        driver.setEmail(email);
        driver.setCardNumber(cardNumber);
        driver.setSerialNumber(serialNumber);
        driver.setRole(Role.DRIVER);
        driver.setPassword(passwordEncoder.encode(password));
        driver.setDatecreation(new Date(System.currentTimeMillis()));

        driver.setNamePhoto(file.getOriginalFilename());
        driver.setTypePhoto(file.getContentType());
        driver.setPhoto(ImageUtils.compressImage(file.getBytes()));
        /*
        Photo imageData = photoRepository.save(Photo.builder()
                .namePhoto(file.getOriginalFilename())
                .type(file.getContentType())
                .photo(ImageUtils.compressImage(file.getBytes())).build());
*/


        Account carrier = accountRepository.findById(idCarrier).orElse(null);

        carrier.getDrivers().add(driver);


        return accountRepository.save(driver);
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
//            accounts.add();
      //      account.setPhoto(images);
        }
        return accounts;
    }

    @Override
    public List<Account> getAllDriverByCarrier(Long idCarrier) {
        //return accountRepository.findById(idCarrier).get().getDrivers().stream().toList();

        List<Account> accounts = new ArrayList<>();

        for (Account account : accountRepository.findById(idCarrier).get().getDrivers().stream().toList()){
            byte[] images= ImageUtils.decompressImage(account.getPhoto());
            account.setPhoto(images);
            accounts.add(account);
        }

        return accounts;
    }




    @Override
    public boolean SetAllNoPDP(MultipartFile file) throws IOException{

        for (Account account : accountRepository.findAll()){
            account.setNamePhoto(file.getOriginalFilename());
            account.setTypePhoto(file.getContentType());
            account.setPhoto(ImageUtils.compressImage(file.getBytes()));

            accountRepository.save(account);
        }

        return true;
    }

    @Override
    public boolean SendCodeReset(String identity) {

        Account account = accountRepository.findByPhoneNumber(identity).orElse(null);

        if(account!= null){

            account.setCodeTel(String.valueOf(generateCode()));
            System.out.println(account.getCodeTel());
            accountRepository.save(account);

           // PhoneNumber to = new PhoneNumber("+21628000046");
            //PhoneNumber from = new PhoneNumber(twilioConfig.getTrialNumber());
            String m = "Your Code verification code is: "+account.getCodeTel();
            System.out.println(m);
            //Message message = Message
              //         .creator(to,from,m).create();

            return true;
        }/*
        if(account!=null){
            return true;
        }*/

        return false;
    }

    public int generateCode(){
        Random random = new Random();
        return 100000 + random.nextInt(999999 - 100000);

    }


    @Override
    public boolean verifyCode(String code , String identity){
        Account account = accountRepository.findByPhoneNumber(identity).orElse(null);

        if(account != null){
            if(account.getCodeTel().equals(code)){
                account.setCodeTel(null);
                accountRepository.save(account);
                return true;
            }
        }

        return false;
    }

    public boolean ChangePasswordAfterVerification(String newPassword , String identity){
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
        catch (Error e){
            System.out.println(e);
            return false;
        }

    }

    @Override
    public Account updateProfile(Long idAccount, MultipartFile file, String name, String phoneNumber, String email, String password, String isEmail, String isPhone, String isPassword, String isPhoto, String isName) throws IOException {

        Account account = accountRepository.findById(idAccount).orElse(null);
        if(account!=null){

            if(isEmail.equals("true"))
                account.setEmail(email);
            if(isPassword.equals("true"))
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

        byte[] images= ImageUtils.decompressImage(accountReturn.getPhoto());

        accountReturn.setPhoto(images);
        return accountReturn;
    }


}
