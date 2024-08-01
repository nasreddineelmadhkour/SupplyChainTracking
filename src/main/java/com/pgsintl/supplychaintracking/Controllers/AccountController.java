package com.pgsintl.supplychaintracking.Controllers;

import com.pgsintl.supplychaintracking.Dto.AccountLoginDto;
import com.pgsintl.supplychaintracking.Entities.Account;
import com.pgsintl.supplychaintracking.Entities.AuthRequest;
import com.pgsintl.supplychaintracking.Services.AccountIService;
import com.pgsintl.supplychaintracking.Services.AccountSecurityService;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/account")
@AllArgsConstructor
public class AccountController {


    private AuthenticationManager authenticationManager;

    private AccountIService accountIService;

    private AccountSecurityService accountSecurityService;

    @PostMapping("/login")
    public AccountLoginDto login(@RequestBody AuthRequest authRequest){
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if(authenticate.isAuthenticated()){
            return accountSecurityService.loginSucces(authRequest.getUsername());
        }else {
            throw new BadCredentialsException("Bad credentials");
        }
    }



    @PostMapping("/addCarrier")
    public Account addCarrier(@RequestBody Account carrier) {
        return accountIService.creatAccountCarrier(carrier);
    }



    @PostMapping("/addDriver/{idCarrier}")
    @PreAuthorize("hasAuthority('CARRIER')")
    public ResponseEntity<Account> addDriver(@PathVariable Long idCarrier
            , @RequestParam("image") MultipartFile file
            , @RequestParam("name") String name
            , @RequestParam("phoneNumber") String phoneNumber
            , @RequestParam("email") String email
            , @RequestParam("password") String password
            , @RequestParam("serialNumber") String serialNumber
            , @RequestParam("cardNumber") String cardNumber
    ) throws IOException{



            return accountIService.creatAccountDriver( name,  password ,  email,  cardNumber ,  serialNumber ,  phoneNumber,  file, idCarrier) ;
    }


    @PostMapping("/updateProfile/{idAccount}")
    public ResponseEntity<Account> updateProfile(@PathVariable Long idAccount
            ,@RequestParam("image") MultipartFile file
            ,@RequestParam("name") String name
            ,@RequestParam("phoneNumber") String phoneNumber
            ,@RequestParam("email") String email
            ,@RequestParam("password") String password
            ,@RequestParam("isName") String isName
            ,@RequestParam("isEmail") String isEmail
            ,@RequestParam("isPhone") String isPhone
            ,@RequestParam("isPassword") String isP
            ,@RequestParam("isPhoto") String isPhoto


    ) throws IOException{

        return accountIService.updateProfile(idAccount,file,name,phoneNumber,email,password,isEmail,isPhone,isP,isPhoto,isName);

    }



    @PostMapping("/updateDriverByCarrier/{idDriver}/{isP}")
    @PreAuthorize("hasAuthority('CARRIER')")
    public ResponseEntity<Boolean> updateDriverByCarrier(@PathVariable Long idDriver , @PathVariable String isP, @RequestBody Account driver) {
        return accountIService.updateDriverByCarrier(idDriver,isP,driver);
    }


    @PostMapping("/deleteDriver/{idDriver}")
    @PreAuthorize("hasAuthority('CARRIER')")
    public boolean deleteDriver(@PathVariable Long idDriver){
        return accountIService.deleteDriver(idDriver);
    }


    @GetMapping("/driversByCarrier/{idCarrier}")
    //@PreAuthorize("hasAuthority('CARRIER') and hasAuthority('OTHER_AUTHORITY') and hasAuthority('ANOTHER_AUTHORITY')")
    @PreAuthorize("hasAuthority('CARRIER')")
    public List<Account> getDriversByCarrier(@PathVariable Long idCarrier)
    {
        return accountIService.getAllDriverByCarrier(idCarrier);
    }

    @GetMapping("/admin")
    public List<AccountLoginDto> getAllAccount(){
        return accountIService.getAllUser();
    }


    @PostMapping("/SetAllNoPDP")
    public boolean setAllNoPDP(@RequestParam("image") MultipartFile file) throws IOException {

        return accountIService.setAllNoPDP(file);

    }




    // *************************** RESET PASSWORD WITH TWILIO ****************************************
    @GetMapping("/resetpassword/SendCodeReset/{identity}")
    public boolean sendCodeReset(@PathVariable String identity) throws MessagingException {
        return accountIService.sendCodeReset(identity);
    }

    @GetMapping("/resetpassword/verifyCode/{code}/{identity}")
    public boolean verifyCode(@PathVariable String code ,@PathVariable String identity) {
        return accountIService.verifyCode(code,identity);
    }

    @GetMapping("/resetpassword/ChangePasswordAfterVerification/{newPassword}/{identity}")
    public boolean changePasswordAfterVerification(@PathVariable String newPassword ,@PathVariable String identity){
        return accountIService.changePasswordAfterVerification(newPassword,identity);
    }



}
